package me.heartmon.global.resultData;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResultData<T> {

    private String code;
    private String message;

    @Nullable
    private T data;

    public static <T> ResultData<T> of(String code, String message, T data) {
        return new ResultData<>(code, message, data);
    }

    public static <T> ResultData<T> of(String code, String message) {
        return new ResultData<>(code, message, null);
    }

    public boolean hasData() {
        return data != null;
    }

    public boolean isSuccess() {
        return code.startsWith("S");
    }

    public boolean isFail() {
        return !isSuccess();
    }
}
