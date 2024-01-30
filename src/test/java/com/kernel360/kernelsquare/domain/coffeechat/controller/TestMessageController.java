package com.kernel360.kernelsquare.domain.coffeechat.controller;

import com.kernel360.kernelsquare.domain.coffeechat.dto.ChatMessage;
import com.kernel360.kernelsquare.global.common_response.error.code.CoffeeChatErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
class TestMessageController {

    @MessageMapping("/test/message")
    @SendTo("/topic/test/room/key")
    public ChatMessage messageHandler(ChatMessage message) throws Exception {

        switch (message.getType()) {
            case ENTER -> message.setMessage(message.getSender()+"님이 입장하였습니다.");
            case LEAVE -> message.setMessage(message.getSender()+"님이 퇴장하였습니다.");
            case TALK -> {}
            default -> throw new BusinessException(CoffeeChatErrorCode.MESSAGE_TYPE_NOT_VALID);
        }

        return message;
    }
}