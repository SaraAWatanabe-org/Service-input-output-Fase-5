package com.challenge.fastfood.service_io.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPasswordRequestDto {

    private String session;
    private String username;
    private String newPassword;

}
