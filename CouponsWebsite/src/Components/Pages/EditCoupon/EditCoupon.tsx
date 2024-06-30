import React, { useEffect, useState } from "react";
import { useForm, SubmitHandler } from "react-hook-form";
import axiosJWT from "../../Utils/axiosJWT"; // Import axiosJWT instance
import { Coupon } from "../../Models/Coupon";
import "./EditCoupon.css";
import { UserType } from "../../Models/UserType";
import { checkData } from "../../Utils/checkData"; // Import checkData function
import { useNavigate, useParams } from "react-router-dom";
import { couponSystem } from "../../Redux/Store";
import { notify } from "../../Utils/notif";

interface Company {
    id: number;
    name: string;
}

export function EditCoupon(): JSX.Element {
    const { id } = useParams<{ id: string }>();
    const [companies, setCompanies] = useState<Company[]>([]);
    const [coupon, setCoupon] = useState<Coupon | null>(null);
    const { register, handleSubmit, formState: { errors }, setValue } = useForm<Coupon>();
    const navigate = useNavigate();
    const currentUserType = couponSystem.getState().auth.userType; // Get the user type from the state

    useEffect(() => {
        if (currentUserType !== UserType.COMPANY && currentUserType !== UserType.ADMIN) {
            navigate("/all");
            notify.error("You are not authorized to view this page.");
            return;
        }

        checkData();
        fetchData();
    }, [currentUserType]);

    const fetchData = () => {
        axiosJWT.get<Company[]>("http://localhost:8080/companies/all")
            .then(response => {
                setCompanies(response.data);
            })
            .catch(error => console.error("Error fetching companies:", error));

        axiosJWT.get<Coupon>(`http://localhost:8080/coupons/${id}`)
            .then(response => {
                const couponData = response.data;
                setCoupon(couponData);
                // Set form values
                setValue("title", couponData.title);
                setValue("description", couponData.description);
                setValue("category", couponData.category);
                setValue("price", couponData.price);
                setValue("discountPercentage", couponData.discountPercentage);
                setValue("amount", couponData.amount);
                setValue("image", couponData.image);
                setValue("companyId", couponData.companyId);
                setValue("available", couponData.available);
            })
            .catch(error => console.error("Error fetching coupon:", error));
    };

    const updateCoupon: SubmitHandler<Coupon> = (data) => {
        axiosJWT.put(`http://localhost:8080/coupons/${id}`, data)
            .then(response => {
                console.log("Coupon updated successfully:", response.data);
                navigate("/all");
            })
            .catch(error => {
                console.error("Error updating coupon:", error);
            });
    };

    const categories = [
        'Food', 'Electronics', 'Restaurant', 'Vacation', 'Computer', 'Health', 'Travel', 'Clothing'
    ];

    return (
        <div className="edit-coupon">
            <h2>Edit Coupon</h2>
            {coupon ? (
                <form onSubmit={handleSubmit(updateCoupon)}>
                    <div className="form-group">
                        <label htmlFor="title">Title:</label>
                        <input id="title" {...register("title", { required: "Title is required" })} defaultValue={coupon.title} />
                        {errors.title && <span className="error">{errors.title.message}</span>}
                    </div>

                    <div className="form-group">
                        <label htmlFor="description">Description:</label>
                        <textarea id="description" {...register("description", { required: "Description is required" })} defaultValue={coupon.description} />
                        {errors.description && <span className="error">{errors.description.message}</span>}
                    </div>

                    <div className="form-group">
                        <label htmlFor="category">Category:</label>
                        <select id="category" {...register("category", { required: "Category is required" })} defaultValue={coupon.category}>
                            <option value="">Select a category</option>
                            {categories.map(category => (
                                <option key={category} value={category}>{category}</option>
                            ))}
                        </select>
                        {errors.category && <span className="error">{errors.category.message}</span>}
                    </div>

                    <div className="form-group">
                        <label htmlFor="startDate">Start Date:</label>
                        <input type="date" id="startDate" {...register("startDate", { required: "Start date is required" })} defaultValue={coupon.startDate.toISOString().slice(0, 10)} />
                        {errors.startDate && <span className="error">{errors.startDate.message}</span>}
                    </div>

                    <div className="form-group">
                        <label htmlFor="endDate">End Date:</label>
                        <input type="date" id="endDate" {...register("endDate", { required: "End date is required" })} defaultValue={coupon.endDate.toISOString().slice(0, 10)} />
                        {errors.endDate && <span className="error">{errors.endDate.message}</span>}
                    </div>

                    <div className="form-group">
                        <label htmlFor="price">Price:</label>
                        <input type="number" step="0.01" id="price" {...register("price", { required: "Price is required" })} defaultValue={coupon.price} />
                        {errors.price && <span className="error">{errors.price.message}</span>}
                    </div>

                    <div className="form-group">
                        <label htmlFor="discountPercentage">Discount Percentage:</label>
                        <input type="number" step="0.01" id="discountPercentage" {...register("discountPercentage", { required: "Discount percentage is required" })} defaultValue={coupon.discountPercentage} />
                        {errors.discountPercentage && <span className="error">{errors.discountPercentage.message}</span>}
                    </div>

                    <div className="form-group">
                        <label htmlFor="amount">Amount:</label>
                        <input type="number" id="amount" {...register("amount", { required: "Amount is required", min: 1 })} defaultValue={coupon.amount} />
                        {errors.amount && <span className="error">{errors.amount.message}</span>}
                    </div>

                    <div className="form-group">
                        <label htmlFor="image">Image URL:</label>
                        <input id="image" {...register("image", { required: "Image URL is required" })} defaultValue={coupon.image} />
                        {errors.image && <span className="error">{errors.image.message}</span>}
                    </div>

                    {(currentUserType === UserType.ADMIN || currentUserType === UserType.COMPANY) && (
                        <div className="form-group">
                            <label htmlFor="companyId">Company:</label>
                            <select id="companyId" {...register("companyId", { required: "Company is required" })} defaultValue={coupon.companyId}>
                                {companies.map(company => (
                                    <option key={company.id} value={company.id}>{company.name}</option>
                                ))}
                            </select>
                            {errors.companyId && <span className="error">{errors.companyId.message}</span>}
                        </div>
                    )}

                    <div className="form-group">
                        <label htmlFor="available">Available:</label>
                        <input type="checkbox" id="available" {...register("available")} defaultChecked={coupon.available} />
                    </div>

                    <button type="submit">Update Coupon</button>
                </form>
            ) : (
                <p>Loading...</p>
            )}
        </div>
    );
}
