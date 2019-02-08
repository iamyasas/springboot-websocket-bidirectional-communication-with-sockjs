package com.iamyasas.springbootjwtauthdemo.websocket;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class GreetingController {
	
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@MessageMapping("/name")
	public void nameProcessor(@Payload String message, Principal principal) throws Exception {
		JsonNode jsonNode = new ObjectMapper().readTree(message);
		String response = "The message is " + jsonNode.get("name").asText();
		messagingTemplate.convertAndSendToUser(principal.getName(), "/reply/name", response);
	}
}
