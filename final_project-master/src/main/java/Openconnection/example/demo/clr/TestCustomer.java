package Openconnection.example.demo.clr;

import Openconnection.example.demo.Service.CompanyService;
import Openconnection.example.demo.Service.CouponService;
import Openconnection.example.demo.Service.CustomerService;
import Openconnection.example.demo.beans.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;


@RequiredArgsConstructor
@Component
@Order(2)
public class TestCustomer implements CommandLineRunner {
    private final CustomerService customerService;
    private final CompanyService companyService;
    private final CouponService couponService;

    @Override
    public void run(String... args) throws Exception {
        try {

            System.out.println("Adding customer 1...\n");
            Customer customer1 = Customer.builder()
                    .customerID(1)
                    .firstName("idanidan")
                    .lastName("idanidan")
                    .email("idanidan@idanidan.com")
                    .password("idanidan")
                    .build();
            customerService.addCustomer(customer1);
            System.out.println("Customer 1 added successfully.\n");

            Customer customer3 = Customer.builder()
                    .customerID(3)
                    .firstName("Alice")
                    .lastName("Johnson")
                    .email("alice@mail.com")
                    .password("password789")
                    .build();
            customerService.addCustomer(customer3);
            System.out.println("Customer 2 added successfully.\n");

            Customer customer4 = Customer.builder()
                    .customerID(4)
                    .firstName("Ofir")
                    .lastName("Cool")
                    .email("ofir@example.com")
                    .password("ofirPassword")
                    .build();
            customerService.addCustomer(customer4);
            System.out.println("Customer 3 added successfully.\n");

            Customer customer5 = Customer.builder()
                    .customerID(5)
                    .firstName("Danit")
                    .lastName("DanitD")
                    .email("Danit@example.com")
                    .password("Danit212121")
                    .build();
            customerService.addCustomer(customer5);
            System.out.println("Customer 4 added successfully.\n");

            Customer customer6 = Customer.builder()
                    .customerID(6)
                    .firstName("Ben")
                    .lastName("Mocher")
                    .email("Ben@example.com")
                    .password("Ben333ew")
                    .build();
            customerService.addCustomer(customer6);
            System.out.println("Customer 5 added successfully.\n");


            System.out.println("========== Printing all customers ==========\n");
            couponService.getAllCoupons().forEach(coupon -> System.out.println(coupon + "\n"));
            // Printing all customers
            System.out.println("All Customers after adding");
            customerService.getAllCustomers().forEach(System.out::println);

            // Deleting a customer with ID 2
            System.out.println("Deleting customer with ID 2");
            //customerService.deleteCustomer(2);
            System.out.println("Remaining Customers after deletion");
            customerService.getAllCustomers().forEach(System.out::println);

            System.out.println("got into the delete section");
            customerService.deleteCustomer(5);
            System.out.println("Customer with id " + 3 + " deleted successfully.");


            couponService.addCouponPurchase(1, 1);
            couponService.addCouponPurchase(2, 2);
            couponService.addCouponPurchase(3, 3);
            couponService.addCouponPurchase(3, 2);
            couponService.addCouponPurchase(4, 4);
            couponService.addCouponPurchase(5, 5);
            couponService.addCouponPurchase(5, 5);

            System.out.println("========== Printing all customers after Delete ==========\n");
            customerService.getAllCustomers().forEach(System.out::println);


        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }
}
