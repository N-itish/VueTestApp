package com.nitish.ChatApp.Handlers.Impl;

import com.nitish.ChatApp.Entity.MessageBody;
import com.nitish.ChatApp.Handlers.Handler;
import com.nitish.ChatApp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class CallHandler implements Handler {
    private final static String DESTINATION = "topic/greeting";
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageBody messageBody;
    private UserRepository userRepo;

    @Autowired
    public CallHandler(SimpMessagingTemplate messagingTemplate,UserRepository userRepo, MessageBody messageBody){
            this.messagingTemplate = messagingTemplate;
            this.messageBody = messageBody;
            this.userRepo = userRepo;
    }
    @Override
    public void returnMessage() {
        if(messageBody.getMessage().equalsIgnoreCase("callRequested")) {
            //convertAndSendToUser automatically adds the "/user/" infront of the destination string
            System.out.println("Requesting permission from reciever");
            messagingTemplate.convertAndSendToUser(userRepo.findEmailByUserName(messageBody.getReciever()), DESTINATION,
                 "callRequest:"+userRepo.findUserNameByEmail(getCurrentUser()));
        }else if(messageBody.getMessage().equalsIgnoreCase("callAccepted")){
            System.out.println("call has been accepted");
            messagingTemplate.convertAndSendToUser(userRepo.findEmailByUserName(messageBody.getReciever()), DESTINATION,
                    "callAccepted");
        }else{
            messagingTemplate.convertAndSendToUser(userRepo.findEmailByUserName(messageBody.getReciever()), DESTINATION,
                    messageBody.getMessage());
        }
    }

    private String getCurrentUser(){
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
             username = ((UserDetails)principal).getUsername();
        } else {
             username = principal.toString();
        }
        return username;
    }
}
