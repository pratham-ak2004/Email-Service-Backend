package com.main.emailService.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Mail {

    @Component
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmailValue {
        @JsonProperty("email")
        private String email;
        @JsonProperty("value")
        private boolean value;
    }

    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("sender")
    private String sender;

    @JsonProperty("to")
    private List<EmailValue> to;

    @JsonProperty("cc")
    private List<EmailValue> cc;

    @JsonProperty("bcc")
    private List<EmailValue> bcc;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("message")
    private String message;

}
