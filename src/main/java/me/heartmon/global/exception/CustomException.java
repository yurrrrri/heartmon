package me.heartmon.global.exception;

import lombok.Getter;
import me.heartmon.global.resultData.ResultData;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@ResponseStatus(value = BAD_REQUEST)
public class CustomException extends RuntimeException {

    private String code;

    public CustomException(ResultData result) {
        super(result.getMessage());
        this.code = result.getCode();
    }
}
