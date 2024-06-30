package Openconnection.example.demo.clr;

import Openconnection.example.demo.Exceptions.CompanyAlreadyExistsException;
import Openconnection.example.demo.Exceptions.CompanyNotFoundException;
import Openconnection.example.demo.Exceptions.CouponNotFoundException;
import Openconnection.example.demo.Exceptions.ErrMsg;
import Openconnection.example.demo.Service.CompanyService;
import Openconnection.example.demo.Service.CouponService;
import Openconnection.example.demo.beans.Category;
import Openconnection.example.demo.beans.Company;
import Openconnection.example.demo.beans.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.sql.Date;

//TODO ; DELETE IS NOT WORKING FROM SWAGGER FOR SOME REASON

@RequiredArgsConstructor
@Component
@Order(1)
public class TestCompany implements CommandLineRunner {

    private final CompanyService companyService;
    private final CouponService couponService;

    @Override
    public void run(String... args) throws Exception {
        try {
            System.out.println("Adding predefined companies and coupons to the database...\n");

            Company company1 = Company.builder()
                    .id(1)
                    .name("company1")
                    .email("company@1.com")
                    .password("company1")
                    .build();
            companyService.addCompany(company1);
            System.out.println("Company 1 added successfully\n");

            Company company2 = Company.builder()
                    .id(2)
                    .name("Tom Company")
                    .email("Tom@email.com")
                    .password("Tompassword32")
                    .build();
            companyService.addCompany(company2);
            System.out.println("Company 2 added successfully\n");

            Company company3 = Company.builder()
                    .id(3)
                    .name("Shani company")
                    .email("shani@email.com")
                    .password("shani321")
                    .build();
            companyService.addCompany(company3);
            System.out.println("Company 3 added successfully\n");

            Company company4 = Company.builder()
                    .id(4)
                    .name("Farkash Company")
                    .email("Frash@email.com")
                    .password("Farkash321")
                    .build();
            companyService.addCompany(company4);
            System.out.println("Company 4 added successfully.\n");

            Company company5 = Company.builder()
                    .id(5)
                    .name("LP")
                    .email("LP@email.com")
                    .password("LPpassword321")
                    .build();
            companyService.addCompany(company5);
            System.out.println("Company 5 added successfully\n");


            System.out.println("Adding coupons...\n");
            Coupon coupon1 = Coupon.builder()
                    .Id(1)
                    .companyId(1)
                    .category(Category.Food)
                    .description("50% off on Food")
                    .title("Food Sale")
                    .startDate(Date.valueOf("2024-03-12"))
                    .endDate(Date.valueOf("2024-03-31"))
                    .available(30)
                    .uniteAvailableSql(true)
                    .discountPercentage(50)
                    .amount(100)
                    .price(50.0)
                    .image("electronics_sale.jpg")
                    .build();

            Coupon coupon2 = Coupon.builder()
                    .Id(2)
                    .companyId(2)
                    .category(Category.Vacation)
                    .description("10% off on summer vacation packages")
                    .title("Summer Vacation Deal")
                    .startDate(Date.valueOf("2024-06-01"))
                    .endDate(Date.valueOf("2024-01-01"))
                    .available(2)
                    .uniteAvailableSql(true)
                    .discountPercentage(10)
                    .amount(50)
                    .price(200.0)
                    .image("summer_vacation.jpg")
                    .build();

            Coupon coupon3 = Coupon.builder()
                    .Id(3)
                    .companyId(3)
                    .category(Category.Health)
                    .description("Get 1 month free on fitness membership")
                    .title("Fitness Membership Offer")
                    .startDate(Date.valueOf("2024-03-15"))
                    .endDate(Date.valueOf("2024-04-15"))
                    .available(10)
                    .uniteAvailableSql(true)
                    .discountPercentage(20)
                    .amount(20)
                    .price(75.0)
                    .image("fitness_membership.jpg")
                    .build();

            Coupon coupon4 = Coupon.builder()
                    .Id(4)
                    .companyId(4)
                    .uniteAvailableSql(true)
                    .category(Category.Travel)
                    .description("Get 3 months free on any travel package")
                    .title("Special Travel Package Offer")
                    .startDate(Date.valueOf("2024-03-25"))
                    .endDate(Date.valueOf("2024-06-25"))
                    .amount(40)
                    .available(0)
                    .uniteAvailableSql(false)
                    .discountPercentage(60)
                    .price(100.0)
                    .image("special_travel_package.jpg")
                    .build();

            Coupon coupon5 = Coupon.builder()
                    .Id(5)
                    .companyId(5)
                    .category(Category.Clothing)
                    .description("Buy one, get one free on all clothing items")
                    .title("Fashion Deal")
                    .startDate(Date.valueOf("2024-04-01"))
                    .endDate(Date.valueOf("2024-04-30"))
                    .amount(200)
                    .price(0.0)
                    .image("fashion_deal.jpg")
                    .available(33)
                    .uniteAvailableSql(true)
                    .discountPercentage(60)

                    .build();


            companyService.addCoupon(coupon1);
            companyService.addCoupon(coupon2);
            companyService.addCoupon(coupon3);
            companyService.addCoupon(coupon4);
            companyService.addCoupon(coupon5);

            System.out.println("Coupons added successfully\n");

            // Update company
            System.out.println("Updating company...\n");
            Company updatedCompany = Company.builder()
                    .id(2)
                    .email("Thomasnew@mail.com")
                    .name("ThomasGever")
                    .password("newPassword")
                    .build();
            companyService.updateCompany(updatedCompany);
            System.out.println("Company updated successfully\n");

            couponService.getOneCoupon(1);
            couponService.getOneCoupon(2);
            couponService.getOneCoupon(3);
            couponService.getOneCoupon(4);
            couponService.getOneCoupon(5);
            System.out.println(couponService.getOneCoupon(1));
            System.out.println(couponService.getOneCoupon(2));
            System.out.println(couponService.getOneCoupon(3));
            System.out.println(couponService.getOneCoupon(5));

            try {
                System.out.println("Printing Company coupons@##########################");
                System.out.println();
             //   System.out.println(companyService.getOneCompany(1));
                System.out.println();
               // System.out.println("Coupons"+company1);
                System.out.println();


                System.out.println("TIme To Delete Company 4 Not By Force");
                System.out.println();

                int companyIdToDelete = 4; // שנה כאן למספר הרצוי
                try {
                    System.out.println("Attempting to delete company with ID: " + companyIdToDelete);
                    companyService.deleteCompany(companyIdToDelete);
                    System.out.println();

                    System.out.println("Company with ID " + companyIdToDelete + " deleted successfully.");

                } catch (Exception e) {
                    System.out.println("Failed to delete company with ID " + companyIdToDelete + ": " + e.getMessage());
                }

                System.out.println();
                int companyIdToForceDelete = 5;
                System.out.println();
                try {
                    System.out.println("Attempting to forcefully delete company with ID: " + companyIdToForceDelete);
                    companyService.forceDeleteCompany(companyIdToForceDelete);
                    System.out.println("Company with ID " + companyIdToForceDelete + " and all its coupons forcefully deleted.");
                } catch (Exception e) {
                    System.out.println("Failed to forcefully delete company with ID " + companyIdToForceDelete + ": " + e.getMessage());
                }

            } catch (Exception e) {
                System.out.println("Error occurred:" + e.getMessage());
            }
        } catch (CompanyAlreadyExistsException | CouponNotFoundException | CompanyNotFoundException e) {
            String errorMessage;
            if (e instanceof CompanyAlreadyExistsException) {
                errorMessage = ErrMsg.COMPANY_ALREADY_EXISTS.getMsg();
            } else if (e instanceof CouponNotFoundException) {
                errorMessage = ErrMsg.COUPON_NOT_FOUND.getMsg();
            } else {
                errorMessage = ErrMsg.COMPANY_NOT_FOUND.getMsg();
            }
        }
    }
}
