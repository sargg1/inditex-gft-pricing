package com.inditex.pricing.infra.jpa;

import com.inditex.pricing.domain.model.Price;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PriceEntityMapper {

    @Mapping(target = "amount", source = "price")
    Price toDomain(PriceEntity entity);
}
