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

    ////we cant change to user name
    @Column(nullable = false, unique = true)
    private String name;

    @Email
    @Column(nullable = false, unique = true, length = 40)
    private String email;

    @Column(nullable = false, length = 40)
    private String password;

    @Singular

    ///// TODO IF WE CHANGE THE CASCADE TO REMOVE HE WONT BE ABLE TO DELETE
    ///// TODO BUT IF WE CHANGE IT TO ALL HE WONT SHOW CUSTOMER COUPONS! WE MUST CHOOSE ONLY 1
    // TODO    AND ALSO HE DOSNT DELETE COMPANY OR CUSTOMER FROM THE CODE WITHOUT SWAGGER
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "companyId")
    @JsonIgnore
    private List<Coupon> coupons;

    //  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "companyId")
    //    private List<Coupon> coupons;

//    @Singular
//    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "companyId")
//    private List<Coupon> coupons;
}
