package org.pokeherb.productservice.infrastructure.dto;

public record HubResponse(
        Long hubId,
        String hubName,
        String hubAddress,
        Double latitude,
        Double longitude
) {}
