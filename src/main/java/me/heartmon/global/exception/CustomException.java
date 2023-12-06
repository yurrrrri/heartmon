package me.heartmon.global.exception;

import lombok.Getter;
import me.heartmon.global.code.ErrorCode;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@ResponseStatus(value = BAD_REQUEST)
public class CustomException extends RuntimeException {

    private String code;

    public CustomException(ErrorCode code) {
        super(code.getMessage());
        this.code = code.getCode();
    }
}
