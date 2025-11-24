package org.pokeherb.productservice.product.presentation;

import lombok.RequiredArgsConstructor;
import org.pokeherb.productservice.global.infrastructure.CustomResponse;
import org.pokeherb.productservice.global.infrastructure.success.GeneralSuccessCode;
import org.pokeherb.productservice.product.domain.application.command.ProductCommandService;
import org.pokeherb.productservice.product.domain.application.dto.ProductDto;
import org.pokeherb.productservice.product.presentation.dto.ProductCreateRequestDto;
import org.pokeherb.productservice.product.presentation.dto.ProductUpdateRequestDto;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/product")
public class ProductController {

    private final ProductCommandService productCommandService;

    @PostMapping("")
    public CustomResponse<?> createProduct(ProductCreateRequestDto dto) {

        ProductDto response = productCommandService.createProduct(dto);
        return CustomResponse.onSuccess(GeneralSuccessCode.CREATED, response);
    }

    @PutMapping("/{productId}")
    public CustomResponse<?> updateProduct(
            @PathVariable("productId") UUID productId,
            ProductUpdateRequestDto dto) {

        ProductDto productDto = productCommandService.updateProduct(dto);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, productDto);
    }

    @DeleteMapping("/{productId}")
    public CustomResponse<?> deleteProduct(
            @PathVariable("productId") UUID productId) {

        productCommandService.deleteProduct(productId);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK);
    }

}
