package com.bac.exception;

public class AccountOpeningException extends BankingException{
    public AccountOpeningException(String message){
        super(message);
    }
}
