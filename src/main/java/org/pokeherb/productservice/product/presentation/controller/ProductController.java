package org.pokeherb.productservice.product.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.pokeherb.productservice.application.query.ProductQueryService;
import org.pokeherb.productservice.global.infrastructure.CustomResponse;
import org.pokeherb.productservice.global.infrastructure.success.GeneralSuccessCode;
import org.pokeherb.productservice.application.command.ProductCommandService;
import org.pokeherb.productservice.application.dto.ProductDto;
import org.pokeherb.productservice.product.presentation.dto.ProductCreateRequestDto;
import org.pokeherb.productservice.product.presentation.dto.ProductUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Product Controller", description = "제품 조회, 등록, 수정, 삭제 API")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductCommandService productCommandService;
    private final ProductQueryService productQueryService;


    @Operation(
            summary = "제품 등록 API",
            description = "신규 제품을 등록합니다."
    )
    @ApiResponse(responseCode = "201", description = "제품 등록 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @PostMapping("")
    public CustomResponse<?> createProduct(ProductCreateRequestDto dto) {

        ProductDto response = productCommandService.createProduct(dto);
        return CustomResponse.onSuccess(GeneralSuccessCode.CREATED, response);
    }

    @Operation(
            summary = "제품 수정 API",
            description = "기존 제품 정보를 수정합니다."
    )
    @ApiResponse(responseCode = "200", description = "제품 수정 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @PutMapping("/{productId}")
    public CustomResponse<?> updateProduct(
            @PathVariable("productId") UUID productId,
            ProductUpdateRequestDto dto) {

        ProductDto productDto = productCommandService.updateProduct(dto);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, productDto);
    }

    @Operation(
            summary = "제품 삭제 API",
            description = "기존 제품 정보를 삭제합니다. soft delete로 구성합니다"
    )
    @ApiResponse(responseCode = "200", description = "제품 삭제 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @DeleteMapping("/{productId}")
    public CustomResponse<?> deleteProduct(
            @PathVariable("productId") UUID productId) {

        productCommandService.deleteProduct(productId);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK);
    }

    @Operation(
            summary = "배송자 ID로 제품 단일 조회 API",
            description = "배송자 ID로 제품 한개의 정보를 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "제품 조회 성공")
    @ApiResponse(responseCode = "404", description = "잘못된 배송자 ID")
    @GetMapping("/driver/{driverId}")
    public CustomResponse<?> getProductsByDriverId(
            @PathVariable("driverId") UUID driverId) {

        ProductDto productResponse = productQueryService.getProductById(driverId);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, productResponse);
    }

    @Operation(
            summary = "배송자 ID로 제품 다중 조회 API",
            description = "배송자 ID로 제품 여러개의 정보를 조회합니다."
    )
    @GetMapping("/driver/{driverId}/all")
    public CustomResponse<?> getAllProductsByDriverId(
            @PathVariable("driverId") UUID driverId,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        Page<ProductDto> productList = productQueryService.findAllProductByDriverId(driverId, page, size);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, productList);
    }

    @Operation(
            summary = "허브 ID로 제품 다중 조회 API",
            description = "허브 ID로 제품 여러개의 정보를 조회합니다."
    )
    @GetMapping("/hub/{hubId}")
    public CustomResponse<?> getAllProductsByHubId(
            @PathVariable("hubId") Long hubId,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        Page<ProductDto> productList = productQueryService.findAllProductByHubId(hubId, page, size);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, productList);
    }
}
