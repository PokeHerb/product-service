package org.pokeherb.productservice.application.service;

import lombok.RequiredArgsConstructor;
import org.pokeherb.productservice.global.infrastructure.exception.CustomException;
import org.pokeherb.productservice.product.domain.ProductRepository;
import org.pokeherb.productservice.product.domain.entity.Product;
import org.pokeherb.productservice.product.domain.exception.ProductErrorCode;
import org.pokeherb.productservice.product.domain.exception.ProductStockNotEnoughException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductStockService {


    private final ProductRepository productRepository;

    @Transactional
    public void decreaseStock(UUID productId, int quantity) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));

        int curStock = product.getStock();

        if (curStock < quantity) {
            throw new ProductStockNotEnoughException(curStock);
        }

        product.changeStock(curStock - quantity);
    }

    @Transactional
    public void increaseStock(UUID productId, int quantity) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));

        int curStock = product.getStock();

        product.changeStock(curStock + quantity);
    }
}
