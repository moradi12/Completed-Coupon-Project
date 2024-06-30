package Openconnection.example.demo.clr;

import Openconnection.example.demo.Service.LoginService;
import Openconnection.example.demo.beans.UserDetails;
import Openconnection.example.demo.beans.UserType;
import Openconnection.example.demo.utills.JWT;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


//@Component
@Order(1)
@RequiredArgsConstructor
public class LoginTester implements CommandLineRunner {
    private final LoginService loginService;
    private final JWT jwt;
    @Override
    public void run(String... args) throws Exception {

        //UserDetails userDetails = new UserDetails("Customer@customer.com","password", UserType.CUSTOMER);
        //UserDetails userDetails2 = new UserDetails("Company@Company.com","CompanyPassowrd", UserType.COMPANY);

//        String customerToken = loginService.login(userDetails.getEmail(), userDetails.getPassword());
//        String companyToken = loginService.login(userDetails2.getEmail(), userDetails2.getPassword());
//
//        System.out.println("Customer Token: " + customerToken);
//        System.out.println("Company Token: " + companyToken);
    }
}
