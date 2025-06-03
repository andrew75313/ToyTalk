package com.example.toytalk.domain.users.controller;

import com.example.toytalk.domain.users.dto.SignupRequestDTO;
import com.example.toytalk.domain.users.dto.UserResponseDTO;
import com.example.toytalk.domain.users.dto.UserUpdateRequestDTO;
import com.example.toytalk.domain.users.service.UserService;
import com.example.toytalk.global.security.oauth.OAuthProperties;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final OAuthProperties oauthProperties;

    @GetMapping("/login")
    public String loginPage(Model model) {
        OAuthProperties.Provider kakao = oauthProperties.getProviders().get("kakao");
        model.addAttribute("clientId", kakao.getClientId());
        model.addAttribute("redirectUri", kakao.getRedirectUri());

        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid SignupRequestDTO requestDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "signup";
        }

        try {
            userService.createUser(requestDTO);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "signup";
        }

        redirectAttributes.addFlashAttribute("successMessage", "회원가입이 완료되었습니다. 로그인해주세요.");
        return "redirect:/api/users/login";
    }

    // ADDITIONAL FEATURES
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable UUID userId) {
        UserResponseDTO responseDTO = userService.getUser(userId);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> responseDTOList = userService.getAllUsers();
        return new ResponseEntity<>(responseDTOList, HttpStatus.OK);
    }

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
