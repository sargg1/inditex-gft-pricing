package com.inditex.pricing.infra.jpa;

import com.inditex.pricing.domain.model.Price;
import com.inditex.pricing.domain.repository.PriceRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class JpaPriceRepositoryAdapter implements PriceRepository {

    private final SpringDataPriceRepository priceRepository;
    private final PriceEntityMapper mapper;

    public JpaPriceRepositoryAdapter(SpringDataPriceRepository priceRepository, PriceEntityMapper mapper) {
        this.priceRepository = priceRepository;
        this.mapper = mapper;
    }


    @Override
    public Optional<Price> findApplicablePrice(LocalDateTime applicationDate, Long productId, Long brandId) {
        return priceRepository
                .findApplicablePrice(brandId, productId, applicationDate)
                .map(mapper::toDomain);
    }

}
