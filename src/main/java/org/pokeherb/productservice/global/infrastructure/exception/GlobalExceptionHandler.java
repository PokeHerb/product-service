package org.pokeherb.productservice.global.infrastructure.exception;

import lombok.extern.slf4j.Slf4j;
import org.pokeherb.productservice.global.infrastructure.error.BaseErrorCode;
import org.pokeherb.productservice.global.infrastructure.CustomResponse;
import org.pokeherb.productservice.global.infrastructure.error.GeneralErrorCode;
import org.pokeherb.productservice.product.domain.exception.ProductStockNotEnoughException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 커스텀 예외 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomResponse<?>> handle(CustomException e) {
        BaseErrorCode code = e.getCode();  // 에러코드 가져오기
        CustomResponse<?> response = CustomResponse.onFail(code);  // 실패 응답 생성

        return new ResponseEntity<>(response, code.getStatus());
    }

    // 일반 예외 처리 (NullPointerException, IllegalArgumentException 등)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse<?>> handle(Exception e) {
        log.error("Exception: {}", e.getMessage());
        log.error("에러: {}", e.toString());

        BaseErrorCode code = GeneralErrorCode.INTERNAL_SERVER_ERROR_500;

        CustomResponse<?> response = CustomResponse.onFail(code);

        return new ResponseEntity<>(response, code.getStatus());
    }

    // 요청 검증 실패 (DTO + @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponse<?>> handle(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        CustomResponse<?> response = CustomResponse.builder()
                .isSuccess(false)
                .status(HttpStatus.BAD_REQUEST)
                .code("VALIDATION_ERROR - 요청 데이터 검증 실패")
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 상품 재고 부족으로 인한 실패
    @ExceptionHandler(ProductStockNotEnoughException.class)
    public ResponseEntity<CustomResponse<?>> handle(ProductStockNotEnoughException e) {

        CustomResponse<?> response = CustomResponse.builder()
                .isSuccess(false)
                .status(e.getProductErrorCode().getStatus())
                .code(e.getProductErrorCode().getCode())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}


