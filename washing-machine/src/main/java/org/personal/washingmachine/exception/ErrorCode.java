package org.personal.washingmachine.exception;

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
    public static final int F_0002 = 2;

    // COMMON BACKEND CONNECTION ERROR
    public static final int E_2000 = 2000;

    /**
     * <p>{@code ErrorCode} instances should NOT be constructed in
     * standard programming. Instead, the class should be used as
     * {@code ErrorCode.E_1001;}.</p>
     */
    private ErrorCode(){}
}
