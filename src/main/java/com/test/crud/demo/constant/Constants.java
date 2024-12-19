package com.test.crud.demo.constant;

public interface Constants {
    String[] AUTH_WHITELIST = {
            "/swagger-ui/**",
            "/api-docs/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/webjars/**",
            "/user/register",
            "/user/login",
            "/auth/validateAccessToken",
            "/context-path/**","/v3/api-docs/**",
    };

    class Response {
        public static final int SUCCESS_CODE = 200;
        public static final int ERROR_CODE = 500;
        public static final int NOT_FOUND_CODE = 404;
        public static final String SUCCESS_MESSAGE = "Success";
        public static final String ERROR_MESSAGE = "Error";
        public static final String NOT_FOUND_MESSAGE = "Not Found";
        public static final String SUCCESS_VALID_TOKEN_MESSAGE = "Access token valid";
    }

    class Message {
        public static final String EXIST_DATA_MESSAGE = "data already exist";
        public static final String INVALID_PASSWORD_MESSAGE = "Password salah";
        public static final String INVALID_USERNAME_MESSAGE = "Username salah";
        public static final String GENERAL_AUTHENTICATION_ERROR = "Authentication failed";
        public static final String NOT_FOUND_DATA_MESSAGE = "data not found";
        public static final String FORBIDDEN_REQUEST_MESSAGE = "Different {value} with exist data is forbidden";
        public static final String INVALID_LOGIN_MESSAGE = "Username / Password wrong";
        public static final String INVALID_TOKEN_MESSAGE = "Invalid access token";

    }

    class Regex {
        public static final String NUMERIC = "\\d+";
        public static final String ALPHANUMERIC = "^[a-zA-Z0-9]+$";
        public static final String ALPHABET = "^[a-zA-Z]+$";
        public static final String ALPHANUMERIC_WITH_DOT_AND_SPACE = "^[a-zA-Z0-9.' ]+$";
    }
}
