import React, { useState, useEffect } from "react";
import axios from "axios";
import { Coupon } from "../../Models/Coupon"; // Adjust the import path as necessary
import "./AllGuestCoupons.css"; // Create and style your component

export function AllGuestCoupons(): JSX.Element {
    const [coupons, setCoupons] = useState<Coupon[]>([]);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        fetchCoupons();
    }, []);

    const fetchCoupons = () => {
        axios.get<Coupon[]>("http://localhost:8080/api/guest/allCoupons")
            .then(response => {
                setCoupons(response.data);
            })
            .catch(error => {
                console.error("Error fetching coupons:", error);
                setError("Failed to fetch coupons");
            });
    };

    return (
        <div className="AllGuestCoupons">
            <h1>Available Coupons</h1>
            {error && <p className="error-message">{error}</p>}
            <div className="coupon-list">
                {coupons.map(coupon => (
                    <div key={coupon.id} className="coupon-item">
                        <h2>{coupon.title}</h2>
                        <p>{coupon.description}</p>
                        <p>Category: {coupon.category}</p>
                        <p>Price: {coupon.price}</p>
                        <p>Expiration Date: {new Date(coupon.endDate).toDateString()}</p>
                    </div>
                ))}
            </div>
        </div>
    );
}
