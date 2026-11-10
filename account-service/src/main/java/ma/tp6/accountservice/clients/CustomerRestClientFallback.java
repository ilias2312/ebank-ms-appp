package ma.tp6.accountservice.clients;

import ma.tp6.accountservice.model.Customer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerRestClientFallback implements CustomerRestClient {
    @Override
    public Customer findCustomerById(Long id) {
        return new Customer(id, "Not available", "Not available");
    }

    @Override
    public List<Customer> allCustomers() {
        return List.of();
    }
}
