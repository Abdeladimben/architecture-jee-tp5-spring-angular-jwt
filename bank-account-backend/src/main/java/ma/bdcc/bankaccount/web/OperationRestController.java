package ma.bdcc.bankaccount.web;

import ma.bdcc.bankaccount.dto.*;
import ma.bdcc.bankaccount.service.BankService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/operations")
@CrossOrigin("*")
public class OperationRestController {
    private BankService bankService;

    public OperationRestController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping("/debit")
    public void debit(@RequestBody DebitDTO dto, Principal principal) {
        bankService.debit(dto.getAccountId(), dto.getAmount(),
            dto.getDescription(), principal.getName());
    }

    @PostMapping("/credit")
    public void credit(@RequestBody CreditDTO dto, Principal principal) {
        bankService.credit(dto.getAccountId(), dto.getAmount(),
            dto.getDescription(), principal.getName());
    }

    @PostMapping("/transfer")
    public void transfer(@RequestBody TransferDTO dto, Principal principal) {
        bankService.transfer(dto.getAccountSource(), dto.getAccountDestination(),
            dto.getAmount(), principal.getName());
    }
}
