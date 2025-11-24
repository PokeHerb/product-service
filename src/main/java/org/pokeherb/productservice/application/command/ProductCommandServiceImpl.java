package org.pokeherb.productservice.application.command;

import lombok.RequiredArgsConstructor;
import org.pokeherb.productservice.global.infrastructure.client.VendorServiceClient;
import org.pokeherb.productservice.global.infrastructure.exception.CustomException;
import org.pokeherb.productservice.product.domain.ProductRepository;
import org.pokeherb.productservice.product.domain.VendorHubIdDto;
import org.pokeherb.productservice.application.dto.ProductDto;
import org.pokeherb.productservice.product.domain.entity.Product;
import org.pokeherb.productservice.product.domain.exception.ProductErrorCode;
import org.pokeherb.productservice.product.presentation.dto.ProductCreateRequestDto;
import org.pokeherb.productservice.product.presentation.dto.ProductUpdateRequestDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductCommandServiceImpl implements ProductCommandService {

    private final ProductRepository productRepository;
    private final VendorServiceClient vendorServiceClient;


    // 상품 생성
    @Override
    public ProductDto createProduct(ProductCreateRequestDto dto) {

        Product newProduct = Product.builder()
                .name(dto.name())
                .stock(dto.stock())
                .build();

        VendorHubIdDto vendorHubIdDto = newProduct.setVendorIdHubId(dto.vendorId(), vendorServiceClient);

        productRepository.save(newProduct);

        return ProductDto.from(
                vendorHubIdDto.vendorId(),
                vendorHubIdDto.hubId(),
                newProduct.getName(),
                newProduct.getStock()
        );
    }

    // 상품 수정
    @Override
    public ProductDto updateProduct(ProductUpdateRequestDto dto) {

        Product product = productRepository.findById(dto.productId()).orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));

        product.changeInfo(dto);

        return ProductDto.from(
                product.getVendorId(),
                product.getHubId(),
                product.getName(),
                product.getStock()
        );
    }

    // 상품 삭제 (soft)
    @Override
    public void deleteProduct(UUID productId) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));


        // 추후에 Security 설정 후 수정
        product.delete(null);


    }
}
