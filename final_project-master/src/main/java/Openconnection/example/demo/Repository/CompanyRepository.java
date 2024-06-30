package Openconnection.example.demo.Repository;

import Openconnection.example.demo.beans.Company;
import Openconnection.example.demo.beans.Coupon;
import Openconnection.example.demo.beans.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Boolean existsByEmailAndPassword(String email, String password);
    Optional<Company> findByName(String name);
    Optional<Company> findByEmailAndPassword(String email, String password);
    Boolean existsByEmail(String email);

    Optional<Company> findById(int id);
    Boolean existsByIdNotAndId(int idNot, int id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Coupon c WHERE c.endDate < CURRENT_DATE")
    void deleteExpiredCoupons();
}
