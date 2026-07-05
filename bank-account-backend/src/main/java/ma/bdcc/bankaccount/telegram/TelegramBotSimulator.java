package ma.bdcc.bankaccount.telegram;

import ma.bdcc.bankaccount.chatbot.RagChatBotService;
import org.springframework.stereotype.Component;
import java.util.Scanner;

@Component
public class TelegramBotSimulator {
    private final RagChatBotService chatBotService;

    public TelegramBotSimulator(RagChatBotService chatBotService) {
        this.chatBotService = chatBotService;
    }

    public void startConsoleBot() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== BANK APP CHATBOT (Console Mode) ===");
        System.out.println("Posez vos questions sur les clients, comptes, soldes.");
        System.out.println("Tapez 'quit' pour quitter.\n");

        while (true) {
            System.out.print("Vous: ");
            String input = scanner.nextLine();
            if ("quit".equalsIgnoreCase(input)) break;

            String response = chatBotService.processMessage(input);
            System.out.println("Bot: " + response + "\n");
        }
        System.out.println("Chatbot terminé.");
    }
}
