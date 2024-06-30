package Openconnection.example.demo.Service;

import Openconnection.example.demo.Exceptions.*;
import Openconnection.example.demo.Repository.AdminRepository;
import Openconnection.example.demo.Repository.CompanyRepository;
import Openconnection.example.demo.Repository.CouponRepository;
import Openconnection.example.demo.Repository.CustomerRepository;
import Openconnection.example.demo.beans.Admin;
import Openconnection.example.demo.beans.Company;
import Openconnection.example.demo.beans.Coupon;
import Openconnection.example.demo.beans.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    private final AdminRepository adminRepository;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final CouponRepository couponRepository;

    public boolean isAdminExists(String email, String password) {
        return "admin@admin.com".equals(email) && "admin".equals(password);
    }


    public boolean login(String email, String password) throws AdminException {
        if (adminEmail.equals(email) && adminPassword.equals(password)) {
            System.out.println("Login successful!");
            return true;
        } else {
            throw new AdminException("Invalid email or password");
        }
    }

    public void addAdmin(Admin admin) throws AdminAlreadyExistsException {
        if (adminRepository.existsById(admin.getId())) {
            throw new AdminAlreadyExistsException("Admin already exists.");
        }
        adminRepository.save(admin);
    }

    public void addCompany(Company company) throws CompanyAlreadyExistsException {
        if (companyRepository.existsById(company.getId())) {
            throw new CompanyAlreadyExistsException(ErrMsg.COMPANY_ALREADY_EXISTS.getMsg());
        }
        companyRepository.save(company);
    }


    public void updateCompany(Company company) throws CompanyNotFoundException {
        int companyId = company.getId();
        if (!companyRepository.existsById(companyId)) {
            throw new CompanyNotFoundException(ErrMsg.COMPANY_NOT_FOUND.getMsg());
        }
        companyRepository.findById(companyId).ifPresent(tempComp -> {
            tempComp.setEmail(company.getEmail());
            tempComp.setPassword(company.getPassword());
            tempComp.setName(company.getName());
            companyRepository.saveAndFlush(tempComp);
            System.out.println("Company updated: " + tempComp);
        });
    }

    public void deleteCompany(int companyId) {
        if (!companyRepository.existsById(companyId)) {
            throw new AdminException("Company not found");
        }
        companyRepository.deleteById(companyId);
    }


    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }


    public Company getOneCompany(int companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new AdminException("Company not found"));
    }


    public void addCustomer(Customer customer) {
        if (customerRepository.existsById(customer.getCustomerID())) {
            throw new AdminException("Customer with the same ID already exists");
        }
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new AdminException("Customer with the same email already exists");
        }
        customerRepository.save(customer);
    }


    public boolean updateCustomer(Customer customer) {
        int id = customer.getCustomerID();
        if (!customerRepository.existsById(id)) {
            throw new AdminException("Customer not found");
        }
        if (customerRepository.existsByEmail(customer.getEmail()) && !customerRepository.findById(id).get().getEmail().equals(customer.getEmail())) {
            throw new AdminException("Customer with the same email already exists");
        }
        customerRepository.saveAndFlush(customer);
        return true;
    }


    public void deleteCustomer(int customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new AdminException("Customer not found");
        }

        // Delete coupons associated with the customer ID
        couponRepository.deleteCouponsByCustomerId(customerId);

        // Now delete the customer
        customerRepository.deleteById(customerId);

        System.out.println("Customer and associated coupons deleted with ID: " + customerId);
    }




    public void forceDeleteCustomer(int customerId) throws CustomerExceptionException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(ErrMsg.CUSTOMER_NOT_FOUND.getMsg()));
        List<Coupon> customerCoupons = customer.getCoupons();
        for (Coupon coupon : customerCoupons) {
            couponRepository.delete(coupon);
        }
        customerRepository.delete(customer);

        System.out.println("Customer and associated coupons forcefully deleted with ID: " + customerId);
    }


    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }


    public Optional<Customer> getOneCustomer(int customerId) {
        return customerRepository.findById(customerId);
    }
}
