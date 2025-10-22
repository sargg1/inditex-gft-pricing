package com.inditex.pricing.application.usecase;

import com.inditex.pricing.application.dto.PriceResponse;

import java.time.LocalDateTime;

public interface GetPriceUseCase {

    PriceResponse getPrice(LocalDateTime applicationDate, Long productId, Long brandId);
}
