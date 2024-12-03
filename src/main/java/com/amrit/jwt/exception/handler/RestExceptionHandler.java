package com.amrit.jwt.exception.handler;

import com.amrit.jwt.dto.ApiResponse;
import com.amrit.jwt.exception.NotFoundException;
import com.amrit.jwt.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    protected ApiResponse<Object> handleNotFoundException(NotFoundException ex) {
        log.error("------- RESOURCE NOT FOUND EXCEPTION -------");
        return ResponseUtil.getNotFoundResponse(ex.getMessage());
    }
}
