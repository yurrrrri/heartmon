package me.heartmon.domain.instaMember.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstaMemberConnectDto {

    @NotBlank
    @Size(min = 4, max = 30)
    private String username;
}
