package com.kernelsquare.domainmongodb.domain.alert.entity;

import io.micrometer.common.util.StringUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Document(collection = "alert")
public class Alert {
    @Id
    private String id;

    @Indexed
    private String recipientId;

    private String senderId;

    private String message;

    private AlertType alertType;

    private LocalDateTime sendTime;

    @Getter
    @RequiredArgsConstructor
    public enum AlertType {
        QUESTION_REPLY("question_reply"),
        RANK_ANSWER("rank_answer");

        private final String description;
    }

    @Builder
    public Alert(String recipientId, String senderId, String message, AlertType alertType) {
        if (StringUtils.isBlank(recipientId))
            throw new InvalidParameterException("Invalid recipientId");
        if (StringUtils.isBlank(senderId))
            throw new InvalidParameterException("Invalid senderId");
        if (StringUtils.isBlank(message))
            throw new InvalidParameterException("Invalid message");
        if (Objects.isNull(alertType))
            throw new InvalidParameterException("Invalid AlertType");

        this.recipientId = recipientId;
        this.senderId = senderId;
        this.message = message;
        this.alertType = alertType;
        this.sendTime = LocalDateTime.now();
    }
}
