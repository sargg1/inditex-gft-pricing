package com.inditex.pricing.infra.jpa;

import com.inditex.pricing.domain.model.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class JpaPriceRepositoryAdapterTest {

    private static final LocalDateTime APPLICATION_DATE = LocalDateTime.of(2020, 6, 14, 10, 0);
    private static final long PRODUCT_ID = 35455L;
    private static final long BRAND_ID = 1L;

    @Mock
    private SpringDataPriceRepository springDataRepository;
    @Mock
    private PriceEntityMapper mapper;

    private JpaPriceRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new JpaPriceRepositoryAdapter(springDataRepository, mapper);
    }

    @Test
    void shouldDelegateToSpringDataRepositoryAndMapResult() {
        PriceEntity entity = new PriceEntity();
        TestEntityAccessor.populate(entity, 99L, BRAND_ID, PRODUCT_ID, APPLICATION_DATE.minusHours(1), APPLICATION_DATE.plusHours(1), 1L, 0, new BigDecimal("35.50"), "EUR");
        Price mappedPrice = new Price(99L, BRAND_ID, PRODUCT_ID, 1L, APPLICATION_DATE.minusHours(1), APPLICATION_DATE.plusHours(1), 0, new BigDecimal("35.50"), "EUR");

        when(springDataRepository.findApplicablePrice(BRAND_ID, PRODUCT_ID, APPLICATION_DATE)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(mappedPrice);

        Optional<Price> result = adapter.findApplicablePrice(APPLICATION_DATE, PRODUCT_ID, BRAND_ID);

        assertThat(result).contains(mappedPrice);
        verify(springDataRepository).findApplicablePrice(BRAND_ID, PRODUCT_ID, APPLICATION_DATE);
        verify(mapper).toDomain(entity);
    }

    @Test
    void shouldReturnEmptyWhenRepositoryDoesNotFindPrice() {
        when(springDataRepository.findApplicablePrice(eq(BRAND_ID), eq(PRODUCT_ID), any(LocalDateTime.class))).thenReturn(Optional.empty());

        Optional<Price> result = adapter.findApplicablePrice(APPLICATION_DATE, PRODUCT_ID, BRAND_ID);

        assertThat(result).isEmpty();
        verify(springDataRepository).findApplicablePrice(BRAND_ID, PRODUCT_ID, APPLICATION_DATE);
        verify(mapper, never()).toDomain(any(PriceEntity.class));
    }
}
