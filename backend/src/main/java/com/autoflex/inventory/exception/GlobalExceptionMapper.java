package com.autoflex.inventory.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof NotFoundException) {
            return buildErrorResponse(Response.Status.NOT_FOUND, exception.getMessage());
        }
        
        if (exception instanceof BusinessException) {
            return buildErrorResponse(Response.Status.BAD_REQUEST, exception.getMessage());
        }
        
        if (exception instanceof ConstraintViolationException) {
            return handleConstraintViolation((ConstraintViolationException) exception);
        }
        
        // Log unexpected exceptions
        exception.printStackTrace();
        return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    private Response handleConstraintViolation(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        
        Map<String, String> errors = violations.stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        ConstraintViolation::getMessage,
                        (existing, replacement) -> existing
                ));

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Validation failed");
        body.put("errors", errors);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(body)
                .build();
    }

    private Response buildErrorResponse(Response.Status status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", message);
        body.put("status", status.getStatusCode());

        return Response.status(status)
                .entity(body)
                .build();
    }
}
