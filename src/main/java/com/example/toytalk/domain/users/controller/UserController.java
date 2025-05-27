package com.example.toytalk.domain.users.controller;

import com.example.toytalk.domain.users.assembler.UserModelAssembler;
import com.example.toytalk.domain.users.dto.SignupRequestDTO;
import com.example.toytalk.domain.users.dto.UserResponseDTO;
import com.example.toytalk.domain.users.dto.UserUpdateRequestDTO;
import com.example.toytalk.domain.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserModelAssembler userModelAssembler;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody SignupRequestDTO requestDTO) {
        UserResponseDTO responseDTO = userService.createUser(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /**
     * HATEOAS 적용
     * @param userId
     * @return Activated 사용자 반환
     */
    @GetMapping("/{userId}")
    public ResponseEntity<EntityModel<UserResponseDTO>> getUser(@PathVariable UUID userId) {
        UserResponseDTO responseDTO = userService.getUser(userId);

        EntityModel<UserResponseDTO> userModel = userModelAssembler.toModel(responseDTO);

        return new ResponseEntity<>(userModel ,HttpStatus.OK);
    }

    /**
     * HATEOAS 적용
     * @return Activated 사용자 반환
     */
    @GetMapping("")
    public ResponseEntity<CollectionModel<EntityModel<UserResponseDTO>>> getAllUsers() {
        List<UserResponseDTO> responseDTOList = userService.getAllUsers();

        List<EntityModel<UserResponseDTO>> userModels = responseDTOList.stream()
                .map(userModelAssembler::toModel)
                .toList();

        CollectionModel<EntityModel<UserResponseDTO>> collectionModel =
                CollectionModel.of(userModels,
                        linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    /**
     * HATEOAS 미적용
     * @param userId
     * @return Activated 사용자 반환
     */
//    @GetMapping("/{userId}")
//    public ResponseEntity<UserResponseDTO> getUser(@PathVariable UUID userId) {
//        UserResponseDTO responseDTO = userService.getUser(userId);
//        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
//    }

    /**
     * HATEOAS 미적용
     * @return Activated 사용자 반환
     */
//    @GetMapping("")
//    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
//        List<UserResponseDTO> responseDTOList = userService.getAllUsers();
//        return new ResponseEntity<>(responseDTOList, HttpStatus.OK);
//    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID userId,
                                                      @Valid @RequestBody UserUpdateRequestDTO requestDTO) {
        UserResponseDTO responseDTO = userService.updateUser(userId, requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
