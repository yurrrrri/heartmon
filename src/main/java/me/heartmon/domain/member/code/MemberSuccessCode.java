package me.heartmon.domain.member.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.heartmon.global.code.SuccessCode;

@Getter
@AllArgsConstructor
public enum MemberSuccessCode implements SuccessCode {

    SIGNUP_SUCCESS("S-M1", "회원가입 되었습니다.");

    private String code;
    private String message;
}
