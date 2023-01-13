package com.example.namoldak.util.webSocket;

import com.example.namoldak.domain.ChatRoom;
import com.example.namoldak.domain.WebSocketMessage;
import com.example.namoldak.domain.WebSocketResponseMessage;
import com.example.namoldak.util.GlobalResponse.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.namoldak.util.GlobalResponse.code.StatusCode.CHAT_ROOM_NOT_FOUND;
import static com.example.namoldak.util.GlobalResponse.code.StatusCode.IN_CHAT_ROOM_NOT_FOUND;

@Slf4j
@Component
public class SignalHandler extends TextWebSocketHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper = new ObjectMapper();
    // 세션 저장 1) clientsInRoom : 게임방 Id를 key 값으로 그 방안 참가자의 session Id들과 session 객체들을 저장
    private final Map<Long, Map<String, WebSocketSession>> clientsInRoom = new HashMap<>();
    // 세션 저장 2) roomIdToSession : 참가자들 각각의 데이터로 session 객체를 key 값으로 하여 해당 객체가 어느방에 속해있는지를 저장
    private final Map<WebSocketSession, Long> roomIdToSession = new HashMap<>();
    private static final String MSG_TYPE_JOIN_ROOM = "join_room";
    private static final String MSG_TYPE_OFFER = "offer";
    private static final String MSG_TYPE_ANSWER = "answer";
    private static final String MSG_TYPE_CANDIDATE = "candidate";
    private static final String MSG_TYPE_LEAVE = "leave";

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {
        // 웹소켓 연결이 끊어지면 실행되는 메소드

        // 끊어진 세션이 어느방에 있었는지 조회
        Long roomId = roomIdToSession.get(session);

        // session 삭제
        removeSession(session, roomId);
    }

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) {
        // 웹소켓이 연결되면 실행되는 메소드
    }

    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage textMessage) {

        try {
            WebSocketMessage message = objectMapper.readValue(textMessage.getPayload(), WebSocketMessage.class);
            String userName = message.getSender();
            String data = message.getData();
            Long roomId = message.getRoomId();

            log.info("======================================== origin message INFO");
            log.info("============== message.getType : " + message.getType());
            log.info("============== message.getRoomId : " + roomId.toString());

            ChatRoom chatRoom;
            switch (message.getType()) {
                // 처음 입장
                case MSG_TYPE_JOIN_ROOM:
                    log.info("======================================== join_room");
                    Map<String, WebSocketSession> clientList = new HashMap<>();

                    // 해당 챗룸이 존재하면
                    if (clientsInRoom.containsKey(roomId)) {
                        // 해당 챗룸을 조회
                        clientList = clientsInRoom.get(roomId);

                        // 세션 저장 1) : 게임방 Id를 key 값으로 그 방안 참가자의 session Id들과 session 객체들을 "수정"해서 저장
                        clientsInRoom.get(roomId).put(session.getId(), session);

                    } else {
                        // 세션 저장 1) : 게임방 Id를 key 값으로 그 방안 참가자의 session Id들과 session 객체들을 "새로" 저장
                        Map<String, WebSocketSession> newClient = new HashMap<>();
                        newClient.put(session.getId(), session);
                        clientsInRoom.put(roomId, newClient);
                    }
                    for (Map.Entry<String, WebSocketSession> client : clientList.entrySet()) {
                        log.info("============== 방안 참가자 세션들 : " + client.getKey() + "  value : " + client.getValue());
                    }

                    // 세션 저장 2) : 이 세션이 어느 방에 들어가 있는지 저장
                    roomIdToSession.put(session, roomId);
                    log.info("============== 입장한 참가자 세션 : " + roomIdToSession.get(session));

                    // 방안 참가자 중 자신을 제외한 나머지 사람들의 Session ID를 List로 저장
                    List<String> exportClientList = new ArrayList<>();
                    for (Map.Entry<String, WebSocketSession> entry : clientsInRoom.get(roomId).entrySet()) {
                        if (entry.getValue() != session) {
                            exportClientList.add(entry.getKey());
                        }
                    }

                    // 접속한 본인에게 방안 참가자들 정보를 전송
                    sendMessage(session,
                            new WebSocketResponseMessage().builder()
                                    .type("all_users")
                                    .sender(userName)
                                    .data(message.getData())
                                    .allUsers(exportClientList)
                                    .candidate(message.getCandidate())
                                    .sdp(message.getSdp())
                                    .build());

                    break;

                case MSG_TYPE_OFFER:
                case MSG_TYPE_ANSWER:
                case MSG_TYPE_CANDIDATE:

                    log.info("======================================== OFFER,ANSWER,ICE");
                    log.info("============== type : " + message.getType());

                    if (clientsInRoom.containsKey(roomId)) {
                        Map<String, WebSocketSession> sClientList = clientsInRoom.get(roomId);

                        for (Map.Entry<String, WebSocketSession> client : sClientList.entrySet()) {
                            log.info("============== 방안 참가자 세션들 : " + client.getKey() + "  value : " + client.getValue());
                        }

                        if (sClientList.containsKey(message.getReceiver())) {
                            WebSocketSession ws = sClientList.get(message.getReceiver());
                            log.info("============== " + message.getReceiver() + "에게 전달");
                            sendMessage(ws,
                                    new WebSocketResponseMessage().builder()
                                            .type(message.getType())
                                            .sender(session.getId())            // 보낸사람 session Id
                                            .receiver(message.getReceiver())    // 받을사람 session Id
                                            .data(message.getData())
                                            .offer(message.getOffer())
                                            .answer(message.getAnswer())
                                            .candidate(message.getCandidate())
                                            .sdp(message.getSdp())
                                            .build());
                        }
                    } else {
                        throw new CustomException(CHAT_ROOM_NOT_FOUND);
                    }
                    break;

                case MSG_TYPE_LEAVE:

                    log.info("======================================== LEAVE");
                    // session 삭제
                    removeSession(session,roomId);

                    break;

                default:
                    log.info("======================================== DEFAULT");
                    log.info("============== 타입이 정의되지 않았습니다 : " + message.getType());

            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // 메세지 발송
    private void sendMessage(WebSocketSession session, WebSocketResponseMessage message) {
        try {
            log.info("======================================== sendMessage START");
            String json = objectMapper.writeValueAsString(message);
            log.info("============== 보낼 형태 : " + json);
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            log.info("============== 발생한 에러 메세지: " + e.getMessage());
        }
    }

    // 접속을 끊은 세션에 대해 2가지 데이터에서 삭제
    public void removeSession(WebSocketSession session, Long roomId){

        // 1) 방 참가자들 세션 정보들 사이에서 삭제

        Map<String, WebSocketSession> clientList = clientsInRoom.get(roomId);
        // 끊어진 세션을 맵에서 찾아 제거
        for (Map.Entry<String, WebSocketSession> entry : clientList.entrySet()) {
            if (entry.getValue() == session) {
                log.info("============== DELETE : " + entry.getKey() + ", SessionID : " + entry.getValue());
                clientList.remove(entry.getKey());
            } else {
                throw new CustomException(IN_CHAT_ROOM_NOT_FOUND);
            }
        }
        // 끊어진 세션을 제외한 나머지 세션들을 다시 저장
        clientsInRoom.put(roomId, clientList);

        // 2) 별도 해당 참가자 세션 정보도 삭제
        roomIdToSession.remove(session);
    }
}
