package org.pokeherb.itemservice.global.infrastructure.error;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {

    HttpStatus getStatus();
    String getCode();
    String getMessage();

}
