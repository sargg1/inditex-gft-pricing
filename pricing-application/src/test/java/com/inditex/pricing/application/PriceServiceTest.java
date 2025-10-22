package com.inditex.pricing.application;

import com.inditex.pricing.application.dto.PriceResponse;
import com.inditex.pricing.application.exception.PriceNotFoundException;
import com.inditex.pricing.application.service.PriceService;
import com.inditex.pricing.domain.model.Price;
import com.inditex.pricing.domain.repository.PriceRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PriceServiceTest {

    private final LocalDateTime applicationDate = LocalDateTime.parse("2020-06-14T10:00:00");

    @Test
    void shouldFailWhenApplicationDateIsNull() {
        PriceService service = new PriceService(alwaysEmptyRepository());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.getPrice(null, 35455L, 1L));

        assertEquals("applicationDate must not be null", exception.getMessage());
    }

    @Test
    void shouldFailWhenProductIdIsNull() {
        PriceService service = new PriceService(alwaysEmptyRepository());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.getPrice(applicationDate, null, 1L));

        assertEquals("productId must not be null", exception.getMessage());
    }

    @Test
    void shouldFailWhenBrandIdIsNull() {
        PriceService service = new PriceService(alwaysEmptyRepository());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.getPrice(applicationDate, 35455L, null));

        assertEquals("brandId must not be null", exception.getMessage());
    }

    @Test
    void shouldFailWhenProductIdIsNotPositive() {
        PriceService service = new PriceService(alwaysEmptyRepository());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.getPrice(applicationDate, 0L, 1L));

        assertEquals("productId must be greater than 0", exception.getMessage());
    }

    @Test
    void shouldFailWhenBrandIdIsNotPositive() {
        PriceService service = new PriceService(alwaysEmptyRepository());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.getPrice(applicationDate, 35455L, -1L));

        assertEquals("brandId must be greater than 0", exception.getMessage());
    }

    @Test
    void shouldThrowNotFoundWhenRepositoryReturnsEmpty() {
        PriceService service = new PriceService(alwaysEmptyRepository());

        assertThrows(PriceNotFoundException.class,
                () -> service.getPrice(applicationDate, 35455L, 1L));
    }

    @Test
    void shouldReturnResponseWhenPriceExists() {
        PriceRepository repository = (applicationDate, productId, brandId) -> Optional.of(new Price(
                1L,
                brandId,
                productId,
                1L,
                applicationDate.minusHours(1),
                applicationDate.plusHours(1),
                0,
                BigDecimal.TEN,
                "EUR"
        ));
        PriceService service = new PriceService(repository);

        PriceResponse response = service.getPrice(applicationDate, 35455L, 1L);

        assertEquals(35455L, response.productId());
        assertEquals(1L, response.brandId());
        assertEquals(1L, response.priceList());
        assertEquals(BigDecimal.TEN, response.price());
        assertEquals("EUR", response.currency());
    }

    private PriceRepository alwaysEmptyRepository() {
        return (applicationDate, productId, brandId) -> Optional.empty();
    }
}