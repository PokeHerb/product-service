package org.pokeherb.productservice.global.infrastructure.client;

import org.pokeherb.productservice.infrastructure.dto.HubResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("hub-service")
public interface HubServiceClient {

    @GetMapping("/{hubId}/exists")
    boolean existsHub(@PathVariable("hubId") Long hubId);

    @GetMapping("/{hubId}")
    HubResponse getHub(@PathVariable("hubId") Long hubId);
}

