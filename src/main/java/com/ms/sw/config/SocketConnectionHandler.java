package com.ms.sw.config;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SocketConnectionHandler extends TextWebSocketHandler {

    List<WebSocketSession> webSocketSessions
            = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession socketSession) throws Exception {

        super.afterConnectionEstablished(socketSession);
        System.out.println("WebSocket connected: "+socketSession.getId());

        webSocketSessions.add(socketSession);
    }


}
