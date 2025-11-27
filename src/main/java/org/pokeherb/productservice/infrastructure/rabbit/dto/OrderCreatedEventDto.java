package org.pokeherb.productservice.infrastructure.rabbit.dto;

import java.util.UUID;

public record OrderCreatedEventDto(
        UUID productId,
        int quantity
) {
}
