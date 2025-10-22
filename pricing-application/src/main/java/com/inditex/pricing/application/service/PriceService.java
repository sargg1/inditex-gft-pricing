package com.inditex.pricing.application.service;

import com.inditex.pricing.application.dto.PriceResponse;
import com.inditex.pricing.application.exception.PriceNotFoundException;
import com.inditex.pricing.application.usecase.GetPriceUseCase;
import com.inditex.pricing.domain.model.Price;
import com.inditex.pricing.domain.repository.PriceRepository;

import java.time.LocalDateTime;
import java.util.Objects;

public class PriceService implements GetPriceUseCase {

    private final PriceRepository priceRepository;
    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = Objects.requireNonNull(priceRepository, "priceRepository must not be null");
    }

    @Override
    public PriceResponse getPrice(LocalDateTime applicationDate, Long productId, Long brandId) {
        requireNonNull(applicationDate, "applicationDate");
        requirePositive(productId, "productId");
        requirePositive(brandId, "brandId");

        Price price = priceRepository
                .findApplicablePrice(applicationDate, productId, brandId)
                .orElseThrow(() -> new PriceNotFoundException(productId, brandId));

        return new PriceResponse(
                price.productId(),
                price.brandId(),
                price.priceList(),
                price.startDate(),
                price.endDate(),
                price.amount(),
                price.currency()
        );
    }

    private static void requireNonNull(Object value, String name) {
        if (value == null) throw new IllegalArgumentException(name + " must not be null");
    }

    private static void requirePositive(Long value, String name) {
        requireNonNull(value, name);
        if (value <= 0) throw new IllegalArgumentException(name + " must be greater than 0");
    }
}
