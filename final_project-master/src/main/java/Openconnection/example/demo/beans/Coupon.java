package Openconnection.example.demo.beans;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Collection;

@Entity
@Table(name = "coupons")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Column(nullable = false)
    private int companyId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String title;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(nullable = false)
    @Min(0)
    private int amount;

    @Column(nullable = false)
    @Min(0)
    private double price;

    @Column(name = "discount_percentage", nullable = false)
    @Min(0)
    private double discountPercentage;

    private int available;
    @Column(nullable = false)
    private boolean uniteAvailableSql;


    private String image;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    }

   ///id: number;
//    name: ;
//    description: ;
//    companyId: ;
//    category: ;
//    title: ;
//    startDate: ;
//    endDate: ;
//    price: ;
//    discountPercentage: ;
//    amount: ;
//    available: ;
//    image: ;