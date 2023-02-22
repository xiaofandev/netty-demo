package springboot.websocket;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
	
	private Map<String, WebSocketSession> clients = new HashMap<>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		clients.put(session.getId(), session);
	}
	
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		// 群发响应信息加上sessionid，表明是谁发的
		String json = session.getId()+":"+message.getPayload();
		for (WebSocketSession ss : clients.values()) {
			ss.sendMessage(new TextMessage(json));
		}
	}
}
