package ma.tp6.accountservice.clients;

import ma.tp6.accountservice.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "CUSTOMER-SERVICE", fallback = CustomerRestClientFallback.class)
public interface CustomerRestClient {
    @GetMapping("/customers/{id}")
    Customer findCustomerById(@PathVariable("id") Long id);

    @GetMapping("/customers")
    List<Customer> allCustomers();
}
