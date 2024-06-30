package Openconnection.example.demo.Controllers;
import Openconnection.example.demo.Service.CouponService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/guest")
public class GuestController {
    private final CouponService couponService;

    @GetMapping("/allCoupons")
    public ResponseEntity<?> getAllCoupons()  {
        return new ResponseEntity<>(couponService.getAllCoupons(), HttpStatus.OK);
    }
}