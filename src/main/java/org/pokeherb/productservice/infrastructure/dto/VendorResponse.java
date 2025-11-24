package org.pokeherb.productservice.infrastructure.dto;

import java.util.UUID;

public record VendorResponse(

        UUID vendorId,
        Long hubId,
        String vendorName,
        String vendorDescription
) {
}
