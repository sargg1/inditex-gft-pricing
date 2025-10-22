package com.inditex.pricing.infra.rest;

import com.inditex.pricing.application.exception.PriceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(PriceNotFoundException.class)
    ProblemDetail handlePriceNotFoundException(PriceNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setDetail(exception.getMessage());
        return problemDetail;
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            ConstraintViolationException.class
    })
    ProblemDetail handleValidationException(Exception exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(resolveValidationMessage(exception));
        return problemDetail;
    }

    @ExceptionHandler(DataAccessException.class)
    ProblemDetail handleDataAccessException(DataAccessException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.SERVICE_UNAVAILABLE);
        problemDetail.setDetail("A database error occurred while processing the request");
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail handleUnexpectedException(Exception exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setDetail("An unexpected error occurred. Please try again later.");
        return problemDetail;
    }

    private String resolveValidationMessage(Exception exception) {
        if (exception instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            return methodArgumentNotValidException.getBindingResult().getFieldErrors().stream()
                    .map(error -> String.format("%s %s", error.getField(), error.getDefaultMessage()))
                    .collect(Collectors.joining(", "));
        }
        if (exception instanceof MethodArgumentTypeMismatchException typeMismatchException) {
            return String.format("Parameter '%s' %s", typeMismatchException.getName(), typeMismatchException.getMostSpecificCause().getMessage());
        }
        if (exception instanceof MissingServletRequestParameterException missingParameterException) {
            return String.format("Parameter '%s' is required", missingParameterException.getParameterName());
        }
        if (exception instanceof ConstraintViolationException constraintViolationException) {
            return constraintViolationException.getConstraintViolations().stream()
                    .map(violation -> violation.getMessage())
                    .collect(Collectors.joining(", "));
        }
        return exception.getMessage();
    }
}
