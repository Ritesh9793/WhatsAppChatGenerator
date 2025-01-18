package com.whatsapp.writer.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class WhatsAppChatService {

    private WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiURL;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public WhatsAppChatService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String generateChatReply(WhatsAppRequest whatsAppRequest) {
        // Build a prompt
        String prompt = buildPrompt(whatsAppRequest);

        // Craft a request
        Map<String, Object> requestBody= Map.of(
                "contents", new Object[] {
                        Map.of("parts", new Object[] {
                                Map.of("text", prompt)
                        })
                }
        );

        // Do request and get response


        String response = webClient.post()
                .uri(geminiApiURL + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Extract response and Return
        return extractResponseContent(response);
    }

    private String extractResponseContent(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        } catch (Exception e) {
            return "Error processing request: "+ e.getMessage();
        }
    }

    private String buildPrompt(WhatsAppRequest whatsAppRequest) {

        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a whatsApp chat reply for the following chat. Please Don't generate any unwanted lines or subject lines ");
        if(whatsAppRequest.getTone() != null && !whatsAppRequest.getTone().isEmpty()) {
            prompt.append("Use a ").append(whatsAppRequest.getTone()).append(" tone");
        }
        prompt.append("\nPerson Chatting with:  \n").append(whatsAppRequest.getChatContent());
        return null;
    }
}
