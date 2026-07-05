package ma.bdcc.bankaccount.chatbot;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin("*")
public class ChatBotRestController {
    private final RagChatBotService chatBotService;

    public ChatBotRestController(RagChatBotService chatBotService) {
        this.chatBotService = chatBotService;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        String reply = chatBotService.processMessage(request.getMessage());
        return new ChatResponse(reply);
    }
}
