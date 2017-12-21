package com.test.demo.websocket;
import com.test.demo.config.GetHttpSessionConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by yhn on 2017/8/31.
 */
@Component
@ServerEndpoint(value = "/websocket",configurator = GetHttpSessionConfig.class)
@Slf4j
public class WebSocket {
    private Session session;
    private static  CopyOnWriteArraySet<WebSocket> webSocketSet  = new CopyOnWriteArraySet<>();

    @OnOpen
    public synchronized void onopen(Session session, EndpointConfig endpointConfig){
        this.session = session;
        webSocketSet.add(this);
        HttpSession httpSession= (HttpSession) endpointConfig.getUserProperties().get(HttpSession.class.getName());
        httpSession.setAttribute("webSokcetSessionId",session.getId());
//        RditNewsController.userid = session.getId();
//        log.info("连接的用户sessionid"+session.getId()+"-userid："+UploadFileController.userid);
        log.info("【websocket消息】有了新的连接，总数为：{}",webSocketSet.size());
    }
    @OnClose
    public void onclose(){
            webSocketSet.remove(this);
            log.info("【websocket消息】连接断开，总数为：{}", webSocketSet.size());
    }
    @OnMessage
    public void onMessage(String message){
        log.info("【websocket消息】收到客户端发来的消息:{}",message);
    }
    public void sendMessage(String message,String id){
        for(WebSocket webSocket:webSocketSet){
            log.info("【websocket消息】消息，message={}",message);
            try {
                if(webSocket.session.getId().equals(id)) {
                    webSocket.session.getBasicRemote().sendText(message);
//                    webSocket.session.getBasicRemote().sendObject();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
