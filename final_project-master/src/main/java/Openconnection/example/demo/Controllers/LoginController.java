package Openconnection.example.demo.Controllers;

import Openconnection.example.demo.Exceptions.AdminAlreadyExistsException;
import Openconnection.example.demo.Service.LoginService;
import Openconnection.example.demo.beans.Credentials;
import Openconnection.example.demo.beans.UserDetails;
import Openconnection.example.demo.Exceptions.LoginException;
import Openconnection.example.demo.Exceptions.CustomerExceptionException;
import Openconnection.example.demo.Exceptions.CompanyNotFoundException;
import Openconnection.example.demo.utills.JWT;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class LoginController {
    private final JWT jwt;
    private final LoginService loginService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDetails userDetails) {
        try {

            String token = loginService.register(userDetails);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            return new ResponseEntity<>(token, headers, HttpStatus.CREATED);
        } catch (LoginException | CustomerExceptionException | AdminAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed: " + e.getMessage());
        }
    }
///////todo my login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Credentials credentials)throws LoginException {
        try {
            UserDetails user = loginService.loginUser(credentials);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwt.generateToken(user));
            Map<String,Object> map = new HashMap<>();
            map.put("id",user.getUserId());
            map.put("userName",user.getUserName());
            return new ResponseEntity<>(map, headers, HttpStatus.CREATED);
        } catch (LoginException | CustomerExceptionException | CompanyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
        }
    }


}