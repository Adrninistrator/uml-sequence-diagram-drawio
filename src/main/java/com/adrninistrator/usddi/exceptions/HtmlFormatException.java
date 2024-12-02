package com.adrninistrator.usddi.exceptions;

/**
 * @author adrninistrator
 * @date 2024/10/2
 * @description:
 */
public class HtmlFormatException extends Exception {

    public HtmlFormatException(String message) {
        super(message);
        System.err.println(message);
    }
}
