package org.pokeherb.productservice.product.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.pokeherb.productservice.global.infrastructure.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProductErrorCode implements BaseErrorCode {

    PRODUCT_STOCK_NOT_ENOUGH(HttpStatus.CONFLICT, "409", "상품 재고가 충분하지 않습니다. {stock}"),
    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "허브를 찾을 수 없습니다."),
    VENDOR_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "생산 업체를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "상품을 찾을 수 없습니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
