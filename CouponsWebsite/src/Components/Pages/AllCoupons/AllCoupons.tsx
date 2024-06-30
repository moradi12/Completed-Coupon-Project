import React, { useState, useEffect } from "react";
import axios from "axios";
import { Coupon } from "../../Models/Coupon";
import "./AllCoupons.css";
import { couponSystem } from "../../Redux/Store";
import { UserType } from "../../Models/UserType";
import { SingleCoupon } from "../SingleCoupon/SingleCoupon";
import { useNavigate } from "react-router-dom";
import axiosJWT from "../../Utils/axiosJWT";

export function AllCoupons(): JSX.Element {
    const [coupons, setCoupons] = useState<Coupon[]>([]);
    const [customerId, setCustomerId] = useState<number>(1); 
    const [customerCoupons, setCustomerCoupons] = useState<number[]>([]);
    const [purchaseError, setPurchaseError] = useState<string | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        fetchCoupons();
        fetchCustomerCoupons();
    }, []);

    const fetchCoupons = () => {
        axios.get<Coupon[]>("http://localhost:8080/companies/allCoupons")
            .then(response => setCoupons(response.data))
            .catch(error => console.error("Error fetching coupons:", error));
    };

    const fetchCustomerCoupons = () => {
        const authId = couponSystem.getState().auth.id;

        axiosJWT.get<number[]>(`http://localhost:8080/api/customer/${authId}/coupons`)
            .then(response => setCustomerCoupons(response.data))
            .catch(error => console.error("Error fetching customer coupons:", error));
    };

    const handlePurchase = (couponId: number) => {
        const authId = couponSystem.getState().auth.id;

        if (customerCoupons.includes(couponId)) {
            setPurchaseError("You already own this coupon.");
            return;
        }

        axiosJWT.post(`http://localhost:8080/api/customer/coupon/purchase`, {
                couponID: couponId,
                customerID: customerId,
                authId: authId
            })
            .then(() => {
                setPurchaseError(null);
                alert("Coupon purchased successfully!");
                setCustomerCoupons([...customerCoupons, couponId]);
            })
            .catch(error => {
                const errorMessage = error.response?.data ?? "An error occurred while purchasing the coupon.";
                setPurchaseError(`Failed to purchase coupon: ${errorMessage}`);
            });
    };

    const handleEdit = (couponId: number) => {
        navigate(`/editCoupon/${couponId}`);
    };

    return (
        <div className="AllCoupons">
            <h1>Coupons</h1>
            {purchaseError && <p className="error-message">{purchaseError}</p>}
            <div className="coupon-list">
                {coupons.map(coupon => (
                    <div key={coupon.id} className="coupon-item">
                        <h2>{coupon.companyName}</h2>
                        <SingleCoupon coupon={coupon} />
                        <button onClick={() => handleEdit(coupon.id)}>Edit</button>
                        {couponSystem.getState().auth.userType === UserType.CUSTOMER &&
                            !customerCoupons.includes(coupon.id) && (
                                <button onClick={() => handlePurchase(coupon.id)}>Purchase</button>
                            )}
                    </div>
                ))}
            </div>
        </div>
    );
}
