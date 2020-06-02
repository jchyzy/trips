package agh.trips.controller;

import agh.trips.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat.sendMessage.{tripId}")
    public ChatMessage sendMessage(@Payload ChatMessage message, @DestinationVariable String tripId) {
        sendMessageToTopic(tripId,message);
        return message;
    }

    @MessageMapping("/chat.newUser.{tripId}")
    public ChatMessage newUser(@Payload ChatMessage message, @DestinationVariable String tripId, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        sendMessageToTopic(tripId, message);
        return message;
    }

    @MessageMapping("/chat.leave.{tripId}")
    public ChatMessage leaveChat(@Payload ChatMessage message, @DestinationVariable String tripId) {
        System.out.println("leaving for real with sending message");
        sendMessageToTopic(tripId, message);
        return message;
    }

    private void sendMessageToTopic(String tripId, ChatMessage message){
        String destination = "/topic/chat."+tripId;
        this.simpMessagingTemplate.convertAndSend(destination, message);
    }
}
