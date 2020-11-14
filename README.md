# spring-security-test-withmockjwttoken
WithMockJwtToken annotation that will create a JwtAuthenticationToken.


## How to use

Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:


````java
allprojects {
   repositories {
    ...
	maven { url 'https://jitpack.io' }
   }
}
````

Step 2. Add the dependency

````java
dependencies {
	        implementation 'com.github.pedrorlmarques:spring-security-withJwtToken-test:Tag'
	}
````

<br>
Add the annotation at the Test Class level
<br>

Default

```java
@WithMockJwtToken(subject = "pedro")
```

More customisation

```java
@WithMockJwtToken(subject = "pedro", authorities = {"PT", "read-write"}, additionalClaims = {
        @WithMockJwtTokenClaim(name = "account", value = "pedro-1"),
        @WithMockJwtTokenClaim(name = "tenant", value = "dev")
})
```

## Options

````java
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


```` 

WithMockJwtTokenClaim

````java
public @interface WithMockJwtTokenClaim {

    /**
     * The claim name to be used.
     *
     * @return
     */
    String name();

    /**
     * The claim value to be used.
     *
     * @return
     */
    String value();
}

````

