package com.example.namoldak.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

// 기능 : Redis에 저장되는 챗룸 객체
@Getter
@Setter
public class ChatRoom implements Serializable {
    // redis에 저장되는 객체들은 Serialize 가능해야함
    private static final long serialVersionUID = 6494678977089006639L;
    private String roomId;
    private String name;

    public static ChatRoom create(String name, String roomId) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId   = roomId;
        chatRoom.name     = name;
        return chatRoom;
    }
}
