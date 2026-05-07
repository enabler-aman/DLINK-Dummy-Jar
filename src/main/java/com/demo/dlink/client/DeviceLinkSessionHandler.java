//package com.demo.dlink.client;
//
//import org.springframework.messaging.simp.stomp.*;
//
//public class DeviceLinkSessionHandler extends StompSessionHandlerAdapter {
//
//    @Override
//    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
//        System.out.println("Connected to DCC Backend");
//    }
//
//    @Override
//    public void handleTransportError(StompSession session, Throwable exception) {
//        System.err.println("Transport error: " + exception.getMessage());
//    }
//}
