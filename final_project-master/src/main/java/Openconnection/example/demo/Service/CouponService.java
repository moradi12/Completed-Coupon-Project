package Openconnection.example.demo.Service;

import Openconnection.example.demo.Exceptions.CouponOutOfStockException;
import Openconnection.example.demo.Repository.CouponRepository;
import Openconnection.example.demo.Repository.CustomerRepository;
import Openconnection.example.demo.beans.Category;
import Openconnection.example.demo.beans.Coupon;
import Openconnection.example.demo.Exceptions.CouponNotFoundException;
import Openconnection.example.demo.Exceptions.ErrMsg;
import Openconnection.example.demo.beans.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final CustomerRepository customerRepository;


    public Optional<Coupon> getOneCoupon(int couponID) throws CouponNotFoundException {
        Optional<Coupon> coupon = couponRepository.findById(couponID);
        if (coupon == null) {
            throw new CouponNotFoundException(ErrMsg.COUPON_ID_NOT_FOUND.getMsg() + ": " + couponID);
        }
        return coupon;
    }

    public boolean couponExistsByTitleAndCompany(String title, int companyId) {
        boolean exists = couponRepository.existsByTitleAndCompanyId(title, companyId);
        System.out.println("Coupon exists with title: " + title + " and company ID: " + companyId + ": " + exists);
        return exists;
    }


    public void deleteCouponPurchase(int couponID, int customerID) throws CouponNotFoundException {
        Coupon coupon = couponRepository.findById(couponID)
                .orElseThrow(() -> new CouponNotFoundException(ErrMsg.COUPON_NOT_FOUND.getMsg()));
        Optional<Customer> optionalCustomer = customerRepository.findById(customerID);

        if (optionalCustomer.isEmpty()) {
            throw new CouponNotFoundException(ErrMsg.CUSTOMER_NOT_FOUND.getMsg());
        }
        Customer customer = optionalCustomer.get();
        List<Coupon> customerCoupons = customer.getCoupons();
        customerCoupons.removeIf(c -> c.getId() == couponID);
        customer.setCoupons(customerCoupons);
        customerRepository.saveAndFlush(customer);

        System.out.println("Coupon purchase deleted for Customer ID: " + customerID + " for Coupon ID: " + couponID);
    }


    public void saveAndFlush(Coupon coupon) throws CouponNotFoundException {
        couponRepository.saveAndFlush(coupon);
        System.out.println("Coupon saved and flushed: " + coupon);
    }

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public void addCouponPurchase(int couponID, int customerID) throws CouponNotFoundException, CouponOutOfStockException {
        Coupon coupon = couponRepository.findById(couponID)
                .orElseThrow(() -> new CouponNotFoundException(ErrMsg.COUPON_NOT_FOUND.getMsg()));
        if (coupon.getAmount() == 0) {
            throw new CouponOutOfStockException(ErrMsg.COUPON_OUT_OF_STOCK.getMsg());

        }
        coupon.setAmount(coupon.getAmount() - 1);
        couponRepository.saveAndFlush(coupon);
        Optional<Customer> optionalCustomer = customerRepository.findById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CouponNotFoundException(ErrMsg.CUSTOMER_NOT_FOUND.getMsg());
        }
        Customer customer = optionalCustomer.get();
        List<Coupon> customerCoupons = customer.getCoupons();
        customerCoupons.add(coupon);
        customer.setCoupons(customerCoupons);
        customerRepository.saveAndFlush(customer);
        System.out.println("Coupon purchase added for Customer ID: " + customerID + " for Coupon ID: " + couponID);
    }


    public void deleteExpiredCoupons() throws CouponNotFoundException {
        Date currentDate = new Date(System.currentTimeMillis());

        try {
            List<Coupon> expiredCoupons = couponRepository.findByEndDateBefore(currentDate);
            for (Coupon coupon : expiredCoupons) {
                couponRepository.delete(coupon);
                System.out.println("Deleted expired coupon with ID " + coupon.getId());
            }
        } catch (Exception e) {
            throw new CouponNotFoundException("Error deleting expired coupons: " + e.getMessage());
        }
    }

    public List<Coupon> getExpiredCoupons(Date currentDate) throws CouponNotFoundException {
        return couponRepository.findByEndDateBefore(currentDate);
    }


    public List<Coupon> findByPriceBetween(double minPrice, double maxPrice) {
        return couponRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<Coupon> findByCompanyIdAndTitle(int companyId, String title) {
        return couponRepository.findByCompanyIdAndTitle(companyId, title);
    }
}


