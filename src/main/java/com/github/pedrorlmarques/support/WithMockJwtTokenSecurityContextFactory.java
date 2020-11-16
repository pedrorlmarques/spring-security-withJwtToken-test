/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.pedrorlmarques.support;

import com.github.pedrorlmarques.annotation.WithMockJwtToken;
import com.github.pedrorlmarques.annotation.WithMockJwtTokenClaim;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * A {@link WithMockJwtTokenSecurityContextFactory} that works with {@link WithMockJwtToken}.
 */
public class WithMockJwtTokenSecurityContextFactory implements WithSecurityContextFactory<WithMockJwtToken> {

    @Override
    public SecurityContext createSecurityContext(WithMockJwtToken withMockJwtToken) {

        SecurityContextHolder.setStrategyName(withMockJwtToken.securityStrategyName());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        //default jwt token attributes
        Jwt.Builder jwtBuilder = Jwt
                .withTokenValue(withMockJwtToken.token())
                .header("alg", "RS256")
                .header("typ", "JWT")
                .subject(withMockJwtToken.subject());

        if (withMockJwtToken.audience().length != 0) {
            jwtBuilder = jwtBuilder.audience(new HashSet<>(Arrays.asList(withMockJwtToken.audience())));
        }

        if (!withMockJwtToken.expiresAt().isEmpty()) {
            jwtBuilder.expiresAt(Instant.parse(withMockJwtToken.expiresAt()));
        }

        if (!withMockJwtToken.issuedAt().isEmpty()) {
            jwtBuilder.issuedAt(Instant.parse(withMockJwtToken.issuedAt()));
        }

        if (!withMockJwtToken.jti().isEmpty()) {
            jwtBuilder.jti(withMockJwtToken.jti());
        }

        if (withMockJwtToken.scope().length != 0) {
            jwtBuilder.claim("scope", new HashSet<>(Arrays.asList(withMockJwtToken.scope())));
        }

        List<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(withMockJwtToken);

        if (withMockJwtToken.additionalClaims().length != 0) {
            for (WithMockJwtTokenClaim withMockJwtTokenClaim : withMockJwtToken.additionalClaims()) {
                jwtBuilder.claim(withMockJwtTokenClaim.name(), withMockJwtTokenClaim.value());
            }
        }

        Jwt jwt = jwtBuilder.build();

        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(jwt, grantedAuthorities, jwt.getSubject());
        context.setAuthentication(jwtAuthenticationToken);

        return context;
    }


    private List<GrantedAuthority> getGrantedAuthorities(WithMockJwtToken withMockJwtToken) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        for (String authority : withMockJwtToken.authorities()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
        }

        if (grantedAuthorities.isEmpty()) {
            for (String role : withMockJwtToken.roles()) {
                Assert.isTrue(!role.startsWith("ROLE_"), () -> "roles cannot start with ROLE_ Got " + role);
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
        } else if (!(withMockJwtToken.roles().length == 1 && "USER".equals(withMockJwtToken.roles()[0]))) {
            throw new IllegalStateException("You cannot define roles attribute " + Arrays.asList(withMockJwtToken.roles())
                    + " with authorities attribute " + Arrays.asList(withMockJwtToken.authorities()));
        }
        return grantedAuthorities;
    }
}
