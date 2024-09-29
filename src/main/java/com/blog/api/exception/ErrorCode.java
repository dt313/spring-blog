package com.blog.api.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@ToString
public enum ErrorCode {
    //
    UNCATEGORIZED_EXCEPTION(9999,"Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    NO_RESOURCE(9998, "Resource not found", HttpStatus.NOT_FOUND),
    BOOKMARK_SELF_ARTICLE(9997, "Cannot bookmark self article", HttpStatus.BAD_REQUEST),
    REACTION_NO_TYPE_TABLE(9996, "Comment type of table is not found", HttpStatus.NOT_FOUND),

    // Auth [100x]
    UNAUTHENTICATED(1001,"Unauthenticated", HttpStatus.UNAUTHORIZED),
    WRONG_PASSWORD(1002,"Password is not true", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1003, "Validation Invalid", HttpStatus.BAD_REQUEST),
    ILLEGAL_REGISTRATION_ID(1004, "illegal registration id", HttpStatus.NOT_ACCEPTABLE),
    TOKEN_INVALID(1005,"token invalid", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(1006,"token expired", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007,"Permission denied", HttpStatus.FORBIDDEN),

    // Invalid
    USERNAME_INVALID(2001, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(2001, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    DOB_INVALID(2001, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),

    // NOT FOUND
    USER_NOT_FOUND(4004, "Username is not signed", HttpStatus.NOT_FOUND),
    PERMISSION_NOT_FOUND(4004, "Permission not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(4004, "Role not found", HttpStatus.NOT_FOUND),
    TOPIC_NOT_FOUND(4004, "Topic not found", HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND(4004, "Comment not found", HttpStatus.NOT_FOUND),
    REACTION_NOT_FOUND(4004, "Reaction not found", HttpStatus.NOT_FOUND),
    ARTICLE_NOT_FOUND(4004, "Article not found", HttpStatus.NOT_FOUND),
    NOTIFICATION_NOT_FOUND(4004, "Notification is not found", HttpStatus.NOT_FOUND),
    IMAGE_NOT_FOUND(4004, "Notification is not found", HttpStatus.NOT_FOUND),

    // Exists - conflict
    USER_EXISTS(3003, "User exists", HttpStatus.CONFLICT),
    PERMISSION_EXIST(3003, "Permission exists", HttpStatus.CONFLICT),
    ROLE_EXIST(3003, "Role exists", HttpStatus.CONFLICT),
    TOPIC_EXIST(3003, "Role exists", HttpStatus.CONFLICT),
    USER_CONFLICT(3003, "User conflict", HttpStatus.CONFLICT),
    ARTICLE_EXISTS(3003, "Article exists", HttpStatus.CONFLICT),

    // Validation
    VALIDATION_ERROR(8888, "Validation Error", HttpStatus.BAD_REQUEST)







    ;





    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
    int code;
    private final String message;
    private final HttpStatusCode statusCode;

}
