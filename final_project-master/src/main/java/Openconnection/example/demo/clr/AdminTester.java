package Openconnection.example.demo.clr;
import Openconnection.example.demo.Exceptions.CompanyAlreadyExistsException;
import Openconnection.example.demo.Exceptions.CompanyNotFoundException;
import Openconnection.example.demo.Exceptions.CustomerExceptionException;
import Openconnection.example.demo.Service.CompanyService;
import Openconnection.example.demo.Service.CouponService;
import Openconnection.example.demo.Service.CustomerService;
import Openconnection.example.demo.beans.Company;
import Openconnection.example.demo.beans.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.util.List;
@RequiredArgsConstructor
//@Component
@Order(6)
public class AdminTester implements CommandLineRunner {
    private final CustomerService customerService;
    private final CompanyService companyService;
    private final CouponService couponService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(   "\n!!!!!!!!!!!!Admin Tester Testing!!!!!!!!!!!!!!\n");

        try {
            addCompanies();
            printCompanies();
            updateCompany();
            printCompanies();
            addCustomers();
           deleterCustomer(3);
           // deleterCustomer();
            printAllCustomers();
            updateCustomer();
            printAllCustomers();
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    private void addCustomers() throws CustomerExceptionException {
        try {
            Customer customer1 = Customer.builder()
                    .customerID(33)
                    .firstName("adminadding")
                    .lastName("admingggg")
                    .email("addddmingg@gmail.com")
                    .password("Nadav483339")
                    .build();
            customerService.addCustomer(customer1);
            System.out.println("\nAdded customer 1: admin adding admingggg");

            Customer customer2 = Customer.builder()
                    .customerID(34)
                    .firstName("Lord")
                    .lastName("Ring")
                    .email("Ring@gmail.com")
                    .password("tingri3n444g")
                    .build();
            customerService.addCustomer(customer2);
            System.out.println("Added customer 2: Lord Ring");

            Customer customer3 = Customer.builder()
                    .customerID(2)
                    .firstName("Dark")
                    .lastName("Ring")
                    .email("Dark@gmail.com")
                    .password("Dark444444")
                    .build();
            customerService.addCustomer(customer3);
            System.out.println("Added customer 3 : Dark Ring ");
        } catch (CustomerExceptionException e) {
            System.out.println("Error adding customer: " + e.getMessage());
        }
    }

    private void addCompanies() {
        try {
            companyService.addCompany(Company.builder()
                    .id(88)
                    .name("Davidof")
                    .email("AAA@email.com")
                    .password("AAAeeeee")
                    .build());
            System.out.println("Added company 1 by admin!");
        } catch (CompanyAlreadyExistsException e) {
            System.out.println("Company already exists: " + e.getMessage());
        }

        try {
            companyService.addCompany(Company.builder()
                    .id(55)
                    .name("qwaaer")
                    .email("rewq@email.com")
                    .password("qweeeewewe")
                    .build());
            System.out.println("Added company 2 by admin!");
        } catch (CompanyAlreadyExistsException e) {
            System.out.println("Error adding company: " + e.getMessage());
        }    printCompanies();
    }

    private void updateCompany() {
        try {
            System.out.println("\nUpdating company by admin~");
            companyService.updateCompany(Company.builder()
                    .id(1)
                    .name("admin32")
                    .email("admin@gmail.com")
                    .password("eeee334434")
                    .build());
            System.out.println("Updated company with ID 1");
        } catch (CompanyNotFoundException e) {
            System.out.println("Error updating company: " + e.getMessage());
        }
    }
    private void printCompanies() {
        List<Company> companiesAfterOperation = companyService.getAllCompanies();
        System.out.println("\nPrinting companies:");
        companiesAfterOperation.forEach(System.out::println);
    }

    private void updateCustomer() throws CustomerExceptionException {
        try {
            customerService.updateCustomer(Customer.builder()
                    .customerID(2)
                    .firstName("Updated")
                    .lastName("Updatedddddd")
                    .email("workplease@gmail.com")
                    .password("1214444442")
                    .build());
            System.out.println("\nUpdated customer with ID 1");
        } catch (CustomerExceptionException e) {
            System.out.println("Error updating customer: " + e.getMessage());
        }
    }

    private void deleterCustomer(int customerId) {
        try {
            customerService.deleteCustomer(customerId);
            System.out.println("\nDeleted customer with ID: " + customerId);
        } catch (CustomerExceptionException e) {
            System.out.println("Error deleting customer: " + e.getMessage());
        }
    }
        private void printAllCustomers() {
        System.out.println("\nPrinting all customers:");
        List<Customer> customersAfterOperation = customerService.getAllCustomers();
        customersAfterOperation.forEach(System.out::println);
    }
}
