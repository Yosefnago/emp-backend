package com.ms.sw.exception.auth;


public class UserAlreadyExistsException extends  RuntimeException{

    public UserAlreadyExistsException(String message){
        super(message);
    }

}
