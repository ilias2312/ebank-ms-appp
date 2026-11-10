package ma.tp6.accountservice.config;

import ma.tp6.accountservice.entities.BankAccount;
import ma.tp6.accountservice.enums.AccountType;
import ma.tp6.accountservice.repositories.BankAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AccountDataLoader implements CommandLineRunner {
    private final BankAccountRepository accountRepository;

    public AccountDataLoader(BankAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(String... args) {
        accountRepository.save(BankAccount.builder().balance(9800).createdAt(LocalDate.now())
                .currency("MAD").type(AccountType.CURRENT_ACCOUNT).customerId(1L).build());
        accountRepository.save(BankAccount.builder().balance(15000).createdAt(LocalDate.now())
                .currency("MAD").type(AccountType.SAVING_ACCOUNT).customerId(1L).build());
        accountRepository.save(BankAccount.builder().balance(4200).createdAt(LocalDate.now())
                .currency("MAD").type(AccountType.CURRENT_ACCOUNT).customerId(2L).build());
    }
}
