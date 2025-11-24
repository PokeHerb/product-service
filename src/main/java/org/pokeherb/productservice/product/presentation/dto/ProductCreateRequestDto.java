package org.pokeherb.productservice.product.presentation.dto;

import java.util.UUID;

public record ProductCreateRequestDto(
        UUID vendorId,
        String name,
        int stock
) {
}
