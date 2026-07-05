package ma.bdcc.bankaccount.telegram;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

// @Component
public class TelegramBotRunner {
    // @Bean
    CommandLineRunner startTelegramBot(TelegramBotSimulator simulator) {
        return args -> {
            simulator.startConsoleBot();
        };
    }
}
