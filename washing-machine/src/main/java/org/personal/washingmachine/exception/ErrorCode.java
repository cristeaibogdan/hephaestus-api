package org.personal.washingmachine.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class ErrorCode {

    // CORE ERRORS
    public static final int E_1001 = 1001;
    public static final int E_1002 = 1002;
    public static final int E_1003 = 1003;
    public static final int E_1004 = 1004;
    public static final int E_1005 = 1005;
    public static final int E_1006 = 1006;
    public static final int E_1007 = 1007;
    public static final int E_1008 = 1008;
    public static final int E_1009 = 1009;
    public static final int E_1010 = 1010;
    public static final int E_1011 = 1011;

    // FEIGN ERRORS
    public static final int F_0001 = 1;

    // COMMON BACKEND CONNECTION ERROR
    public static final int E_2000 = 2000;

    // DEFAULT ERROR
    public static final int E_9999 = 9999;
}
