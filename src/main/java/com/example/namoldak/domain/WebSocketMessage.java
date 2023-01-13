package com.example.namoldak.domain;

import lombok.Getter;

// 기능 : 프론트에서 받는 시그널링용 Message
@Getter
public class WebSocketMessage {
    private String sender;
    private String type;
    private String data;
    private Long roomId;
    private String receiver;
    private Object offer;
    private Object answer;
    private Object candidate;
    private Object sdp;

}
