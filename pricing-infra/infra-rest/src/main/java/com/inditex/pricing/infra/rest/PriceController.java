package com.inditex.pricing.infra.rest;

import com.inditex.pricing.application.dto.PriceResponse;
import com.inditex.pricing.application.usecase.GetPriceUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/prices")
@Tag(name = "Prices", description = "Returns the applicable prices")
@Validated
public class PriceController {
    private final GetPriceUseCase getPriceUseCase;

    public PriceController(GetPriceUseCase getPriceUseCase) {
        this.getPriceUseCase = getPriceUseCase;
    }

    @GetMapping
    @Operation(
            summary = "Get applicable price",
            description = "Returns the price that applies based on the product and brand requested according to the date."
    )
    public PriceResponse getPrice(
            @Parameter(
                    description = "Date and time when the price should be applied",
                    example = "2020-06-14T16:00:00"
            )
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @NotNull(message = "The applicationDate parameter is required")
            @RequestParam("applicationDate")
            LocalDateTime applicationDate,

            @Positive(message = "The productId parameter must be greater than 0")
            @Parameter(description = "Unique identifier of the product", example = "35455")
            @NotNull(message = "The productId parameter is required")
            @RequestParam("productId") Long productId,

            @Positive(message = "The brandId parameter must be greater than 0")
            @Parameter(description = "Unique identifier of the brand", example = "1")
            @NotNull(message = "The brandId parameter is required")
            @RequestParam("brandId") Long brandId) {
        return getPriceUseCase.getPrice(applicationDate, productId, brandId);
    }
}