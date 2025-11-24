package org.pokeherb.productservice.product.domain;

import java.util.UUID;

public record VendorHubIdDto(
        UUID vendorId,
        Long hubId

) {
}
