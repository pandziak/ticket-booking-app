package pl.pandziak.ticketbookingapp

import com.fasterxml.jackson.annotation.JsonProperty

class ErrorResponse {

    private final String message

    ErrorResponse(@JsonProperty("message") String message) {
        this.message = message
    }

    String getMessage() {
        return message
    }

}
