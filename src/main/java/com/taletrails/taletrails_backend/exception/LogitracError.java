package com.taletrails.taletrails_backend.exception;

public enum LogitracError {
    SERVER_ERROR(
            0,
            "Server Error",
            "Server crashed, reason not known"
    ),
    ACCESS_DENIED(
            1,
            "Access Denied",
            "User is not allowed to perform this action."
    ),
    ACCOUNT_ALREADY_EXISTS(
            2,
                    "account already exists",
                    "there is already a account with this phone number"
    ),
    ACCOUNT_DOES_NOT_EXIST(
            3,
            "account doesn't exist",
            "there is no account with this phone number"
    ),
    INVALID_PASSWORD(
            4,
            "password invalid",
            "password entered doesn't match the account's password"
    ),
    INVALID_REQUEST_DATA(
            5,
            "Data invalid",
            "Data entered is invalid may contain nulls or be missing required params"
    );


    private final int code;
    private final String shortDesc;
    private final String description;

    LogitracError(int code, String shortDesc, String description) {
        this.code = code;
        this.shortDesc = shortDesc;
        this.description = description;
    }

    public String getCode() {
        return String.format("%04d", code);
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getDescription() {
        return description;
    }
}
