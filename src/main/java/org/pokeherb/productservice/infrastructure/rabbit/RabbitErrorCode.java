package org.pokeherb.productservice.infrastructure.rabbit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.pokeherb.productservice.global.infrastructure.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RabbitErrorCode implements BaseErrorCode {

    RABBITMQ_CONNECTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "RABBIT_JSON_500", "RabbitMQ 메시지의 JSON 변환에 실패했습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
