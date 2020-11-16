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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class WithMockJwtTokenSecurityContextFactoryTest {

    @Mock
    WithMockJwtToken withMockJwtToken;

    @Mock
    WithMockJwtTokenClaim withMockJwtTokenClaim;

    private WithMockJwtTokenSecurityContextFactory factory;

    @BeforeEach
    public void setup() {
        this.factory = new WithMockJwtTokenSecurityContextFactory();
    }

    @Test
    public void audienceWorks() {
        given(this.withMockJwtToken.token()).willReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        given(this.withMockJwtToken.roles()).willReturn(new String[]{});
        given(this.withMockJwtToken.audience()).willReturn(new String[]{"account://default"});
        given(this.withMockJwtToken.expiresAt()).willReturn("");
        given(this.withMockJwtToken.issuedAt()).willReturn("");
        given(this.withMockJwtToken.jti()).willReturn("");
        given(this.withMockJwtToken.scope()).willReturn(new String[]{});
        given(this.withMockJwtToken.additionalClaims()).willReturn(new WithMockJwtTokenClaim[]{});
        given(this.withMockJwtToken.authorities()).willReturn(new String[]{});
        assertThat((Set<String>) ((JwtAuthenticationToken) this.factory.createSecurityContext(this.withMockJwtToken).getAuthentication()).getTokenAttributes().get("aud"))
                .containsOnly("account://default");
    }

    @Test
    public void expiresAtWorks() {
        given(this.withMockJwtToken.token()).willReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        given(this.withMockJwtToken.roles()).willReturn(new String[]{});
        given(this.withMockJwtToken.audience()).willReturn(new String[]{});

        String now = Instant.now().toString();
        given(this.withMockJwtToken.expiresAt()).willReturn(now);
        given(this.withMockJwtToken.issuedAt()).willReturn("");
        given(this.withMockJwtToken.jti()).willReturn("");
        given(this.withMockJwtToken.scope()).willReturn(new String[]{});
        given(this.withMockJwtToken.additionalClaims()).willReturn(new WithMockJwtTokenClaim[]{});
        given(this.withMockJwtToken.authorities()).willReturn(new String[]{});
        assertThat((Instant) ((JwtAuthenticationToken) this.factory.createSecurityContext(this.withMockJwtToken).getAuthentication()).getTokenAttributes().get("exp"))
                .isEqualTo(Instant.parse(now));
    }

    @Test
    public void securityStrategyWorks() {

        given(this.withMockJwtToken.securityStrategyName()).willReturn(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        given(this.withMockJwtToken.token()).willReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        given(this.withMockJwtToken.roles()).willReturn(new String[]{});
        given(this.withMockJwtToken.audience()).willReturn(new String[]{});
        given(this.withMockJwtToken.expiresAt()).willReturn("");
        given(this.withMockJwtToken.issuedAt()).willReturn("");
        given(this.withMockJwtToken.jti()).willReturn("");
        given(this.withMockJwtToken.scope()).willReturn(new String[]{});
        given(this.withMockJwtToken.additionalClaims()).willReturn(new WithMockJwtTokenClaim[]{});
        given(this.withMockJwtToken.authorities()).willReturn(new String[]{});

        assertThat(((JwtAuthenticationToken) this.factory.createSecurityContext(this.withMockJwtToken).getAuthentication())).isNotNull();
        assertThat(SecurityContextHolder.getContextHolderStrategy().getClass().getSimpleName()).isEqualTo("InheritableThreadLocalSecurityContextHolderStrategy");
    }

    @Test
    public void issuedAtWorks() {
        given(this.withMockJwtToken.token()).willReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        given(this.withMockJwtToken.roles()).willReturn(new String[]{});
        given(this.withMockJwtToken.audience()).willReturn(new String[]{});

        given(this.withMockJwtToken.expiresAt()).willReturn("");
        String now = Instant.now().toString();
        given(this.withMockJwtToken.issuedAt()).willReturn(now);
        given(this.withMockJwtToken.jti()).willReturn("");
        given(this.withMockJwtToken.scope()).willReturn(new String[]{});
        given(this.withMockJwtToken.additionalClaims()).willReturn(new WithMockJwtTokenClaim[]{});
        given(this.withMockJwtToken.authorities()).willReturn(new String[]{});
        assertThat((Instant) ((JwtAuthenticationToken) this.factory.createSecurityContext(this.withMockJwtToken).getAuthentication()).getTokenAttributes().get("iat"))
                .isEqualTo(Instant.parse(now));
    }

    @Test
    public void jtiWorks() {
        given(this.withMockJwtToken.token()).willReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        given(this.withMockJwtToken.roles()).willReturn(new String[]{});
        given(this.withMockJwtToken.audience()).willReturn(new String[]{});
        given(this.withMockJwtToken.expiresAt()).willReturn("");
        given(this.withMockJwtToken.issuedAt()).willReturn("");
        given(this.withMockJwtToken.jti()).willReturn("jit");
        given(this.withMockJwtToken.scope()).willReturn(new String[]{});
        given(this.withMockJwtToken.additionalClaims()).willReturn(new WithMockJwtTokenClaim[]{});
        given(this.withMockJwtToken.authorities()).willReturn(new String[]{});
        assertThat((String) ((JwtAuthenticationToken) this.factory.createSecurityContext(this.withMockJwtToken).getAuthentication()).getTokenAttributes().get("jti"))
                .isEqualTo("jit");
    }

    @Test
    public void scopeWorks() {
        given(this.withMockJwtToken.token()).willReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        given(this.withMockJwtToken.roles()).willReturn(new String[]{});
        given(this.withMockJwtToken.audience()).willReturn(new String[]{});
        given(this.withMockJwtToken.expiresAt()).willReturn("");
        given(this.withMockJwtToken.issuedAt()).willReturn("");
        given(this.withMockJwtToken.jti()).willReturn("");
        given(this.withMockJwtToken.scope()).willReturn(new String[]{"read", "write"});
        given(this.withMockJwtToken.additionalClaims()).willReturn(new WithMockJwtTokenClaim[]{});
        given(this.withMockJwtToken.authorities()).willReturn(new String[]{});
        assertThat((Set<String>) ((JwtAuthenticationToken) this.factory.createSecurityContext(this.withMockJwtToken).getAuthentication()).getTokenAttributes().get("scope"))
                .containsOnly("read", "write");
    }

    @Test
    public void additionalClaimsWorks() {
        given(this.withMockJwtToken.token()).willReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        given(this.withMockJwtToken.roles()).willReturn(new String[]{});
        given(this.withMockJwtToken.audience()).willReturn(new String[]{});
        given(this.withMockJwtToken.expiresAt()).willReturn("");
        given(this.withMockJwtToken.issuedAt()).willReturn("");
        given(this.withMockJwtToken.jti()).willReturn("");
        given(this.withMockJwtToken.scope()).willReturn(new String[]{});
        given(this.withMockJwtTokenClaim.name()).willReturn("account");
        given(this.withMockJwtTokenClaim.value()).willReturn("123");
        given(this.withMockJwtToken.additionalClaims()).willReturn(new WithMockJwtTokenClaim[]{withMockJwtTokenClaim});
        given(this.withMockJwtToken.authorities()).willReturn(new String[]{});
        assertThat((String) ((JwtAuthenticationToken) this.factory.createSecurityContext(this.withMockJwtToken).getAuthentication()).getTokenAttributes().get("account"))
                .isEqualTo("123");
    }

    @Test
    public void rolesWorks() {
        given(this.withMockJwtToken.token()).willReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        given(this.withMockJwtToken.roles()).willReturn(new String[]{"USER", "CUSTOM"});
        given(this.withMockJwtToken.audience()).willReturn(new String[]{});
        given(this.withMockJwtToken.expiresAt()).willReturn("");
        given(this.withMockJwtToken.issuedAt()).willReturn("");
        given(this.withMockJwtToken.jti()).willReturn("");
        given(this.withMockJwtToken.scope()).willReturn(new String[]{});
        given(this.withMockJwtToken.additionalClaims()).willReturn(new WithMockJwtTokenClaim[]{});
        given(this.withMockJwtToken.authorities()).willReturn(new String[]{});
        assertThat(this.factory.createSecurityContext(this.withMockJwtToken).getAuthentication().getAuthorities())
                .extracting("authority").containsOnly("ROLE_USER", "ROLE_CUSTOM");
    }

    @Test
    public void authoritiesWorks() {

        given(this.withMockJwtToken.token()).willReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        given(this.withMockJwtToken.audience()).willReturn(new String[]{});
        given(this.withMockJwtToken.expiresAt()).willReturn("");
        given(this.withMockJwtToken.issuedAt()).willReturn("");
        given(this.withMockJwtToken.jti()).willReturn("");
        given(this.withMockJwtToken.scope()).willReturn(new String[]{});
        given(this.withMockJwtToken.additionalClaims()).willReturn(new WithMockJwtTokenClaim[]{});
        given(this.withMockJwtToken.roles()).willReturn(new String[]{"USER"});
        given(this.withMockJwtToken.authorities()).willReturn(new String[]{"USER", "CUSTOM"});
        assertThat(this.factory.createSecurityContext(this.withMockJwtToken).getAuthentication().getAuthorities())
                .extracting("authority").containsOnly("USER", "CUSTOM");
    }

    @Test
    public void rolesWithRolePrefixFails() {
        given(this.withMockJwtToken.token()).willReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        given(this.withMockJwtToken.audience()).willReturn(new String[]{});
        given(this.withMockJwtToken.expiresAt()).willReturn("");
        given(this.withMockJwtToken.issuedAt()).willReturn("");
        given(this.withMockJwtToken.jti()).willReturn("");
        given(this.withMockJwtToken.scope()).willReturn(new String[]{});
        given(this.withMockJwtToken.roles()).willReturn(new String[]{"ROLE_FAIL"});
        given(this.withMockJwtToken.authorities()).willReturn(new String[]{});
        assertThatIllegalArgumentException().isThrownBy(() -> this.factory.createSecurityContext(this.withMockJwtToken));
    }

    @Test
    public void authoritiesAndRolesInvalid() {
        given(this.withMockJwtToken.token()).willReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        given(this.withMockJwtToken.audience()).willReturn(new String[]{});
        given(this.withMockJwtToken.expiresAt()).willReturn("");
        given(this.withMockJwtToken.issuedAt()).willReturn("");
        given(this.withMockJwtToken.jti()).willReturn("");
        given(this.withMockJwtToken.scope()).willReturn(new String[]{});
        given(this.withMockJwtToken.roles()).willReturn(new String[]{"CUSTOM"});
        given(this.withMockJwtToken.authorities()).willReturn(new String[]{"USER", "CUSTOM"});
        assertThatIllegalStateException().isThrownBy(() -> this.factory.createSecurityContext(this.withMockJwtToken));
    }
}
