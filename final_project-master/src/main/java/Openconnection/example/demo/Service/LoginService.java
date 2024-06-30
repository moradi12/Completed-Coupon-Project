package Openconnection.example.demo.Service;

import Openconnection.example.demo.Exceptions.*;
import Openconnection.example.demo.beans.*;
import Openconnection.example.demo.utills.JWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final CompanyService companyService;
    private final CustomerService customerService;
    private final AdminService adminService;
    private final JWT jwt;
    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;


    public String register(UserDetails user) throws LoginException, CustomerExceptionException, AdminAlreadyExistsException {

        switch (user.getUserType()) {
            case CUSTOMER:
                System.out.println("Registering customer: " + user.getEmail());
                customerService.addCustomer(Customer.builder()
                        .customerID(0)
                        .firstName(user.getUserName().split("_")[0])
                        .lastName(user.getUserName().split("_")[1])
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .build());
                break;
            case COMPANY:
                System.out.println("Registering company: " + user.getEmail());
                companyService.addCompany(Company.builder()
                        .id(0)
                        .name(user.getUserName())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .build());
                break;
            case ADMIN:
                System.out.println("Registering admin: " + user.getEmail());
                adminService.addAdmin(Admin.builder()
                        .id(0)
                        .name(user.getUserName())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .build());
                break;
            default:
                throw new LoginException("Invalid user type");
        }
        String token = jwt.generateToken(user);
        System.out.println("User registered successfully. Token: " + token);
        return token;
    }

    public UserDetails loginUser(Credentials credentials) throws LoginException, CustomerExceptionException, CompanyNotFoundException {
        System.out.println("Attempting to log in with email: " + credentials.getEmail() + ", userType: " + credentials.getUserType());
        UserDetails userDetails;
        switch (credentials.getUserType()) {
            case ADMIN:
                userDetails = validateAdminCredentials(credentials);
                break;
            case CUSTOMER:
                userDetails = validateCustomerCredentials(credentials);
                break;
            case COMPANY:
                userDetails = validateCompanyCredentials(credentials);
                break;
            default:
                System.out.println("Invalid user type.");
                throw new LoginException("Invalid user type");
        }
        System.out.println("User logged in successfully: " + userDetails.getEmail());
        return userDetails;
    }

    private UserDetails validateAdminCredentials(Credentials credentials) throws LoginException {
        if ("admin@admin.com".equals(credentials.getEmail()) && "admin".equals(credentials.getPassword())) {
            return UserDetails.builder()
                    .email(credentials.getEmail())
                    .userName("Admin")
                    .userType(UserType.ADMIN)
                    .userId(1)
                    .password(credentials.getPassword())
                    .build();
        } else {
            throw new LoginException("Invalid admin credentials");
        }
    }

private UserDetails validateCustomerCredentials(Credentials credentials) throws LoginException, CustomerExceptionException {
    System.out.println("Validating customer credentials for: " + credentials.getEmail());
    if (!customerService.isCustomerExists(credentials.getEmail(), credentials.getPassword())) {
        System.out.println("Customer not found or wrong credentials: " + credentials.getEmail());
        throw new LoginException("Login failed: Wrong email or password for customer");
    } else {
        Customer customer = customerService.login(credentials.getEmail(), credentials.getPassword());
        return UserDetails.builder()
                .email(customer.getEmail())
                .userName(customer.getFirstName() + "_" + customer.getLastName())
                .userType(UserType.CUSTOMER)
                .userId(customer.getCustomerID())
                .password(customer.getPassword())
                .build();
    }
}

private UserDetails validateCompanyCredentials(Credentials credentials) throws LoginException, CompanyNotFoundException {
    System.out.println("Validating company credentials for: " + credentials.getEmail());
    if (!companyService.isCompanyExists(credentials.getEmail(), credentials.getPassword())) {
        System.out.println("Company not found or wrong credentials: " + credentials.getEmail());
        throw new LoginException("Login failed: Wrong email or password for company");
    } else {
        Company company = companyService.login(credentials.getEmail(), credentials.getPassword());
        return UserDetails.builder()
                .email(company.getEmail())
                .userName(company.getName() + "_")
                .userType(UserType.COMPANY)
                .userId(company.getId())
                .password(company.getPassword())
                .build();
    }
}

public boolean validateToken(String token) {
    try {
        return jwt.validateToken(token);
    } catch (Exception e) {
        System.out.println("Token validation failed: " + e.getMessage());
        return false;
    }
}

public boolean checkUser(String token, UserType userType) {
    try {
        return jwt.checkUser(token, userType);
    } catch (Exception e) {
        System.out.println("Failed to check user: " + e.getMessage());
        return false;
    }
}
}
