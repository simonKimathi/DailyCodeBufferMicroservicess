package com.skytrix.OrderService.External.Decorder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skytrix.OrderService.Exceptions.CustomException;
import com.skytrix.OrderService.External.responce.ErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;


@Log4j2
public class CustomErrorDecorder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("::{}", response.request().url());
        log.info("::{}", response.request().headers());


        try {
            ErrorResponse errorResponse = objectMapper.readValue(response.body().asInputStream(), ErrorResponse.class);

            return new CustomException(errorResponse.getErrorMessage(), errorResponse.getErrorCode(), response.status());

        } catch (IOException e) {
            throw new CustomException("Internal server error", "INTERNAL_SERVER_ERROR", 500);
        }
    }
}