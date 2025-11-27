package org.pokeherb.productservice.infrastructure.rabbit.dto;

import java.util.UUID;

public record OrderCancelledEventDto(
        UUID productId,
        int quantity
) {
}
