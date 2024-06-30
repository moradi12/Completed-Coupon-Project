import React, { useEffect, useState } from "react";
import { Coupon } from "../../Models/Coupon";
import "./CustomerCoupons.css";
import axiosJWT from "../../Utils/axiosJWT";
import { useSelector, useDispatch } from "react-redux";
import { RootState } from "../../Redux/RootState";
import axios from "axios";
import { notify } from "../../Utils/notif";
import { UserType } from "../../Models/UserType";
import { useNavigate } from "react-router-dom";
import { checkData } from "../../Utils/checkData";
import { jwtDecode } from "jwt-decode";
import { couponSystem } from "../../Redux/Store";

type jwtData = {
    userType: string,
    userName: string,
    id: number,
    sub: string,
    iat: number,
    exp: number
};

export function CustomerCoupons(): JSX.Element {   
    const [coupons, setCoupons] = useState<Coupon[]>([]);
    const [error, setError] = useState<string | null>(null);
    const [filters, setFilters] = useState({ maxPrice: 0, minPrice: 0, maxPriceRange: 0, companyId: 0, title: "" });
    const navigate = useNavigate();

    useEffect(() => {
        checkData();
        const userType = couponSystem.getState().auth.userType;
        console.log(couponSystem.getState().auth);
        if (userType !== UserType.CUSTOMER) {
            notify.error("You are not authorized to view this page.");
            navigate("/all");
            return;
        }
       // fetchCoupons();
    }, []);

    const fetchCoupons = async (url: string = `http://localhost:8080/api/customer/${couponSystem.getState().auth.id}/coupons`) => {
        try {
            const response = await axiosJWT.get(url);
            setCoupons(response.data);
            notify.success("Coupons fetched successfully!");
        } catch (error) {
            const errorMsg = axios.isAxiosError(error) ? error.response?.data?.message || "An error occurred while fetching coupons." : "An error occurred while fetching coupons.";
            setError(errorMsg);
            notify.error(errorMsg);
        }
    };

    const handleFilterChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { id, value } = e.target;
        setFilters(prev => ({ ...prev, [id]: id === "title" ? value : Number(value) }));
    };

    return (
        <div className="CustomerCoupons">
            <h1>Purchased Coupons</h1>
            {error && <p className="error">{error}</p>}
            <div>
                <label htmlFor="maxPrice">Max Price: </label>
                <input type="number" id="maxPrice" value={filters.maxPrice} onChange={handleFilterChange} />
                <button onClick={() => fetchCoupons(`http://localhost:8080/api/customer/${couponSystem.getState().auth.id}/coupons/max-price/${filters.maxPrice}`)}>Fetch by Max Price</button>
            </div>
            <div>
                <label htmlFor="minPrice">Min Price: </label>
                <input type="number" id="minPrice" value={filters.minPrice} onChange={handleFilterChange} />
                <label htmlFor="maxPriceRange">Max Price: </label>
                <input type="number" id="maxPriceRange" value={filters.maxPriceRange} onChange={handleFilterChange} />
                <button onClick={() => fetchCoupons(`http://localhost:8080/api/customer/coupons/price-range/${filters.minPrice}/${filters.maxPriceRange}`)}>Fetch by Price Range</button>
            </div>
            <div>
                {/* <label htmlFor="companyId">Company ID: </label>
                <input type="number" id="companyId" value={filters.companyId} onChange={handleFilterChange} />
              */}
                {/* <label htmlFor="title">Title: </label>
                <input type="text" id="title" value={filters.title} onChange={handleFilterChange} />
                <button onClick={() => fetchCoupons(`http://localhost:8080/api/customer/coupons/company-title`, { companyId: filters.companyId, title: filters.title })}>Fetch by Company and Title</button> */}
            </div>
            <div className="coupons-list">
                {coupons.map(coupon => (
                    <div key={coupon.id} className="coupon-item">
                        <h2>{coupon.title}</h2>
                        <p><strong>ID:</strong> {coupon.id}</p>
                        <p><strong>Description:</strong> {coupon.description}</p>
                        <p><strong>Price:</strong> ${coupon.price}</p>
                        <p><strong>Category:</strong> {coupon.category}</p>
                        <p><strong>Start Date:</strong> {new Date(coupon.startDate).toLocaleDateString()}</p>
                        <p><strong>End Date:</strong> {new Date(coupon.endDate).toLocaleDateString()}</p>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default CustomerCoupons;
