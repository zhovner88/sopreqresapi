package com.reqres.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class RegisterResponse {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("token")
    private String token;
}