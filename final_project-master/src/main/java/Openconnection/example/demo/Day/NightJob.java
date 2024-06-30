package Openconnection.example.demo.Day;

import Openconnection.example.demo.Repository.CouponRepository;
import Openconnection.example.demo.Service.CouponService;
import Openconnection.example.demo.beans.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NightJob {

    private final CouponRepository couponRepository;
    private final CouponService couponService;

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredCoupons() {
        try {
            Date currentDate = new Date(System.currentTimeMillis());
            List<Coupon> expiredCoupons = couponService.getExpiredCoupons(currentDate);
            System.out.println("Found " + expiredCoupons.size() + " expired coupons");

            for (Coupon coupon : expiredCoupons) {
                couponRepository.delete(coupon);
                System.out.println("Deleted expired coupon with ID " + coupon.getId());
            }
        } catch (Exception e) {
            System.out.println("Error deleting expired coupons: " + e.getMessage());
        }
        System.out.println("Scheduled task executed");
    }
}
