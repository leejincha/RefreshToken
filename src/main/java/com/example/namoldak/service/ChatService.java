package com.example.namoldak.service;

import com.example.namoldak.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    // Redis를 이용하는 방법에는 두가지가 있다 RedisTemplate, RedisRepository
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessageSendingOperations sendingOperations;
    private final ChannelTopic channelTopic;

    public void meesage(ChatMessage message) {

        log.info(message.getMessage());
        log.info(message.getRoomId());
        log.info(message.getSender());
        log.info(String.valueOf(message.getType()));
        log.info((String) message.getContent());

        ChatMessage exportMessage;

        switch (message.getType()){
            case "ENTER":
                exportMessage = ChatMessage.builder()
                        .type(message.getType())
                        .sender(message.getSender())
                        .message("[공지] " + message.getSender() + "님이 입장하셨습니다.")
                        .build();

                sendingOperations.convertAndSend("/sub/gameroom/" + message.getRoomId(), exportMessage);
                break;

            case "ICE":
                exportMessage = ChatMessage.builder()
                        .type(message.getType())
                        .sender(message.getSender())
                        .ice(message.getIce())
                        .build();

                sendingOperations.convertAndSend("/sub/gameroom/" + message.getRoomId(), exportMessage);
                break;

            case "OFFER":
                exportMessage = ChatMessage.builder()
                        .type(message.getType())
                        .sender(message.getSender())
                        .offer(message.getOffer())
                        .build();

                sendingOperations.convertAndSend("/sub/gameroom/" + message.getRoomId(), exportMessage);
                break;

            case "ANSWER":
                exportMessage = ChatMessage.builder()
                        .type(message.getType())
                        .sender(message.getSender())
                        .answer(message.getAnswer())
                        .build();

                sendingOperations.convertAndSend("/sub/gameroom/" + message.getRoomId(), exportMessage);
                break;

            default:
                // Websocket에 발행된 메시지를 redis로 발행(publish)
//        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
                sendingOperations.convertAndSend("/sub/gameroom/" + message.getRoomId(), message);
        }
    }
}
