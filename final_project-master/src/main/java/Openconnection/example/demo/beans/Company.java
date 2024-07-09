package Openconnection.example.demo.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "companies")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    @Email
    @Column(nullable = false, unique = true, length = 40)
    private String email;

    @Column(nullable = false, length = 40)
    private String password;

    @Singular
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "companyId")
    @JsonIgnore
    private List<Coupon> coupons;

}
