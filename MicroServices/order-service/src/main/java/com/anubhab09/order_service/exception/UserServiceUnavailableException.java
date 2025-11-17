package com.anubhab09.order_service.exception;

public class UserServiceUnavailableException extends RuntimeException{
    public UserServiceUnavailableException(String msg){
        super(msg);
    }
}
