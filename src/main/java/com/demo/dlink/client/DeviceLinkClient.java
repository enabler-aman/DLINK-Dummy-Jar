package com.demo.dlink.client;

import com.demo.dlink.client.model.DeviceEvent;
import com.demo.dlink.client.model.PrintCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

public class DeviceLinkClient {

    public static void main(String[] args) throws Exception {
        // Create WebSocket STOMP client
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        // Connect asynchronously
        CompletableFuture<StompSession> future =
                stompClient.connectAsync("ws://localhost:8080/ws-endpoint", new SimpleStompHandler());

        // Wait for connection
        StompSession session = future.join();
        System.out.println("✅ Connected to WebSocket server!");

        String stationId = "PC1";

        // Loop: send event every 5 seconds
//        int counter = 1;
//        while (true) {
//            // Create new event
//            DeviceEvent event = new DeviceEvent(
//                    "PRINTER",
//                    "REGISTERED",
//                    "Printer connected successfully (event " + counter + ")",
//                    "Message #" + counter
//            );
//
//            // Send message
//            session.send("/app/device/" + stationId + "/event", event);
//            System.out.println("📡 Sent device event #" + counter);
//
//            counter++;
//            // Wait for 5 seconds before next event
//            TimeUnit.SECONDS.sleep(5);
//        }
        // Subscribe to messages for this station
        session.subscribe("/topic/device/" + stationId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                System.out.println("✅ Subscribed to /topic/device/" + stationId);
                return PrintCommand.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("📥 Raw frame: " + payload);

                ObjectMapper mapper = new ObjectMapper();
                PrintCommand command = mapper.convertValue(payload, PrintCommand.class);

                System.out.println("🖨️ Received print command: " + command);
            }

        });

        // Also subscribe to admin channel (optional)
        session.subscribe("/topic/admin", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return DeviceEvent.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                DeviceEvent event = (DeviceEvent) payload;
                System.out.println("👑 Admin broadcast received: " + event);
            }
        });

        System.out.println("🛰️ Listening for messages...");

        // Keep connection alive
        Thread.sleep(Long.MAX_VALUE);
    }

    // Basic handler for connection lifecycle events
    static class SimpleStompHandler extends StompSessionHandlerAdapter {
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            System.out.println("🚀 STOMP session established!");
        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            System.err.println("❌ Transport error: " + exception.getMessage());
        }
    }
}
