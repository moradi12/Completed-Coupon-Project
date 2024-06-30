package Openconnection.example.demo.Repository;

import Openconnection.example.demo.beans.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Boolean existsByEmail(String email);
    Boolean existsByEmailAndPassword(String email, String password);
    Optional<Customer> findByEmailAndPassword(String email, String password);
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findById(Integer id);

    List<Customer> findByCouponsCompanyId(Integer companyId);}
