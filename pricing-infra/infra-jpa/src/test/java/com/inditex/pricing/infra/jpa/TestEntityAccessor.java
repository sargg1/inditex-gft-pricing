package com.inditex.pricing.infra.jpa;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;

final class TestEntityAccessor {

    private TestEntityAccessor() {
    }

    static void populate(PriceEntity entity,
                         Long id,
                         Long brandId,
                         Long productId,
                         LocalDateTime startDate,
                         LocalDateTime endDate,
                         Long priceList,
                         Integer priority,
                         BigDecimal price,
                         String currency) {
        setField(entity, "id", id);
        setField(entity, "brandId", brandId);
        setField(entity, "productId", productId);
        setField(entity, "startDate", startDate);
        setField(entity, "endDate", endDate);
        setField(entity, "priceList", priceList);
        setField(entity, "priority", priority);
        setField(entity, "price", price);
        setField(entity, "currency", currency);
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("Unable to set field '%s'".formatted(fieldName), ex);
        }
    }
}
