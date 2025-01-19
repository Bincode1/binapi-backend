package com.bin.binapibackend.model.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String useraccount;
    private String userpassword;
}
