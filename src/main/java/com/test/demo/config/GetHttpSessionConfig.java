package com.test.demo.config;
import javax.websocket.server.ServerEndpointConfig.Configurator;
import javax.websocket.server.ServerEndpointConfig;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;

/**
 * Created by yhn on 2017/11/5.
 */
public class GetHttpSessionConfig extends Configurator{
    @Override
    public void modifyHandshake(ServerEndpointConfig sec,HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession=(HttpSession) request.getHttpSession();
        sec.getUserProperties().put(HttpSession.class.getName(),httpSession);
    }
}
