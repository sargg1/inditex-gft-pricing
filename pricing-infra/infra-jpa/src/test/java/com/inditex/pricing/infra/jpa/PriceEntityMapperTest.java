package com.inditex.pricing.infra.jpa;

import com.inditex.pricing.domain.model.Price;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PriceEntityMapperTest {

    private final PriceEntityMapper mapper = Mappers.getMapper(PriceEntityMapper.class);

    @Test
    void shouldMapEntityToDomain() {
        PriceEntity entity = buildEntity();

        Price price = mapper.toDomain(entity);

        assertThat(price.id()).isEqualTo(entity.getId());
        assertThat(price.brandId()).isEqualTo(entity.getBrandId());
        assertThat(price.productId()).isEqualTo(entity.getProductId());
        assertThat(price.amount()).isEqualTo(entity.getPrice());
        assertThat(price.currency()).isEqualTo(entity.getCurrency());
        assertThat(price.priceList()).isEqualTo(entity.getPriceList());
        assertThat(price.priority()).isEqualTo(entity.getPriority());
        assertThat(price.startDate()).isEqualTo(entity.getStartDate());
        assertThat(price.endDate()).isEqualTo(entity.getEndDate());
    }

    private PriceEntity buildEntity() {
        PriceEntity entity = new PriceEntity();
        TestEntityAccessor.populate(entity,
                42L,
                1L,
                2L,
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(1),
                3L,
                4,
                new BigDecimal("19.99"),
                "EUR");
        return entity;
    }
}
