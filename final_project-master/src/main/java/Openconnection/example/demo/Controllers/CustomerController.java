package Openconnection.example.demo.Controllers;

import Openconnection.example.demo.Exceptions.*;
import Openconnection.example.demo.Service.CompanyService;
import Openconnection.example.demo.Service.CouponService;
import Openconnection.example.demo.Service.CustomerService;
import Openconnection.example.demo.beans.*;
import Openconnection.example.demo.utills.JWT;
import io.swagger.annotations.ResponseHeader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@CrossOrigin
public class CustomerController {

    private final CouponService couponService;
    private final CustomerService customerService;
    private  final CompanyService companyService;
    private final JWT JWT;

    @PostMapping("/add")
    public ResponseEntity<?> addCustomer(@RequestHeader("Authorization") String jwt, @PathVariable @RequestBody Customer customer) {
        HttpHeaders headers = JWT.getHeaders(jwt);
        try {
            customerService.addCustomer(customer);
            return new ResponseEntity<>("Customer added successfully", headers, HttpStatus.OK);
        } catch (CustomerExceptionException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/all")
    public List<Coupon> getAllCoupons() {
        return couponService.getAllCoupons();
    }

    @GetMapping("/{customerId}/coupons")
    public ResponseEntity<?> getCustomerCoupons(@RequestHeader("Authorization") String jwt, @PathVariable String customerId) {
        int Id = Integer.parseInt(customerId);
        HttpHeaders headers = JWT.getHeaders(jwt);
        String userJwt = jwt.split(" ")[1];

        if (JWT.getUserType(userJwt).equals(UserType.CUSTOMER.toString())) {
            try {
                List<Coupon> customerCoupons = customerService.getCustomerCoupons(Id);
                return new ResponseEntity<>(customerCoupons, headers, HttpStatus.OK);
            } catch (CustomerNotFoundException e) {
                return new ResponseEntity<>(e.getMessage(), headers, HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                return new ResponseEntity<>("Failed to retrieve customer coupons", headers, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>("Unauthorized access", headers, HttpStatus.FORBIDDEN);
    }

    @PostMapping("/coupon/purchase/{couponID}/{customerID}")
    public ResponseEntity<?> purchaseCoupon(@RequestHeader("Authorization") String jwt, @PathVariable int couponID, @PathVariable int customerID) {
        HttpHeaders headers = JWT.getHeaders(jwt);
        String userJwt = jwt.split(" ")[1];
        if (JWT.getUserType(userJwt).equals(UserType.CUSTOMER.toString())) {

            try {
                customerService.purchaseCoupon(couponID, customerID);
                return new ResponseEntity<>("Coupon purchased successfully", headers, HttpStatus.OK);
            } catch (CouponNotFoundException | CustomerNotFoundException | CouponAlreadyPurchasedException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }
        return new ResponseEntity<>
                ("Failed to retrieve customer coupons", headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/{customerID}/coupons/max-price/{price}")
    public ResponseEntity<?> getAllCustomerCouponsByMaxPrice(
            @RequestHeader("Authorization") String jwt, @PathVariable double price, @PathVariable int customerID) {
        HttpHeaders headers = JWT.getHeaders(jwt);
        String userJwt = jwt.split(" ")[1];

        if (JWT.getUserType(userJwt).equals(UserType.CUSTOMER.toString())) {
            try {
                List<Coupon> coupons = customerService.getAllCustomerCouponsByMaxPrice(customerID, price);
                return new ResponseEntity<>(coupons, headers, HttpStatus.OK);
            } catch (CustomerNotFoundException e) {
                return new ResponseEntity<>(e.getMessage(), headers, HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                return new ResponseEntity<>("Failed to retrieve customer coupons by max price", headers, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>("Unauthorized access", headers, HttpStatus.FORBIDDEN);
    }


    @GetMapping("/coupons/price-range/{minPrice}/{maxPrice}")
    public ResponseEntity<?> getCouponsByPriceRange(
            @RequestHeader("Authorization") String jwt, @PathVariable double minPrice, @PathVariable double maxPrice) {
        HttpHeaders headers = JWT.getHeaders(jwt);
        String userJwt = jwt.split(" ")[1];

        if (JWT.getUserType(userJwt).equals(UserType.CUSTOMER.toString())) {
            try {
                List<Coupon> coupons = couponService.findByPriceBetween(minPrice, maxPrice);
                return new ResponseEntity<>(coupons, headers, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Failed to retrieve coupons by price range", headers, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>("Unauthorized access", headers, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/coupons/company-title")
    public ResponseEntity<?> getCouponsByCompanyAndTitle(
            @RequestHeader("Authorization") String jwt, @RequestParam int companyId, @RequestParam String title) {
        HttpHeaders headers = JWT.getHeaders(jwt);
        String userJwt = jwt.split(" ")[1];

        if (JWT.getUserType(userJwt).equals(UserType.CUSTOMER.toString())) {
            try {
                List<Coupon> coupons = couponService.findByCompanyIdAndTitle(companyId, title);
                return new ResponseEntity<>(coupons, headers, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Failed to retrieve coupons by company and title", headers, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>("Unauthorized access", headers, HttpStatus.FORBIDDEN);
    }
}


