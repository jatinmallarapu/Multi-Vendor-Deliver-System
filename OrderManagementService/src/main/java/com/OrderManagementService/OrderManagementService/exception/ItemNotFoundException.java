package com.OrderManagementService.OrderManagementService.exception;

public class ItemNotFoundException extends RuntimeException{

    public ItemNotFoundException(String message) {
        super(message);
    }

}
