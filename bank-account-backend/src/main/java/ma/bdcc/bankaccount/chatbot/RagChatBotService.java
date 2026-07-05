package ma.bdcc.bankaccount.chatbot;

import ma.bdcc.bankaccount.dto.AccountOperationDTO;
import ma.bdcc.bankaccount.dto.BankAccountDTO;
import ma.bdcc.bankaccount.dto.CustomerDTO;
import ma.bdcc.bankaccount.service.BankService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagChatBotService {
    private final BankService bankService;

    private static final String OPENAI_API_KEY = "YOUR_OPENAI_API_KEY_HERE";
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public RagChatBotService(BankService bankService) {
        this.bankService = bankService;
    }

    public String processMessage(String userMessage) {
        String userMessageLower = userMessage.toLowerCase();

        String contextData = retrieveContextData(userMessageLower);

        if (contextData != null) {
            return contextData;
        }

        return callOpenAI(userMessage);
    }

    private String retrieveContextData(String message) {
        if (message.contains("client") || message.contains("customer")) {
            List<CustomerDTO> customers = bankService.listCustomers();
            if (customers.isEmpty()) return "Aucun client trouvé.";
            return "Voici la liste des clients:\n" + customers.stream()
                .map(c -> "  - " + c.getName() + " (" + c.getEmail() + ")")
                .collect(Collectors.joining("\n"));
        }

        if (message.contains("compte") || message.contains("account")) {
            List<BankAccountDTO> accounts = bankService.listAccounts();
            if (accounts.isEmpty()) return "Aucun compte trouvé.";
            return "Voici la liste des comptes:\n" + accounts.stream()
                .map(a -> "  - " + a.getId().substring(0, 8) + "... | Solde: " + a.getBalance() + "€ | Client: " + a.getCustomerDTO().getName())
                .collect(Collectors.joining("\n"));
        }

        if (message.contains("solde") || message.contains("balance")) {
            List<BankAccountDTO> accounts = bankService.listAccounts();
            double total = accounts.stream().mapToDouble(BankAccountDTO::getBalance).sum();
            return "Le solde total de tous les comptes est de " + total + " €.";
        }

        return null;
    }

    private String callOpenAI(String message) {
        try {
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            String requestBody = "{"
                + "\"model\": \"gpt-3.5-turbo\","
                + "\"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}],"
                + "\"temperature\": 0.7"
                + "}";

            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(OPENAI_API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + OPENAI_API_KEY)
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            return "Je suis un assistant bancaire. Pour l'instant, je peux vous renseigner sur: "
                + "les clients, les comptes, les soldes. "
                + "Veuillez préciser votre question (ex: 'liste des clients', 'soldes des comptes').";
        } catch (Exception e) {
            return "Désolé, une erreur est survenue. Veuillez réessayer.";
        }
    }
}
