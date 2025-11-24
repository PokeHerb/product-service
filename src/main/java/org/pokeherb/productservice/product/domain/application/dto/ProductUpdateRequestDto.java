package org.pokeherb.productservice.product.domain.application.dto;

public record ProductUpdateRequestDto(
        Long hubId,
        String name,
        int stock
) {
}
