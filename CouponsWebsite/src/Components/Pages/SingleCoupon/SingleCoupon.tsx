import { AxiosError } from "axios";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { Coupon } from "../../Models/Coupon";
import { RootState } from "../../Redux/RootState";
import axiosJWT from "../../Utils/axiosJWT";
import { checkData } from "../../Utils/checkData";
import { notify } from "../../Utils/notif";
import "./SingleCoupon.css";

interface CouponProps {
    coupon: Coupon;
}

export function SingleCoupon(props: CouponProps): JSX.Element {
    const userId = useSelector((state: RootState) => state.auth.id);
    const userType = useSelector((state: RootState) => state.auth.userType);
    const isLogged = useSelector((state: RootState) => state.auth.isLogged);
    const [purchaseStatus, setPurchaseStatus] = useState<string | null>(null);

    useEffect(() => {
        checkData();
    }, []);

    const handlePurchase = () => {
        axiosJWT.post(`http://localhost:8080/api/customer/coupon/purchase/${props.coupon.id}/${userId}`)
            .then(response => {
                setPurchaseStatus("Coupon purchased successfully!");
                notify.success("Coupon purchased successfully!");
            })
            .catch((error: AxiosError) => {
                setPurchaseStatus(error.response?.data as string);
                notify.error(error.response?.data as string);
            });
    };

    // Placeholder function to check if the user has the coupon
    const userHasCoupon = (couponId: number): boolean => {
        // Logic to check if the user has the coupon
        // This might involve an API call or checking a state that keeps track of user's coupons
        return true; // Placeholder value for the purpose of this example
    };

    return (
        <div className="SingleCoupon Box">
            <h1>{props.coupon.title}</h1>
            <hr />
            <img onError={(e) => { e.currentTarget.onerror = null; e.currentTarget.src = 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSwrLMS1VpaC9nqSbV_HndDq5r2ZMmLYR6mUw&s'; }}
            src={props.coupon.image} alt="img"/>
            <p><strong>Coupon ID:</strong> {props.coupon.id}</p>
            <p><strong>Available Amount:</strong> {props.coupon.amount}</p>
            <p><strong>Description:</strong> {props.coupon.description}</p>
            <p><strong>Start Date:</strong> {props.coupon.startDate.toString()}</p>
            <p><strong>End Date:</strong> {props.coupon.endDate.toString()}</p>
            <p><strong>Price:</strong> $ {props.coupon.price}</p>
            <p><strong>Category:</strong> {props.coupon.category}</p>
            {/* {userHasCoupon(props.coupon.id) && (
                <p className="coupon-message">You already have this coupon!</p>
            )} */}
            {isLogged && userType === "CUSTOMER" && (
                <button type="button" onClick={handlePurchase}>Purchase</button>
            )}
            {!isLogged && (
                <p className="login-message">You need to log in to purchase the coupon</p>
            )}
            {purchaseStatus && (
                <p className="purchase-status">{purchaseStatus}</p>
            )}
        </div>
    );
}
