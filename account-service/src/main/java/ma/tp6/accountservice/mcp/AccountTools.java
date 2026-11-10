package ma.tp6.accountservice.mcp;

import lombok.RequiredArgsConstructor;
import ma.tp6.accountservice.clients.CustomerRestClient;
import ma.tp6.accountservice.entities.BankAccount;
import ma.tp6.accountservice.enums.AccountType;
import ma.tp6.accountservice.repositories.BankAccountRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountTools {
    private final BankAccountRepository accountRepository;
    private final CustomerRestClient customerRestClient;

    @Tool(description = "List all bank accounts with their owner")
    public List<BankAccount> getAllAccounts() {
        List<BankAccount> accounts = accountRepository.findAll();
        accounts.forEach(a -> a.setCustomer(customerRestClient.findCustomerById(a.getCustomerId())));
        return accounts;
    }

    @Tool(description = "Get one bank account by its account id")
    public BankAccount getAccountById(@ToolParam(description = "The account id (UUID)") String accountId) {
        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setCustomer(customerRestClient.findCustomerById(account.getCustomerId()));
        return account;
    }

    @Tool(description = "List all bank accounts of one customer")
    public List<BankAccount> getAccountsByCustomerId(@ToolParam(description = "The customer id") Long customerId) {
        List<BankAccount> accounts = accountRepository.findByCustomerId(customerId);
        accounts.forEach(a -> a.setCustomer(customerRestClient.findCustomerById(customerId)));
        return accounts;
    }

    @Tool(description = "Create a new bank account for a customer")
    public BankAccount createAccount(
            @ToolParam(description = "The initial balance") double balance,
            @ToolParam(description = "The currency, for example MAD") String currency,
            @ToolParam(description = "The account type: CURRENT_ACCOUNT or SAVING_ACCOUNT") String type,
            @ToolParam(description = "The owner customer id") Long customerId) {
        return accountRepository.save(BankAccount.builder()
                .balance(balance)
                .currency(currency)
                .type(AccountType.valueOf(type))
                .customerId(customerId)
                .createdAt(LocalDate.now())
                .build());
    }
}
