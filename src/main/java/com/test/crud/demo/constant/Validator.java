package com.test.crud.demo.constant;

public class Validator {
    public static class Regex{
        public static final String ALPHANUMERIC_PATTERN = "^[a-zA-Z ]*$";

        public static final String NUMERIC = "\\d+";

    }

    public static class Message{
         public static final String INVALID_SUCCESS = "Invalid Success";
         public static final String INVALID_FAILED = "Invalid Failed Format {value}";

         public static final String INVALID_NAME_STAFF = "Invalid {value} Null";

         public static final String INVALID_NULL = "Invalid {value} Null";

         public static final String INVALID_PASSWWORD = "Invalid {value} not match";
         public static final String INVALID_USERNAME = "Invalid {value} null";
        public static final String INVALID_PASSWWORD_NULL = "Invalid {value} null";
    }


}
