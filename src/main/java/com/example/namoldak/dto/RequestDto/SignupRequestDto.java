package com.example.namoldak.dto.RequestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class SignupRequestDto {
    @NotBlank(message = "닉네임은 필수 입력 값입니다")
    private String nickname;

    @Pattern(regexp="^\\w+@\\w+\\.\\w+(\\.\\w+)?$",
            message = "올바른 형식의 이메일 주소여야 합니다")
    @NotBlank(message = "이메일은 필수 입력 값입니다")
    private String email;

//    @Pattern(regexp="^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,15}",
//            message = "비밀번호는 영문, 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자여야 합니다 합니다.")
    @NotBlank(message = "비밀번호는 필수 입력 값입니다")
    private String password;
}
