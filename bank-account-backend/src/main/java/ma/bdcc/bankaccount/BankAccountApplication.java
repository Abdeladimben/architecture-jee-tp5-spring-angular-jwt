package ma.bdcc.bankaccount;

import ma.bdcc.bankaccount.dto.CustomerDTO;
import ma.bdcc.bankaccount.security.entities.AppRole;
import ma.bdcc.bankaccount.security.entities.AppUser;
import ma.bdcc.bankaccount.security.repositories.AppRoleRepository;
import ma.bdcc.bankaccount.security.repositories.AppUserRepository;
import ma.bdcc.bankaccount.service.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class BankAccountApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankAccountApplication.class, args);
    }

    @Bean
    CommandLineRunner start(BankService bankService) {
        return args -> {
            CustomerDTO c1 = bankService.saveCustomer(
                new CustomerDTO(null, "Mohamed Alaoui", "m.alaoui@email.com"));
            CustomerDTO c2 = bankService.saveCustomer(
                new CustomerDTO(null, "Fatima Benali", "f.benali@email.com"));
            CustomerDTO c3 = bankService.saveCustomer(
                new CustomerDTO(null, "Ahmed Chafik", "a.chafik@email.com"));

            bankService.saveCurrentAccount(5000, 1000, c1.getId());
            bankService.saveSavingAccount(10000, 3.5, c1.getId());
            bankService.saveCurrentAccount(8000, 2000, c2.getId());
            bankService.saveSavingAccount(15000, 4.0, c3.getId());

            System.out.println("=== DONNÉES DE DÉMONSTRATION CRÉÉES ===");
            System.out.println("Clients: " + bankService.listCustomers().size());
            System.out.println("Comptes: " + bankService.listAccounts().size());
        };
    }

    @Bean
    CommandLineRunner initUsers(AppUserRepository appUserRepository,
                                 AppRoleRepository appRoleRepository,
                                 PasswordEncoder passwordEncoder) {
        return args -> {
            AppRole adminRole = appRoleRepository.save(
                AppRole.builder().roleName("ADMIN").build());
            AppRole userRole = appRoleRepository.save(
                AppRole.builder().roleName("USER").build());

            appUserRepository.save(AppUser.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .active(true)
                .roles(List.of(adminRole, userRole))
                .build());

            appUserRepository.save(AppUser.builder()
                .username("user")
                .password(passwordEncoder.encode("user123"))
                .active(true)
                .roles(List.of(userRole))
                .build());

            System.out.println("=== UTILISATEURS CRÉÉS ===");
            System.out.println("admin/admin123 avec rôles ADMIN, USER");
            System.out.println("user/user123 avec rôle USER");
        };
    }
}
