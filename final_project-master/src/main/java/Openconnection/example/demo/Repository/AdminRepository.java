package Openconnection.example.demo.Repository;

import Openconnection.example.demo.beans.Admin;
import Openconnection.example.demo.beans.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByEmailAndPassword(String email, String password);

}
