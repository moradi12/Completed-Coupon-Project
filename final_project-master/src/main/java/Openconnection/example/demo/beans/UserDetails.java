package Openconnection.example.demo.beans;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserDetails {
    private int userId;
    private String userName;
    private String email;
    private String password;
    private UserType userType;

}
