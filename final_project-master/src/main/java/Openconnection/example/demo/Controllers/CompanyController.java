package Openconnection.example.demo.Controllers;

import Openconnection.example.demo.Exceptions.*;
import Openconnection.example.demo.Service.CompanyService;
import Openconnection.example.demo.beans.Category;
import Openconnection.example.demo.beans.Company;
import Openconnection.example.demo.beans.Coupon;
import Openconnection.example.demo.beans.UserType;
import Openconnection.example.demo.utills.JWT;
import io.jsonwebtoken.MalformedJwtException;
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
@RequestMapping("/companies")
public class CompanyController {
    private final JWT JWT;
    private final CompanyService companyService;

    @PostMapping("/{id}/coupons")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addCoupon(@RequestHeader("Authorization") String jwt, @Validated @RequestBody Coupon coupon) throws CouponNotFoundException {
        String userJwt = jwt.split(" ")[1];
        HttpHeaders headers = JWT.getHeaders(jwt);
        if (JWT.getUserType(userJwt).equals(UserType.COMPANY.toString())) {
            companyService.addCoupon(coupon);
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(headers, HttpStatus.FORBIDDEN);
    }

    @PutMapping("/coupons/{couponId}")
    public ResponseEntity<?> updateCoupon(@RequestHeader("Authorization") String jwt, @PathVariable int couponId, @Validated @RequestBody Coupon coupon) throws CouponNotFoundException, CompanyNotFoundException {
        String userJwt = jwt.split(" ")[1];
        HttpHeaders headers = JWT.getHeaders(jwt);
        if (JWT.getUserType(userJwt).equals(UserType.COMPANY.toString()) || JWT.getUserType(userJwt).equals(UserType.ADMIN.toString())) {
            companyService.updateCoupon(couponId, coupon);
            return new ResponseEntity<>(headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(headers, HttpStatus.FORBIDDEN);
    }
    @DeleteMapping("/{companyId}/coupons/{couponId}")
    public ResponseEntity<?> deleteCoupon(@RequestHeader("Authorization") String jwt, @PathVariable int companyId, @PathVariable int couponId) throws CouponNotFoundException {
        String userJwt = jwt.split(" ")[1];
        HttpHeaders headers = JWT.getHeaders(jwt);
        if (JWT.getUserType(userJwt).equals(UserType.COMPANY.toString())) {
            companyService.deleteCoupon(couponId);
            return new ResponseEntity<>(headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(headers, HttpStatus.FORBIDDEN);
    }



        @DeleteMapping("/{companyId}")
    public ResponseEntity<?> deleteCompany(@PathVariable int companyId) {
        try {
            companyService.deleteCompany(companyId);
            return ResponseEntity.ok("Company deleted successfully");
        } catch (CompanyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (CouponNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/forceDelete/{companyId}")
    public ResponseEntity<?> forceDeleteCompany(@PathVariable int companyId) {
        try {
            companyService.forceDeleteCompany(companyId);
            return ResponseEntity.ok("Company and all associated coupons forcefully deleted");
        } catch (CompanyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }










    @GetMapping("/{companyId}/coupons/{couponId}")
    public ResponseEntity<?> getCouponById(@RequestHeader("Authorization") String jwt, @PathVariable int companyId, @PathVariable int couponId) {
        try {
            String userJwt = jwt.split(" ")[1];
            HttpHeaders headers = JWT.getHeaders(jwt);
            if (JWT.getUserType(userJwt).equals(UserType.ADMIN.toString()) ||
                    JWT.getUserType(userJwt).equals(UserType.COMPANY.toString())) {
                Coupon coupon = companyService.getCouponById(couponId);
                return new ResponseEntity<>(coupon, headers, HttpStatus.OK);
            }
            return new ResponseEntity<>("Forbidden", headers, HttpStatus.FORBIDDEN);
        } catch (CouponNotFoundException e) {
            return new ResponseEntity<>("Coupon not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Unauthorized: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/allCoupons")
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        try {
            List<Coupon> coupons = companyService.getAllCoupons();
            return ResponseEntity.ok(coupons);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllCompanies() {
        try {
            return ResponseEntity.ok(companyService.getAllCompanies());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving companies: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompany(@RequestHeader("Authorization") String jwt, @PathVariable int id, @RequestBody Company company) {
        try {
            String userJwt = jwt.split(" ")[1];
            HttpHeaders headers = JWT.getHeaders(jwt);
            if (JWT.getUserType(userJwt).equals(UserType.ADMIN.toString())) {
                company.setId(id);
                companyService.updateCompany(company);
                return new ResponseEntity<>(company, headers, HttpStatus.OK);
            }
            return new ResponseEntity<>("Forbidden", headers, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>("Unauthorized: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyById(@RequestHeader("Authorization") String jwt, @PathVariable int id) {
        try {
            String userJwt = jwt.split(" ")[1];
            HttpHeaders headers = JWT.getHeaders(jwt);
            if (JWT.getUserType(userJwt).equals(UserType.ADMIN.toString()) ||
                    JWT.getUserType(userJwt).equals(UserType.COMPANY.toString())) {
                Optional<Company> company = companyService.getOneCompany(id);
                if (company.isPresent()) {
                    return new ResponseEntity<>(company.get(), headers, HttpStatus.OK);
                } else {
                    throw new CompanyNotFoundException("Company not found with id: " + id);
                }
            }
            return new ResponseEntity<>("Forbidden", headers, HttpStatus.FORBIDDEN);
        } catch (CompanyNotFoundException e) {
            return new ResponseEntity<>("Company not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Unauthorized: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{id}/coupons/category/{category}")
    public ResponseEntity<?> getCompanyCouponsByCategory(@RequestHeader("Authorization") String jwt, @PathVariable int id, @PathVariable Category category) throws CompanyNotFoundException {
        String userJwt = jwt.split(" ")[1];
        HttpHeaders headers = JWT.getHeaders(jwt);
        if (JWT.getUserType(userJwt).equals(UserType.COMPANY.toString())) {
            return new ResponseEntity<>(companyService.getCompanyCoupons(id), headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(headers, HttpStatus.FORBIDDEN);
    }


}



//package Openconnection.example.demo.Controllers;
//
//import Openconnection.example.demo.Exceptions.*;
//import Openconnection.example.demo.Service.CompanyService;
//import Openconnection.example.demo.beans.Category;
//import Openconnection.example.demo.beans.Company;
//import Openconnection.example.demo.beans.Coupon;
//import Openconnection.example.demo.beans.UserType;
//import Openconnection.example.demo.utills.JWT;
//import io.jsonwebtoken.MalformedJwtException;
//import lombok.AllArgsConstructor;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@CrossOrigin
//@RestController
//@AllArgsConstructor
//@RequestMapping("/companies")
//public class CompanyController {
//    private final JWT JWT;
//    private final CompanyService companyService;
//
//
//    @PostMapping("/{id}/coupons")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseEntity<?> addCoupon(@RequestHeader("Authorization") String jwt, @Validated @RequestBody Coupon coupon) throws CouponNotFoundException {
//        String userJwt = jwt.split(" ")[1];
//        HttpHeaders headers = JWT.getHeaders(jwt);
//        if (JWT.getUserType(userJwt).equals(UserType.COMPANY.toString())) {
//            companyService.addCoupon(coupon);
//            return new ResponseEntity<>(headers, HttpStatus.CREATED);
//        }
//        return new ResponseEntity<>(headers, HttpStatus.FORBIDDEN);
////    }
//    }
//
//    @PutMapping("/coupon")
//    public ResponseEntity<?> updateCoupon(@RequestHeader("Authorization") String jwt, @Validated @RequestBody Coupon coupon) throws CouponNotFoundException, CompanyNotFoundException {
//        String userJwt = jwt.split(" ")[1];
//        HttpHeaders headers = JWT.getHeaders(jwt);
//        if (JWT.getUserType(userJwt).equals(UserType.COMPANY.toString())) {
//            companyService.updateCoupon(coupon);
//            return new ResponseEntity<>(headers, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(headers, HttpStatus.FORBIDDEN);
//
//
//    }
//
//
//@DeleteMapping("/{companyId}/coupons/{couponId}")
//public ResponseEntity<?> deleteCoupon(@RequestHeader("Authorization") String jwt, @PathVariable int companyId, @PathVariable int couponId) throws CouponNotFoundException {
//    String userJwt = jwt.split(" ")[1];
//    HttpHeaders headers = JWT.getHeaders(jwt);
//    if (JWT.getUserType(userJwt).equals(UserType.COMPANY.toString())) {
//        companyService.deleteCoupon(couponId);
//        return new ResponseEntity<>(headers, HttpStatus.OK);
//    }
//    return new ResponseEntity<>(headers, HttpStatus.FORBIDDEN);
//}
//
//
//    @GetMapping("/{companyId}/coupons/{couponId}")
//    public ResponseEntity<?> getCouponById(@RequestHeader("Authorization") String jwt, @PathVariable int companyId, @PathVariable int couponId) {
//        try {
//            String userJwt = jwt.split(" ")[1];
//            HttpHeaders headers = JWT.getHeaders(jwt);
//            if (JWT.getUserType(userJwt).equals(UserType.ADMIN.toString()) ||
//                    JWT.getUserType(userJwt).equals(UserType.COMPANY.toString())) {
//                Coupon coupon = companyService.getCouponById(couponId);
//                return new ResponseEntity<>(coupon, headers, HttpStatus.OK);
//            }
//            return new ResponseEntity<>("Forbidden", headers, HttpStatus.FORBIDDEN);
//        } catch (CouponNotFoundException e) {
//            return new ResponseEntity<>("Coupon not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            return new ResponseEntity<>("Unauthorized: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
//        }
//    }
//
//
//
//    @GetMapping("/allCoupons/{companyID}")
//    public ResponseEntity<?> getAllCompanyCoupons(@RequestHeader("Authorization") String jwt, @PathVariable int id) throws CouponNotFoundException, CompanyNotFoundException {
//        String userJwt = jwt.split(" ")[1];
//        HttpHeaders headers = JWT.getHeaders(jwt);
//        if (JWT.getUserType(userJwt).equals(UserType.COMPANY.toString())) {
//            return new ResponseEntity<>(companyService.getCompanyCoupons(id), headers, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(headers, HttpStatus.FORBIDDEN);
//    }
//
//    @GetMapping("/allCoupons")
//    public ResponseEntity<List<Coupon>> getAllCoupons() {
//        try {
//            List<Coupon> coupons = companyService.getAllCoupons();
//            return ResponseEntity.ok(coupons);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
////    @GetMapping("/allCoupons")
////    public ResponseEntity<?> getAllCoupons(@RequestHeader("Authorization") String jwt) {
////        try {
////            String userJwt = jwt.split(" ")[1];
////            HttpHeaders headers = JWT.getHeaders(jwt);
////
////            UserType userType = UserType.valueOf(JWT.getUserType(userJwt));
////            if (userType == UserType.ADMIN || userType == UserType.COMPANY) {
////                return new ResponseEntity<>(companyService.getAllCoupons(), headers, HttpStatus.OK);
////            }
////            return new ResponseEntity<>("Forbidden", headers, HttpStatus.FORBIDDEN);
////        } catch (Exception e) {
////            return new ResponseEntity<>("Unauthorized: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
////        }
//
//
//    @GetMapping("/all")
//    public ResponseEntity<?> getAllCompanies() {
//        try {
//            return ResponseEntity.ok(companyService.getAllCompanies());
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error retrieving companies: " + e.getMessage());
//        }
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateCompany(@RequestHeader("Authorization") String jwt, @PathVariable int id, @RequestBody Company company) {
//        try {
//            // Extract and validate the JWT token
//            String userJwt = jwt.split(" ")[1];
//            HttpHeaders headers = JWT.getHeaders(jwt);
//
//            if (JWT.getUserType(userJwt).equals(UserType.ADMIN.toString())) {
//                company.setId(id);
//                companyService.updateCompany(company);
//                return new ResponseEntity<>(company, headers, HttpStatus.OK);
//            }
//            return new ResponseEntity<>("Forbidden", headers, HttpStatus.FORBIDDEN);
//        } catch (Exception e) {
//            return new ResponseEntity<>("Unauthorized: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
//        }
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getCompanyById(@RequestHeader("Authorization") String jwt, @PathVariable int id) {
//        try {
//            String userJwt = jwt.split(" ")[1];
//            HttpHeaders headers = JWT.getHeaders(jwt);
//            if (JWT.getUserType(userJwt).equals(UserType.ADMIN.toString()) ||
//                    JWT.getUserType(userJwt).equals(UserType.COMPANY.toString())) {
//                Optional<Company> company = companyService.getOneCompany(id);
//                if (company.isPresent()) {
//                    return new ResponseEntity<>(company.get(), headers, HttpStatus.OK);
//                } else {
//                    throw new CompanyNotFoundException("Company not found with id: " + id);
//                }
//            }
//            return new ResponseEntity<>("Forbidden", headers, HttpStatus.FORBIDDEN);
//        } catch (CompanyNotFoundException e) {
//            return new ResponseEntity<>("Company not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            return new ResponseEntity<>("Unauthorized: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
//        }
//    }
//
//
//
//
//    @GetMapping("/{id}/coupons/category/{category}")
//    public ResponseEntity<?> getCompanyCouponsByCategory(@RequestHeader("Authorization") String jwt, @PathVariable int id, @PathVariable Category category) throws CompanyNotFoundException {
//        String userJwt = jwt.split(" ")[1];
//        HttpHeaders headers = JWT.getHeaders(jwt);
//        if (JWT.getUserType(userJwt).equals(UserType.COMPANY.toString())) {
//            return new ResponseEntity<>(companyService.getCompanyCoupons(id), headers, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(headers, HttpStatus.FORBIDDEN);
//
//        // done
//    }
//}
//
////
////    @DeleteMapping("/{companyId}")
////    public ResponseEntity<?> deleteCompany(@PathVariable int companyId) {
////        try {
////            companyService.deleteCompany(companyId);
////            return ResponseEntity.ok("Company deleted successfully");
////        } catch (CompanyNotFoundException e) {
////            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
////        } catch (CouponNotFoundException e) {
////            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
////        }
////    }
//
////    @DeleteMapping("/forceDelete/{companyId}")
////    public ResponseEntity<?> forceDeleteCompany(@PathVariable int companyId) {
////        try {
////            companyService.forceDeleteCompany(companyId);
////            return ResponseEntity.ok("Company and all associated coupons forcefully deleted");
////        } catch (CompanyNotFoundException e) {
////            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
////        }
////    }
//
//
//    // @GetMapping("/{id}/coupons")
//    // public List<Coupon> getCompanyCouponsById(@PathVariable int id) throws CompanyNotFoundException {
//    //     return companyService.getCompanyCoupons();
//    // }
//
//
////
////    @PostMapping("/{id}/coupons")
////    @ResponseStatus(HttpStatus.CREATED)
////    public ResponseEntity<?> addCoupon(@RequestHeader("Authorization") String jwt, @Validated @RequestBody Coupon coupon) {
////        try {
////            String userJwt = jwt.split(" ")[1];
////            HttpHeaders headers = JWT.getHeaders(jwt);
////            if (JWT.getUserType(userJwt).equals(UserType.COMPANY.toString())) {
////                companyService.addCoupon(coupon);
////                return new ResponseEntity<>(headers, HttpStatus.CREATED);
////            }
////            return new ResponseEntity<>(headers, HttpStatus.FORBIDDEN);
////        } catch (ArrayIndexOutOfBoundsException | MalformedJwtException e) {
////            return new ResponseEntity<>("Invalid JWT token", HttpStatus.BAD_REQUEST);
////        } catch (CouponNotFoundException e) {
////            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
////        }
////    }
