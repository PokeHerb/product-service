package org.pokeherb.productservice.application.query;

import org.pokeherb.productservice.application.dto.ProductDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ProductQueryService {

    ProductDto getProductById(UUID productId);

    Page<ProductDto> findAllProductByHubId(Long hubId, int page, int size);

    Page<ProductDto> findAllProductByDriverId(UUID driverId, int page, int size);
}
