package org.pokeherb.productservice.infrastructure.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record VendorResponse(

        UUID vendorId,
        Long hubId,
        String name,
        String description,
        String tel,
        String vendorType,
        String street,
        String details,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) implements Serializable {
}
