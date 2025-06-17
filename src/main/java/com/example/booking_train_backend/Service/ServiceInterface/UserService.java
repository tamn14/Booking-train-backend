package com.example.booking_train_backend.Service.ServiceInterface;

import com.example.booking_train_backend.DTO.Request.ChangePasswordRequest;
import com.example.booking_train_backend.DTO.Request.UsersRequest;
import com.example.booking_train_backend.DTO.Request.UsersUpdateRequest;
import com.example.booking_train_backend.DTO.Response.UsersResponse;

import java.util.List;

public interface UserService {

   public UsersResponse createUser (UsersRequest request) ;
   public List<UsersResponse> getAllUsers() ;
   public UsersResponse getUserById(int id) ;
   public UsersResponse updateUser (UsersUpdateRequest usersRequest ) ;
   public UsersResponse getMyInfor() ;
   public void changePassword (ChangePasswordRequest request) ;
   public void deleteUser(int id) ;

}
