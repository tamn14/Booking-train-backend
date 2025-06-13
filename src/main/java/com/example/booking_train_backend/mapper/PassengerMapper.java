package com.example.booking_train_backend.mapper;

import com.example.booking_train_backend.DTO.Request.UsersRequest;
import com.example.booking_train_backend.DTO.Response.UsersResponse;
import com.example.booking_train_backend.Entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PassengerMapper {
    // mapping from request to entity
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "userName", target = "userName")
    Users toEntity (UsersRequest request) ;


    // mapping from entity to DTO
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    UsersResponse toDTO (Users users) ;
}
