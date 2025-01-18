package com.whatsapp.writer.app;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/whatsapp")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class whatsAppChatGeneratorController {

    private final WhatsAppChatService whatsAppChatService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateChat(@RequestBody WhatsAppRequest whatsAppRequest){

        String response = whatsAppChatService.generateChatReply(whatsAppRequest);

        return ResponseEntity.ok(response);
    }
}
