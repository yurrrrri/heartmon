package me.heartmon.global.resultData;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.heartmon.global.code.Code;

@Getter
@AllArgsConstructor
public class ResultData {

    private String code;
    private String message;

    @Nullable
    private Object data;

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
