package me.heartmon.domain.member.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.heartmon.global.code.ErrorCode;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    USERNAME_ALREADY_EXISTS("F-M1", "이미 존재하는 회원 아이디입니다."),
    MEMBER_NOT_EXISTS("F-M2", "존재하지 않는 회원입니다.");

    private String code;
    private String message;
}
