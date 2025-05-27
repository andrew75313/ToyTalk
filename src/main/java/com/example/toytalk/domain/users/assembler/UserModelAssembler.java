package com.example.toytalk.domain.users.assembler;

import com.example.toytalk.domain.users.controller.UserController;
import com.example.toytalk.domain.users.dto.UserResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserResponseDTO, EntityModel<UserResponseDTO>> {

    @Override
    public EntityModel<UserResponseDTO> toModel(UserResponseDTO user) {
        UUID userId = UUID.fromString(user.getId());

        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUser(userId)).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("fetch-all-users"),
                linkTo(methodOn(UserController.class).updateUser(userId, null)).withRel("update-user"),
                linkTo(methodOn(UserController.class).deleteUser(userId)).withRel("delete-user")
        );
    }
}
