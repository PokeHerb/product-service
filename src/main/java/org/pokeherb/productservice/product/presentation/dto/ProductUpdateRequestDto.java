package org.pokeherb.productservice.product.presentation.dto;

import java.util.UUID;

public record ProductUpdateRequestDto(
        UUID productId,
        UUID vendorId,
        String name,
        int stock
) {
}

