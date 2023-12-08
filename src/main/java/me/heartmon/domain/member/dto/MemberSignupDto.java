package me.heartmon.domain.member.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberSignupDto {

    @Size(min = 4, max = 30)
    private String username;

    @Pattern(regexp = "^[A-Za-z0-9!@#$%^&*()_+{}\\[\\]:;,.?~\\-]{6,30}$")
    private String password;
}
