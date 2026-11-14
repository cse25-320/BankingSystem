package com.bac.exception;

public class WithdrawalNotAllowedException extends BankingException{
    public WithdrawalNotAllowedException(String message){
        super(message);
    }
}
