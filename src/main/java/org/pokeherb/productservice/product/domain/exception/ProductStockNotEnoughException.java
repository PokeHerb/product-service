package org.pokeherb.productservice.product.domain.exception;

import lombok.Getter;

@Getter
public class ProductStockNotEnoughException extends RuntimeException {

    private final ProductErrorCode productErrorCode;
    private final int stock;

    public ProductStockNotEnoughException(int stock) {
        super(ProductErrorCode.PRODUCT_STOCK_NOT_ENOUGH.getMessage().replace("{stock}", String.valueOf(stock)));
        this.productErrorCode = ProductErrorCode.PRODUCT_STOCK_NOT_ENOUGH;
        this.stock = stock;
    }
}
