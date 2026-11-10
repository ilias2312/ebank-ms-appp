package ma.tp6.customerservice.mcp;

import lombok.RequiredArgsConstructor;
import ma.tp6.customerservice.entities.Customer;
import ma.tp6.customerservice.services.CustomerService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerTools {
    private final CustomerService customerService;

    @Tool(description = "List all bank customers")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @Tool(description = "Get one bank customer by its id")
    public Customer getCustomerById(@ToolParam(description = "The customer id") Long id) {
        return customerService.getCustomerById(id);
    }

    @Tool(description = "Create a new bank customer")
    public Customer createCustomer(
            @ToolParam(description = "The customer full name") String name,
            @ToolParam(description = "The customer email address") String email) {
        return customerService.saveCustomer(Customer.builder().name(name).email(email).build());
    }
}
