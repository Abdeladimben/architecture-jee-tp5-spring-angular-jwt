package ma.bdcc.bankaccount.web;

import ma.bdcc.bankaccount.dto.CustomerDTO;
import ma.bdcc.bankaccount.service.BankService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin("*")
public class CustomerRestController {
    private BankService bankService;

    public CustomerRestController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping
    public List<CustomerDTO> customers(@RequestParam(name = "keyword", defaultValue = "") String keyword) {
        if (keyword.isEmpty()) {
            return bankService.listCustomers();
        }
        return bankService.searchCustomers(keyword);
    }

    @GetMapping("/{id}")
    public CustomerDTO getCustomer(@PathVariable Long id) {
        return bankService.getCustomer(id);
    }

    @PostMapping
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return bankService.saveCustomer(customerDTO);
    }

    @PutMapping("/{id}")
    public CustomerDTO updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        return bankService.updateCustomer(id, customerDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        bankService.deleteCustomer(id);
    }
}
