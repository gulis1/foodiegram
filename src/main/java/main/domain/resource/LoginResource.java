package main.domain.resource;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Resource;

@Resource
@Data
@AllArgsConstructor
public class LoginResource {
    private String authToken;
    private String refreshToken;
    private String error;
}
