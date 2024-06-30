import React, { FormEvent, useState, useEffect } from "react";
import axios from "axios";
import { notify } from "../../Utils/notif";
import { Navigate, Route, useNavigate } from "react-router-dom";
import { couponSystem } from "../../Redux/Store";
import { UserType } from "../../Models/UserType";
import { red } from "@mui/material/colors";

const urlLink = "http://localhost:8080/api/auth/register";

const AddCompanyAdmin = () => {
  const [companyName, setCompanyName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [emailInUse, setEmailInUse] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    if(couponSystem.getState().auth.userType !== UserType.ADMIN){
      navigate("/all");
      notify.error("You are not authorized to view this page.");
  }
    const checkEmailInUse = async (email: string) => {
      console.log("Checking if email is in use:", email);
      try {
        const [customersResponse, companiesResponse] = await Promise.all([
          axios.get("http://localhost:8080/api/admin/customers"),
          axios.get("http://localhost:8080/api/customer/all"),
        ]);

        const customers = customersResponse.data;
        const companies = companiesResponse.data;

        const isEmailInUse =
          customers.some((customer: any) => customer.email === email) ||
          companies.some((company: any) => company.email === email);

        console.log("Email in use:", isEmailInUse);
        setEmailInUse(isEmailInUse);
      } catch (error) {
        console.error("Error checking email:", error);
        setError(
          "An error occurred while checking the email. Please try again."
        );
        notify.error(
          "An error occurred while checking the email. Please try again."
        );
        setEmailInUse(true);
      }
    };

    if (email.length >= 5) {
      checkEmailInUse(email);
    }
  }, [email]);

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    console.log("Submitting company registration form");

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

    if (companyName.length < 5) {
      setError("Company name must be at least 5 characters long");
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
      userType: "COMPANY",
      userName: companyName,
    };

    console.log("User details:", userDetails);

    try {
      const response = await axios.post(urlLink, userDetails);
      const token = response.data; // Assuming your backend sends back a JWT token directly
      localStorage.setItem("jwtToken", token); // Store token in localStorage
      setSuccess("Registration successful!");
      notify.success("Registration successful!");
      console.log("Registration successful. Token:", token);
      navigate("/login");
    } catch (error) {
      if (axios.isAxiosError(error)) {
        const errorMessage =
          error.message || "An error occurred. Please try again.";
        setError(errorMessage);
        notify.error(errorMessage);
        console.error("Registration error:", errorMessage);
      } else {
        setError("An error occurred. Please try again.");
        notify.error("An error occurred. Please try again.");
        console.error("Registration error:", error);
      }
    }
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <h1>Register Company Admin</h1>
        <input
          type="text"
          placeholder="Company Name"
          value={companyName}
          onChange={(e) => setCompanyName(e.target.value)}
        />
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <input
          type="password"
          placeholder="Confirm Password"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
        />
        {error && <p style={{ color: "red" }}>{error}</p>}
        {success && <p style={{ color: "green" }}>{success}</p>}
        <button type="submit">Submit</button>
      </form>
      {!(couponSystem.getState().auth.userType === UserType.ADMIN) && (
        <Navigate to={"/all"} />
      )}
    </div>
  );
};

export default AddCompanyAdmin;
