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

package com.github.pedrorlmarques.annotation;

import com.github.pedrorlmarques.support.WithMockJwtTokenSecurityContextFactory;
import org.springframework.core.annotation.AliasFor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.context.TestContext;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(factory = WithMockJwtTokenSecurityContextFactory.class)
public @interface WithMockJwtToken {

    /**
     * The subject to be used.
     *
     * @return
     */
    String subject();

    /**
     * Scopes to be used.
     *
     * @return
     */
    String[] scope() default {};

    /**
     * The jti to be used.
     *
     * @return
     */
    String jti() default "";

    /**
     * <p>
     * The roles to use. The default is "USER". A {@link GrantedAuthority} will be created
     * for each value within roles. Each value in roles will automatically be prefixed
     * with "ROLE_". For example, the default will result in "ROLE_USER" being used.
     * </p>
     * <p>
     * If {@link #authorities()} is specified this property cannot be changed from the
     * default.
     * </p>
     *
     * @return
     */
    String[] roles() default {"USER"};

    /**
     * <p>
     * The authorities to use. A {@link GrantedAuthority} will be created for each value.
     * </p>
     *
     * <p>
     * If this property is specified then {@link #roles()} is not used. This differs from
     * {@link #roles()} in that it does not prefix the values passed in automatically.
     * </p>
     *
     * @return
     */
    String[] authorities() default {};

    /**
     * The audience to be used.
     *
     * @return
     */
    String[] audience() default {};

    /**
     * The expiresAt to be used.
     * Instant format
     *
     * @return
     */
    String expiresAt() default "";

    /**
     * The issuedAt to be used.
     * Instant format
     *
     * @return
     */
    String issuedAt() default "";

    /**
     * The token to be used. The default random token.
     * JWT format
     *
     * @return
     */
    String token() default "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";


    /**
     * Additional claims to be added on JWT Token
     *
     * @return
     */
    WithMockJwtTokenClaim[] additionalClaims() default {};

    /**
     * Determines when the {@link SecurityContext} is setup. The default is before
     * {@link TestExecutionEvent#TEST_METHOD} which occurs during
     * {@link org.springframework.test.context.TestExecutionListener#beforeTestMethod(TestContext)}
     *
     * @return the {@link TestExecutionEvent} to initialize before
     * @since 5.1
     */
    @AliasFor(annotation = WithSecurityContext.class)
    TestExecutionEvent setupBefore() default TestExecutionEvent.TEST_METHOD;
}
