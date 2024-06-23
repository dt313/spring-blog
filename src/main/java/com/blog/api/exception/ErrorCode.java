package com.blog.api.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@ToString
public enum ErrorCode {
    NO_RESOURCE(9998, "Resource not found", HttpStatus.NOT_FOUND),
    UNCATEGORIZED_EXCEPTION(9999,"Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED(9997,"Permission denied", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(9996,"Unauthenticated", HttpStatus.UNAUTHORIZED),
    WRONG_PASSWORKD(9993,"Password is not true", HttpStatus.UNAUTHORIZED),
    INVALID_KEY(9995, "Validation Invalid", HttpStatus.BAD_REQUEST),
    NO_SUCH_METHOD(9994, "request format is unacceptable", HttpStatus.NOT_ACCEPTABLE),

    // User
    USER_NOT_FOUND(1001, "Username is not signed", HttpStatus.NOT_FOUND),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_EXISTS(1002, "User exists", HttpStatus.CONFLICT),
    USER_CONFLICT(1005, "Token and user conflict", HttpStatus.CONFLICT),
    // Validation
    DOB_INVALID(2001, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),

    // Permission
    PERMISSION_NOT_FOUND(3001, "Permission not found", HttpStatus.NOT_FOUND),
    PERMISSION_EXIST(3002, "Permission exist", HttpStatus.BAD_REQUEST),
    // Role
    ROLE_NOT_FOUND(4001, "Role not found", HttpStatus.NOT_FOUND),
    ROLE_EXIST(4002, "Role exist", HttpStatus.CONFLICT),

    // Topic
    TOPIC_NOT_FOUND(4001, "Role not found", HttpStatus.NOT_FOUND),
    TOPIC_EXIST(4002, "Role exist", HttpStatus.CONFLICT),


    // Comment
    COMMENT_NOT_FOUND(4001, "Comment not found", HttpStatus.NOT_FOUND),
    COMMENT_EXIST(4002, "Comment exist", HttpStatus.CONFLICT),

    // Reaction
    // Comment
    REACTION_NOT_FOUND(4001, "Reaction not found", HttpStatus.NOT_FOUND),
    REACTION_EXIST(4002, "Reaction exist", HttpStatus.CONFLICT),

    // User
    ARTICLE_NOT_FOUND(5001, "Article not found", HttpStatus.NOT_FOUND),
    ARTICLE_EXISTS(5002, "Article exists", HttpStatus.CONFLICT),
    BOOKMARK_SELF_ARTICLE(5002, "Cannot bookmark self article", HttpStatus.BAD_REQUEST),

    NO_TYPE_TABLE(1234, "Type of table is not found", HttpStatus.NOT_FOUND),

    QUESTION_NOT_FOUND(1234, "Question is not found", HttpStatus.NOT_FOUND)

    ;





    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
    int code;
    private String message;
    private HttpStatusCode statusCode;

}
