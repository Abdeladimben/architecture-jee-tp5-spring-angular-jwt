package ma.bdcc.bankaccount.web;

import ma.bdcc.bankaccount.dto.*;
import ma.bdcc.bankaccount.service.BankService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin("*")
public class BankAccountRestController {
    private BankService bankService;

    public BankAccountRestController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping
    public List<BankAccountDTO> listAccounts() {
        return bankService.listAccounts();
    }

    @GetMapping("/{id}")
    public BankAccountDTO getAccount(@PathVariable String id) {
        return bankService.getAccount(id);
    }

    @PostMapping("/current")
    public CurrentAccountDTO saveCurrentAccount(@RequestBody Map<String, Object> data) {
        double initialBalance = Double.parseDouble(data.get("initialBalance").toString());
        double overDraft = Double.parseDouble(data.get("overDraft").toString());
        Long customerId = Long.parseLong(data.get("customerId").toString());
        return bankService.saveCurrentAccount(initialBalance, overDraft, customerId);
    }

    @PostMapping("/saving")
    public SavingAccountDTO saveSavingAccount(@RequestBody Map<String, Object> data) {
        double initialBalance = Double.parseDouble(data.get("initialBalance").toString());
        double interestRate = Double.parseDouble(data.get("interestRate").toString());
        Long customerId = Long.parseLong(data.get("customerId").toString());
        return bankService.saveSavingAccount(initialBalance, interestRate, customerId);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable String id) {
        bankService.deleteAccount(id);
    }

    @GetMapping("/{id}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String id) {
        return bankService.accountHistory(id);
    }
}
