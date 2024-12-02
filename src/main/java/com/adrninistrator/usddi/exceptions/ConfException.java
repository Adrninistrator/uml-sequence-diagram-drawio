package com.adrninistrator.usddi.exceptions;

/**
 * @author adrninistrator
 * @date 2024/10/21
 * @description:
 */
public class ConfException extends Exception {

    public ConfException(String message) {
        super(message);
        System.err.println(message);
    }
}
