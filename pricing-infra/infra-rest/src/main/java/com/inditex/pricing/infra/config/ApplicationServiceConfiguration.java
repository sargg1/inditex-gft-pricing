package com.inditex.pricing.infra.config;

import com.inditex.pricing.application.service.PriceService;
import com.inditex.pricing.application.usecase.GetPriceUseCase;
import com.inditex.pricing.domain.repository.PriceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationServiceConfiguration {

    @Bean
    public GetPriceUseCase getPriceQuery(PriceRepository priceRepository) {
        return new PriceService(priceRepository);
    }

}