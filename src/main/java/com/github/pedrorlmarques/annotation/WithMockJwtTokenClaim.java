package com.github.pedrorlmarques.annotation;

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
