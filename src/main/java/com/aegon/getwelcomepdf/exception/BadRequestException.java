package com.aegon.getwelcomepdf.exception;
import lombok.Getter;
import lombok.ToString;
@ToString
public class BadRequestException extends RuntimeException {
    private static final long serialVersionUID = -5457893143670666696L;
    @Getter
    private String message;
    @Getter
    private String details;
    @Getter
    private Integer status;
    public BadRequestException(String message) {
        super(message);
        this.message = message;
    }
    public BadRequestException(String message, String details, Integer status) {
        super(details);
        this.status = status;
        this.details = details;
        this.message = message;
    }
}
