package app.mathias.sector.exception;

import app.mathias.sector.utils.MessageHelper;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(MessageHelper.getMessage("error.resource.notfound", resourceName, fieldName, fieldValue));
    }
} 