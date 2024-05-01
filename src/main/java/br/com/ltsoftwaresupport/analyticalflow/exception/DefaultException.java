package br.com.ltsoftwaresupport.analyticalflow.exception;

import java.io.Serial;

public class DefaultException extends Exception{
    @Serial
    private static final long serialVersionUID = 1L;

    public DefaultException(String message) {
        super(message);
    }
}
