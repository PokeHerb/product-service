package org.pokeherb.productservice.application.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ProductDto(
        UUID productId,
        UUID vendorId,
        Long hubId,
        String name,
        int stock

) {
    public static ProductDto from(UUID vendorId, Long hubId, String name, int stock) {
        return ProductDto.builder()
                .vendorId(vendorId)
                .hubId(hubId)
                .name(name)
                .stock(stock)
                .build();
    }
}
