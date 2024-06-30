import React, { useEffect, useState } from "react";
import { Coupon } from "../../Models/Coupon";
import axiosJWT from "../../Utils/axiosJWT";
import { useSelector } from "react-redux";
import { RootState } from "../../Redux/RootState";
import { AxiosError } from "axios";
import { notify } from "../../Utils/notif";
import { SingleCoupon } from "../SingleCoupon/SingleCoupon";
import { checkData } from "../../Utils/checkData";
import { couponSystem } from "../../Redux/Store";
import { useNavigate } from "react-router-dom";
import { UserType } from "../../Models/UserType";
import "./MyCoupons.css"; 
const MyCoupons: React.FC = () => {
    const [coupons, setCoupons] = useState<Coupon[]>([]);
    const [userId, setUserId] = useState("")
    const navigate = useNavigate(); // Hook for navigation

    useEffect(() => {
        const effunction = async () => {
            await checkData();
            console.log(couponSystem.getState().auth);
            if(couponSystem.getState().auth.userType !== UserType.CUSTOMER){
                
                navigate("/all");
                notify.error("You are not authorized to view this page.");
            }
            
            setUserId(couponSystem.getState().auth.id.toString());   
        }
        console.log("test")
        effunction();
        // fetchUserCoupons();
    }, [navigate]);

    const fetchUserCoupons = () => {
        
        axiosJWT.get(`http://localhost:8080/api/customer/${userId}/coupons`)
            .then(response => {
                setCoupons(response.data);
            })
            .catch((error: AxiosError) => {
                notify.error(error.response?.data as string);
            });
    };

    return (
        <div>
            <h1>My Coupons</h1>
            <div>
                {coupons.length === 0 && <p>No coupons found.</p>}
                {coupons.map((coupon) => (
                    <SingleCoupon key={coupon.id} coupon={coupon} />
                ))}
            </div>
        </div>
    );
};

export default MyCoupons;
