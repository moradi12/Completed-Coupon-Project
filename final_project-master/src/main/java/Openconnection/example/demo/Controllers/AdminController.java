package Openconnection.example.demo.Controllers;

import Openconnection.example.demo.Exceptions.*;
import Openconnection.example.demo.Service.AdminService;
import Openconnection.example.demo.Service.CompanyService;
import Openconnection.example.demo.Service.CustomerService;
import Openconnection.example.demo.beans.*;
import Openconnection.example.demo.utills.JWT;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;
    private final CompanyService companyService;
    private final CustomerService customerService;
    private final JWT JWT;

    @PostMapping("/admin/{id}")
    public ResponseEntity<String> addAdmin(
            @RequestHeader("Authorization") String jwt, @PathVariable("id") int id, @RequestBody Admin admin) {
        try {
            adminService.addAdmin(admin);
            return ResponseEntity.status(HttpStatus.CREATED).body("Admin added successfully!");
        } catch (AdminAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Admin already exists: " + e.getMessage());
        } catch (AdminException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add admin: " + e.getMessage());
        }
    }

    @PutMapping("/companies")
    public ResponseEntity<String> updateCompany(@RequestHeader("Authorization") String jwt, @RequestBody Company company) throws CompanyNotFoundException {
        HttpHeaders headers = JWT.getHeaders(jwt);
        String userJwt = jwt.split(" ")[1];
        if (JWT.getUserType(userJwt).equals(UserType.ADMIN.toString())) {
            adminService.updateCompany(company);
            return new ResponseEntity<>("Company updated successfully", headers, HttpStatus.OK);
        }
        return new ResponseEntity<>("Forbidden", headers, HttpStatus.FORBIDDEN);
    }


    @DeleteMapping("/companies/{companyId}")
    public ResponseEntity<?> deleteCompany(@RequestHeader("Authorization") String jwt, @PathVariable int companyId) {
        HttpHeaders headers = JWT.getHeaders(jwt);
        String userJwt = jwt.split(" ")[1];
        if (JWT.getUserType(userJwt).equals(UserType.ADMIN.toString())) {
            adminService.deleteCompany(companyId);
            return new ResponseEntity<>(headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(headers, HttpStatus.FORBIDDEN);
    }


    @GetMapping("/companies")
    public ResponseEntity<?> getAllCompanies(@RequestHeader(name = "Authorization") String jwt) {
        HttpHeaders headers = JWT.getHeaders(jwt);
        String userJwt = jwt.split(" ")[1];
        if (JWT.getUserType(userJwt).equals(UserType.ADMIN.toString())) {

            return new ResponseEntity<>(adminService.getAllCompanies(), headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(headers, HttpStatus.FORBIDDEN);


//    @DeleteMapping("/{companyId}")
//    public ResponseEntity<?> deleteCompany(@PathVariable int companyId) {
//        try {
//            companyService.deleteCompany(companyId);
//            return ResponseEntity.ok("Company deleted successfully");
//        } catch (CompanyNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (CouponNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
//        }
//    }

//    @DeleteMapping("/forceDelete/{companyId}")
//    public ResponseEntity<?> forceDeleteCompany(@PathVariable int companyId) {
//        try {
//            companyService.forceDeleteCompany(companyId);
//            return ResponseEntity.ok("Company and all associated coupons forcefully deleted");
//        } catch (CompanyNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//    }
    }


    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return adminService.getAllCustomers();
    }


    @GetMapping("admin/customers")
    public ResponseEntity<?> adminGetAllCustomers(@RequestHeader("Authorization") String jwt) {
        HttpHeaders headers;
        try {
            headers = JWT.getHeaders(jwt);
            String userJwt = jwt.split(" ")[1];
            if (JWT.getUserType(userJwt).equals(UserType.ADMIN.toString())) {
                List<Customer> customers = adminService.getAllCustomers();
                return new ResponseEntity<>(customers, headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Forbidden: Insufficient privileges", headers, HttpStatus.FORBIDDEN);
            }
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("JWT token has expired", HttpStatus.UNAUTHORIZED);
        } catch (SignatureException e) {
            return new ResponseEntity<>("Invalid JWT signature", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching customers", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/companies/{companyId}")
    public ResponseEntity<?> getOneCompany(@RequestHeader("Authorization") String jwt, @PathVariable int companyId) {
        HttpHeaders headers = JWT.getHeaders(jwt);
        String userJwt = jwt.split(" ")[1];
        if (JWT.getUserType(userJwt).equals(UserType.ADMIN.toString())) {
            Company company = adminService.getOneCompany(companyId);
            if (company != null) {
                return new ResponseEntity<>(company, headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Company not found", headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(headers, HttpStatus.FORBIDDEN);
    }

    @PostMapping("/customers")
    public String addCustomer(@RequestHeader("Authorization") String jwt, @RequestBody Customer customer) {
        try {
            adminService.addCustomer(customer);
            return "Customer added successfully!";
        } catch (AdminException e) {
            return e.getMessage();
        }
    }

    @PutMapping("/customers/{customerId}")
    public String updateCustomer(@RequestHeader("Authorization") String jwt, @PathVariable int customerId, @RequestBody Customer customer) {
        try {
            customer.setCustomerID(customerId);
            adminService.updateCustomer(customer);
            return "Customer updated successfully!";
        } catch (AdminException e) {
            return e.getMessage();
        }
    }
/////todo this 1 is working
////    @DeleteMapping("/customers/{customerId}")
////    public ResponseEntity<?> deleteCustomer(@RequestHeader("Authorization") String jwt, @PathVariable int customerId) {
////        HttpHeaders headers = JWT.getHeaders(jwt);
////        try {
////            String userJwt = jwt.split(" ")[1];
////            if (JWT.getUserType(userJwt).equals(UserType.ADMIN.toString())) {
////                adminService.deleteCustomer(customerId);
////                return new ResponseEntity<>("Customer deleted successfully", headers, HttpStatus.OK);
////            } else {
////                return new ResponseEntity<>("Forbidden: Insufficient privileges", headers, HttpStatus.FORBIDDEN);
////            }
////        } catch (AdminException e) {
////            return new ResponseEntity<>(e.getMessage(), headers, HttpStatus.NOT_FOUND);
////        }
//    }

    @DeleteMapping("/customers/{customerId}")
    public ResponseEntity<?> deleteCustomer(@RequestHeader("Authorization") String jwt, @PathVariable int customerId) {
        try {
            String userJwt = jwt.split(" ")[1];
            if (JWT.getUserType(userJwt).equals(UserType.ADMIN.toString())) {
                adminService.deleteCustomer(customerId);
                return ResponseEntity.ok("Customer deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden: Insufficient privileges");
            }
        } catch (AdminException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


//
//    @DeleteMapping("/customers/{customerId}")
//    public ResponseEntity<?> deleteCustomer(@RequestHeader("Authorization") String jwt, @PathVariable int customerId) {
//        HttpHeaders headers = JWT.getHeaders(jwt);
//        String userJwt = jwt.split(" ")[1];
//        if (JWT.getUserType(userJwt).equals(UserType.ADMIN.toString())) {
//            adminService.deleteCustomer(customerId);
//            return new ResponseEntity<>(headers, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(headers, HttpStatus.FORBIDDEN);
//    }


    @GetMapping("/customers/{customerId}")
    public Optional<Customer> getOneCustomer(@RequestHeader("Authorization") String jwt, @PathVariable int customerId) {
        return adminService.getOneCustomer(customerId);
    }


    @PostMapping("/add")
    public ResponseEntity<String> addCompany(@RequestHeader("Authorization") String jwt, @Validated @RequestBody Company company) throws CompanyAlreadyExistsException {
        HttpHeaders headers = JWT.getHeaders(jwt);
        try {
            companyService.addCompany(company);
            return ResponseEntity.status(HttpStatus.CREATED).body("Company added successfully!");
        } catch (CompanyAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Company already exists: " + e.getMessage());
        }
    }


    //    @DeleteMapping("/forceDelete/{companyId}")
//    public ResponseEntity<?> forceDeleteCompany(@PathVariable int companyId) {
//        try {
//            companyService.forceDeleteCompany(companyId);
//            return ResponseEntity.ok("Company and all associated coupons forcefully deleted");
//        } catch (CompanyNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//    }
    @DeleteMapping("/{customerId}")
    public ResponseEntity<String> forceDeleteCustomer(@PathVariable int customerId) {
        try {
            adminService.forceDeleteCustomer(customerId);
            return ResponseEntity.ok("Customer and associated coupons deleted successfully");
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting customer and associated coupons.");
        }
    }
}

/// we have the login in loginController so we dont need it atm
//    @PostMapping("/login")
//    @ResponseStatus(HttpStatus.OK)
//    public String login(@RequestBody UserDetails userDetails) {
//
//
//        if (userDetails.getUserType() == UserType.ADMIN) {
//            return JWT.generateToken(userDetails);
//        } else {
//            return "Unauthorized";
//        }
//    }


//    @PostMapping("/addCompany")
//    public void addCompany(@RequestBody Company company) throws CompanyAlreadyExistsException {
//        adminService.addCompany(company);
//    }

//




