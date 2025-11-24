package org.pokeherb.productservice.application.command;

import org.pokeherb.productservice.application.dto.ProductDto;
import org.pokeherb.productservice.product.presentation.dto.ProductCreateRequestDto;
import org.pokeherb.productservice.product.presentation.dto.ProductUpdateRequestDto;

import java.util.UUID;

public interface ProductCommandService {

    ProductDto createProduct(ProductCreateRequestDto dto);

    ProductDto updateProduct(ProductUpdateRequestDto dto);

    void deleteProduct(UUID productId);
}
