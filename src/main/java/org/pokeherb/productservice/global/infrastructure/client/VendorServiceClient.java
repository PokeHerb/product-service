package org.pokeherb.productservice.global.infrastructure.client;

import org.pokeherb.productservice.infrastructure.dto.VendorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient("vendor-service")
public interface VendorServiceClient {

    @GetMapping("/v1/vendor/{vendorId}/exists")
    boolean existsVendor(@PathVariable("vendorId") UUID vendorId);

    @GetMapping("v1/vendor/{vendorId}")
    VendorResponse getVendor(@PathVariable("vendorId") UUID vendorId);
}
