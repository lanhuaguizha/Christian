package com.christian.annotation;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class Session {

    private String d;

    public void setD(@Nullable String d) {
        this.d = d;
    }
}
