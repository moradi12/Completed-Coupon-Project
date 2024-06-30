package Openconnection.example.demo.clr;

import Openconnection.example.demo.Repository.CompanyRepository;
import Openconnection.example.demo.Repository.CouponRepository;
import Openconnection.example.demo.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor
@Order(6)
public class CrudRead implements CommandLineRunner {

    private final CouponRepository couponRepository;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) {
        printAllEntities();
    }

    private void printAllEntities() {
        printAllCompanies();
        printAllCustomers();
        printAllCoupons();
    }

    private void printAllCompanies() {
        System.out.println("========== Printing all companies ==========\n");
        companyRepository.findAll().forEach(System.out::println);
        System.out.println("\n");
    }

    private void printAllCustomers() {
        System.out.println("========== Printing all customers ==========\n");
        customerRepository.findAll().forEach(System.out::println);
        System.out.println("\n");
    }

    private void printAllCoupons() {
        System.out.println("========== Printing all coupons ==========\n");
        couponRepository.findAll().forEach(System.out::println);
        System.out.println("\n");
    }
}
