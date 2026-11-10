package ma.tp6.accountservice.web;

import lombok.RequiredArgsConstructor;
import ma.tp6.accountservice.clients.CustomerRestClient;
import ma.tp6.accountservice.entities.BankAccount;
import ma.tp6.accountservice.repositories.BankAccountRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountRestController {
    private final BankAccountRepository accountRepository;
    private final CustomerRestClient customerRestClient;

    @GetMapping("/accounts")
    public List<BankAccount> accountList() {
        List<BankAccount> accounts = accountRepository.findAll();
        accounts.forEach(a -> a.setCustomer(customerRestClient.findCustomerById(a.getCustomerId())));
        return accounts;
    }

    @GetMapping("/accounts/{id}")
    public BankAccount bankAccountById(@PathVariable String id) {
        BankAccount account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setCustomer(customerRestClient.findCustomerById(account.getCustomerId()));
        return account;
    }

    @GetMapping("/accounts/customer/{customerId}")
    public List<BankAccount> accountsByCustomer(@PathVariable Long customerId) {
        List<BankAccount> accounts = accountRepository.findByCustomerId(customerId);
        accounts.forEach(a -> a.setCustomer(customerRestClient.findCustomerById(customerId)));
        return accounts;
    }

    @PostMapping("/accounts")
    public BankAccount save(@RequestBody BankAccount account) {
        return accountRepository.save(account);
    }
}
