import React, { FormEvent, useState, useEffect } from "react";
import axios from "axios";
import { notify } from "../../Utils/notif";
import './AddCustomerAdmin.css';
import { useNavigate } from "react-router-dom";
import axiosJWT from "../../Utils/axiosJWT"; // Import axiosJWT instance
import { couponSystem } from "../../Redux/Store";
import { UserType } from "../../Models/UserType";

const urlLink = "http://localhost:8080/api/auth/register";

const AddCustomerAdmin = () => {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [emailInUse, setEmailInUse] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    if(couponSystem.getState().auth.userType !== UserType.COMPANY){
      navigate("/all");
      notify.error("You are not authorized to view this page.");

    const checkEmailInUse = async (email: string) => {
      console.log("Checking if email is in use:", email);
      try {
        const [customersResponse, companiesResponse] = await Promise.all([
          axios.get("http://localhost:8080/api/admin/customers"),
          axios.get("http://localhost:8080/api/customer/all"),
        ]);

        const customers = customersResponse.data;
        const companies = companiesResponse.data;

        const isEmailInUse = customers.some((customer: any) => customer.email === email) || 
          companies.some((company: any) => company.email === email);

        console.log("Email in use:", isEmailInUse);
        setEmailInUse(isEmailInUse);
      } catch (error) {
        console.error("Error checking email:", error);
        setError("An error occurred while checking the email. Please try again.");
        notify.error("An error occurred while checking the email. Please try again.");
        setEmailInUse(true);
      }
    };

    if (email.length >= 5) {
      checkEmailInUse(email);
    }
}}, [email]);

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    console.log("Submitting customer registration form");

    if (password.length < 5) {
      setError("Password must be at least 5 characters long");
      return;
    }

    if (password !== confirmPassword) {
      setError("Passwords do not match");
      return;
    }

    if (email.length < 5) {
      setError("Email must be at least 5 characters long");
      return;
    }

    if (firstName.length < 2 || lastName.length < 2) {
      setError("First name and last name must each be at least 2 characters long");
      return;
    }

    if (emailInUse) {
      setError("Email is already in use. Please use a different email.");
      notify.error("Email is already in use. Please use a different email.");
      return;
    }

    const userDetails = {
      email,
      password,
      userType: "CUSTOMER",
      userName: `${firstName}_${lastName}`,
    };

    console.log("User details:", userDetails);

    axiosJWT.post(urlLink, userDetails)
      .then(response => {
        const token = response.data; // Assuming your backend sends back a JWT token directly
        localStorage.setItem("jwtToken", token); // Store token in localStorage
        setSuccess("Registration successful!");
        notify.success("Registration successful!");
        console.log("Registration successful. Token:", token);
        navigate('/login');
      })
      .catch(error => {
        if (axios.isAxiosError(error)) {
          const errorMessage = error.message || "An error occurred. Please try again.";
          setError(errorMessage);
          notify.error(errorMessage);
          console.error("Registration error:", errorMessage);
        } else {
          setError("An error occurred. Please try again.");
          notify.error("An error occurred. Please try again.");
          console.error("Registration error:", error);
        }
      });
  };

  return (
    <form onSubmit={handleSubmit} className="add-customer-admin-form">
      <h1>Register Customer Admin</h1>
      <input
        type="text"
        placeholder="First Name"
        value={firstName}
        onChange={(e) => setFirstName(e.target.value)}
        className="form-input"
      />
      <input
        type="text"
        placeholder="Last Name"
        value={lastName}
        onChange={(e) => setLastName(e.target.value)}
        className="form-input"
      />
      <input
        type="email"
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        className="form-input"
      />
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        className="form-input"
      />
      <input
        type="password"
        placeholder="Confirm Password"
        value={confirmPassword}
        onChange={(e) => setConfirmPassword(e.target.value)}
        className="form-input"
      />
      {error && <p className="form-error">{error}</p>}
      {success && <p className="form-success">{success}</p>}
      <button type="submit" className="form-button">Submit</button>
    </form>
  );
};

export default AddCustomerAdmin;
