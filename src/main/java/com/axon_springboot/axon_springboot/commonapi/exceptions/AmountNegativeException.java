package com.axon_springboot.axon_springboot.commonapi.exceptions;

public class AmountNegativeException  extends RuntimeException {
    public AmountNegativeException(String message) {
        super(message);
    }
}
