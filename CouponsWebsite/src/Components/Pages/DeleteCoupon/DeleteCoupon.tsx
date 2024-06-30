import React, { useState } from "react";
import axios from "axios";
import "./DeleteCoupon.css";

export function DeleteCoupon(): JSX.Element {
    const [companyId, setCompanyId] = useState("");
    const [couponId, setCouponId] = useState("");
    const [deleteStatus, setDeleteStatus] = useState("");

    const handleDelete = () => {
        if (!companyId || !couponId) {
            setDeleteStatus("Please provide both company ID and coupon ID.");
            return;
        }

        if (!window.confirm("Are you sure you want to delete this coupon?")) {
            return;
        }

        axios.delete(`http://localhost:8080/companies/${companyId}/coupons/${couponId}`)
            .then(() => {
                setDeleteStatus("Coupon deleted successfully.");
                setCouponId("");
            })
            .catch(error => {
                console.error("Error deleting coupon:", error);
                setDeleteStatus("Error deleting coupon. Please try again.");
            });
    };

    return (
        <div className="delete-coupon">
            <h2>Delete Coupon</h2>
            <form>
                <div className="form-group">
                    <label htmlFor="companyId">Company ID:</label>
                    <input
                        type="text"
                        id="companyId"
                        value={companyId}
                        onChange={(e) => setCompanyId(e.target.value)}
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="couponId">Coupon ID:</label>
                    <input
                        type="text"
                        id="couponId"
                        value={couponId}
                        onChange={(e) => setCouponId(e.target.value)}
                    />
                </div>
                <button type="button" onClick={handleDelete}>Delete Coupon</button>
                {deleteStatus && <p className="delete-status">{deleteStatus}</p>}
            </form>
        </div>
    );
}
