package org.pokeherb.productservice.product.domain;

import org.pokeherb.productservice.application.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductDetailsRepository {

    ProductDto getProductById(UUID productId);

    Page<ProductDto> findAllProductByHubId(Long hubId, Pageable pageable);

    Page<ProductDto> findAllProductByDriverId(UUID driverId, Pageable pageable);
}
