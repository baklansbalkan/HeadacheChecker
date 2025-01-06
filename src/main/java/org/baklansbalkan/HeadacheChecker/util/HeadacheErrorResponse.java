package org.baklansbalkan.HeadacheChecker.util;

public class HeadacheErrorResponse {
    private String message;

    public HeadacheErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
