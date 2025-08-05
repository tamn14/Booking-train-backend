package com.example.booking_train_backend.Controller;

import com.example.booking_train_backend.DTO.Request.ChangePasswordRequest;
import com.example.booking_train_backend.DTO.Request.UsersRequest;
import com.example.booking_train_backend.DTO.Request.UsersUpdateRequest;
import com.example.booking_train_backend.DTO.Response.ApiResponse;
import com.example.booking_train_backend.DTO.Response.UsersResponse;
import com.example.booking_train_backend.Service.ServiceInterface.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class UserController {
    UserService userService  ;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ApiResponse<UsersResponse> createUser (@RequestBody @Valid UsersRequest usersRequest) {
        UsersResponse usersResponse = userService.createUser(usersRequest) ;
        return ApiResponse.<UsersResponse>builder()
                .code(1000)
                .result(usersResponse)
                .mess("Success")
                .build() ;
    }

    @GetMapping()
    public ApiResponse<List<UsersResponse>> getAllUser () {
        List<UsersResponse> usersResponse = userService.getAllUsers() ;
        return ApiResponse.<List<UsersResponse> >builder()
                .code(1000)
                .result(usersResponse)
                .mess("Success")
                .build() ;
    }

    @GetMapping("/{id}")
    public ApiResponse<UsersResponse> getUserById (@PathVariable("id")  int id) {
        UsersResponse usersResponse = userService.getUserById(id) ;
        return ApiResponse.<UsersResponse>builder()
                .code(1000)
                .result(usersResponse)
                .mess("Success")
                .build() ;
    }

    @PutMapping("/update")
    public ApiResponse<UsersResponse> updateUser (@RequestBody @Valid UsersUpdateRequest usersUpdateRequest) {
        UsersResponse usersResponse = userService.updateUser(usersUpdateRequest) ;
        return ApiResponse.<UsersResponse>builder()
                .code(1000)
                .result(usersResponse)
                .mess("Success")
                .build() ;
    }

    @GetMapping("/myInfor")
    public ApiResponse<UsersResponse> getMyInfor () {
        UsersResponse usersResponse = userService.getMyInfor() ;
        return ApiResponse.<UsersResponse>builder()
                .code(1000)
                .result(usersResponse)
                .mess("Success")
                .build() ;
    }

    @PostMapping("/changePass")
    public ApiResponse<Void> changePassword (@RequestBody @Valid  ChangePasswordRequest request) {
        userService.changePassword(request);
        return ApiResponse.<Void>builder()
                .code(1000)
                .mess("Success")
                .build() ;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return ApiResponse.<Void>builder()
                .code(1000)
                .mess("Success")
                .build() ;
    }




}
