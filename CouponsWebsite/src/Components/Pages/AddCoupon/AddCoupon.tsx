import React, { useEffect, useState } from "react";
import { useForm, SubmitHandler } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import axiosJWT from "../../Utils/axiosJWT"; // Ensure axiosJWT is correctly imported
import { Coupon } from "../../Models/Coupon";
import "./AddCoupon.css";
import { checkData } from "../../Utils/checkData";
import { couponSystem } from "../../Redux/Store";
import { UserType } from "../../Models/UserType";
import { notify } from "../../Utils/notif";

export function AddCoupon(): JSX.Element {   
    const { register, handleSubmit, formState: { errors } } = useForm<Coupon>();
    const navigate = useNavigate();


    
    useEffect(() => {      
        checkData();    
        if (couponSystem.getState().auth.userType !== UserType.COMPANY) {
            navigate("/all");
            notify.error("You are not authorized to view this page.");
        }
    }, [navigate]);

    const addCoupon: SubmitHandler<Coupon> = (data) => {
        axiosJWT.post(`http://localhost:8080/companies/${couponSystem.getState().auth.id}/coupons`, data)
            .then(response => {
                console.log("Coupon added successfully:", response.data);
                navigate("/all");
            })
            .catch(error => {
                if (error.response) {
                    console.error("Server error:", error.response.data);
                } else if (error.request) {
                    console.error("No response received:", error.request);
                } else {
                    console.error("Error:", error.message);
                }
            });
    };

    const categories = [
        'Food', 'Electronics', 'Restaurant', 'Vacation', 'Computer', 'Health', 'Travel', 'Clothing'
    ];

    return (
        <div className="add-coupon">
            <h2>Add New Coupon</h2>
            <form onSubmit={handleSubmit(addCoupon)}>
                <div className="form-group">
                    <label htmlFor="title">Title:</label>
                    <input id="title" {...register("title", { required: "Title is required" })} />
                    {errors.title && <span className="error">{errors.title.message}</span>}
                </div>

                <div className="form-group">
                    <label htmlFor="description">Description:</label>
                    <textarea id="description" {...register("description", { required: "Description is required" })} />
                    {errors.description && <span className="error">{errors.description.message}</span>}
                </div>

                <div className="form-group">
                    <label htmlFor="category">Category:</label>
                    <select id="category" {...register("category", { required: "Category is required" })}>
                        <option value="">Select a category</option>
                        {categories.map(category => (
                            <option key={category} value={category}>{category}</option>
                        ))}
                    </select>
                    {errors.category && <span className="error">{errors.category.message}</span>}
                </div>

                <div className="form-group">
                    <label htmlFor="startDate">Start Date:</label>
                    <input type="date" id="startDate" {...register("startDate", { required: "Start date is required" })} />
                    {errors.startDate && <span className="error">{errors.startDate.message}</span>}
                </div>

                <div className="form-group">
                    <label htmlFor="endDate">End Date:</label>
                    <input type="date" id="endDate" {...register("endDate", { required: "End date is required" })} />
                    {errors.endDate && <span className="error">{errors.endDate.message}</span>}
                </div>

                <div className="form-group">
                    <label htmlFor="amount">Amount:</label>
                    <input type="number" id="amount" {...register("amount", { required: "Amount is required", min: 1 })} />
                    {errors.amount && <span className="error">{errors.amount.message}</span>}
                </div>

                <div className="form-group">
                    <label htmlFor="price">Price:</label>
                    <input type="number" step="0.01" id="price" {...register("price", { required: "Price is required" })} />
                    {errors.price && <span className="error">{errors.price.message}</span>}
                </div>

                <div className="form-group">
                    <label htmlFor="image">Image URL:</label>
                    <input id="image" {...register("image", { required: "Image URL is required" })} />
                    {errors.image && <span className="error">{errors.image.message}</span>}
                </div>            
                <button type="submit">Add Coupon</button>
            </form>
        </div>
    );
}
