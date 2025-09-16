package com.reqres.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class RegisterRequest {

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;
}