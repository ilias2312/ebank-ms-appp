package ma.tp6.customerservice.config;

import ma.tp6.customerservice.entities.Customer;
import ma.tp6.customerservice.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataLoader implements CommandLineRunner {
    private final CustomerRepository customerRepository;

    public CustomerDataLoader(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) {
        customerRepository.save(Customer.builder().name("Mohamed").email("mohamed@mail.com").build());
        customerRepository.save(Customer.builder().name("Sara").email("sara@mail.com").build());
        customerRepository.save(Customer.builder().name("Yassine").email("yassine@mail.com").build());
    }
}
