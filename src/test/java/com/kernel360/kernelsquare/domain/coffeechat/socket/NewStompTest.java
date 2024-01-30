package com.kernel360.kernelsquare.domain.coffeechat.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.kernel360.kernelsquare.domain.coffeechat.dto.ChatMessage;
import com.kernel360.kernelsquare.domain.coffeechat.dto.MessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DisplayName("되냐 ? ?  ? ? ?? ? ? ? ? ? ? ")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NewStompTest {

    @LocalServerPort
    private int port;

    private SockJsClient sockJsClient;

    private WebSocketStompClient stompClient;

    private final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

    @BeforeEach
    public void setup() {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));

        this.sockJsClient = new SockJsClient(transports);
        this.stompClient = new WebSocketStompClient(sockJsClient);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setObjectMapper(objectMapper);

        this.stompClient.setMessageConverter(messageConverter);
    }

    @Test
    public void getGreeting() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);
        StompSessionHandler handler = new TestSessionHandler(latch);
        this.stompClient.connect("ws://localhost:" + this.port + "/kernel-square", this.headers, handler);

        latch.await(30, TimeUnit.SECONDS);

        assertThat(latch.getCount()).isEqualTo(0);
    }

    class TestSessionHandler extends StompSessionHandlerAdapter {

        private final CountDownLatch latch;

        public TestSessionHandler(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            ChatMessage sendMessage = ChatMessage.builder()
                .message("hi")
                .roomKey("key")
                .type(MessageType.TALK)
                .sender("홍박사")
                .build();

            session.subscribe("/topic/chat/room/" + sendMessage.getRoomKey(), new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return ChatMessage.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    ChatMessage receiveMessage = (ChatMessage) payload;

                    System.out.println(receiveMessage.getRoomKey());

                    try {
                        assertThat(receiveMessage.getMessage()).startsWith("hi");
                        latch.countDown();
                    } catch (Throwable e) {
                        latch.countDown();
                    }
                }
            });
            try {
                System.out.println("여긴 룸키가 어떨까ㅣ ? ? ? " + sendMessage.getRoomKey());

                session.send("/app/chat/message", sendMessage);
            } catch (Throwable t) {
                latch.countDown();
            }
        }
    }
}
