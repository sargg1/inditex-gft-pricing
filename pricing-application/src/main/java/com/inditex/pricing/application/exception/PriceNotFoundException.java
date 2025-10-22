package com.inditex.pricing.application.exception;

public class PriceNotFoundException extends RuntimeException {

    public PriceNotFoundException(Long productId, Long brandId) {
        super(String.format("Price not found for product %d and brand %d", productId, brandId));
    }
}
