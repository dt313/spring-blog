package com.blog.api.exception;

import com.blog.api.dto.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        errorResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        // Log thông tin chi tiết về ngoại lệ để dễ dàng theo dõi
        log.error("Unhandled exception: ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ErrorResponse> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setCode(errorCode.getCode());
        errorResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ErrorCode.NO_RESOURCE.getCode());
        errorResponse.setMessage(ErrorCode.NO_RESOURCE.getMessage());
        return ResponseEntity.status(ErrorCode.NO_RESOURCE.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(value = TransactionSystemException.class)
    ResponseEntity<ErrorResponse> handleTransactionSystemException(TransactionSystemException ex) {
        Throwable cause = ex.getRootCause(); // Lấy nguyên nhân gốc

        if (cause instanceof ConstraintViolationException constraintViolationException) {
            Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();

            String errorMessage = "";
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<?> violation : violations) {
                errorMessage = violation.getMessage(); // Lấy interpolatedMessage
                break;
            }
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(ErrorCode.VALIDATION_ERROR.getCode()); // Sử dụng mã lỗi phù hợp
            errorResponse.setMessage(errorMessage);
            errorResponse.setStatusCode(HttpStatus.BAD_REQUEST); // Bao gồm chi tiết các lỗi

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        // Nếu không phải lỗi do ConstraintViolation, trả về lỗi chung
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ErrorCode.NO_RESOURCE.getCode());
        errorResponse.setMessage(ErrorCode.NO_RESOURCE.getMessage());
        return ResponseEntity.status(ErrorCode.NO_RESOURCE.getStatusCode()).body(errorResponse);
    }


    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ErrorResponse> handleAccessDeniedException( AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ErrorResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String enumKey = ex.getFieldError().getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
            var constraintViolation = ex.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);
            attributes = constraintViolation.getConstraintDescriptor().getAttributes();
            log.info(attributes.toString());

        }catch (IllegalArgumentException e) {

        }

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(errorCode.getCode());
        if(Objects.nonNull(attributes)) {
            String message = mapAttribute(errorCode.getMessage(), attributes);
            errorResponse.setMessage(message);
        } else {
            errorResponse.setMessage(errorCode.getMessage());
        }

        return ResponseEntity.badRequest().body(errorResponse);
    }


    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        ErrorCode errorCode = ErrorCode.IMAGE_UPLOAD_ERROR;

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ErrorResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }


}
