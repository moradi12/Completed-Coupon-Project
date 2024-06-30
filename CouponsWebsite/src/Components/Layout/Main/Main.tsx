import { useEffect, useState } from "react";
import axios from "axios";
import { Coupon } from "../../Models/Coupon";
import { SingleCoupon } from "../../Pages/SingleCoupon/SingleCoupon";
import "./Main.css";

export function Main(): JSX.Element {
    return (
        <div className="Main">
            <h1></h1>
            <AllCoupons />
        </div>
    );
}
export function AllCoupons(): JSX.Element {
    const [coupons, setCoupons] = useState<Coupon[]>([]);
    useEffect(() => {
        axios.get("http://localhost:8080/companies/allCoupons")
            .then(response => response.data)
            .then(data => setCoupons(data))
            .catch(error => {
                console.error(error);
            });
    }, []);

    return (
        <div className="AllCoupons">
            <div className="coupon-list">
                {coupons.map(coupon => (
                    <SingleCoupon key={coupon.id} coupon={coupon} />
                ))}
            </div>
        </div>
    );
}
