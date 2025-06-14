package org.example.security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class SecurityProperties {

    @Value("${jwt.refresh.max.count}")
    private Integer maxRefreshTokensCount;

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    @Value("${jwt.access.expiration}")
    private Long accessExpiration;

    @Value("${jwt.secret}")
    private String secret;
}
