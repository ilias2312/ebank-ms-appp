package ma.tp6.customerservice.web;

import lombok.RequiredArgsConstructor;
import ma.tp6.customerservice.entities.Customer;
import ma.tp6.customerservice.services.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerRestController {
    private final CustomerService customerService;

    @GetMapping("/customers")
    public List<Customer> customerList() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/customers/{id}")
    public Customer getCustomer(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping("/customers")
    public Customer save(@RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }
}
