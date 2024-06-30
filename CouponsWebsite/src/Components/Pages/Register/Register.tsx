import React, { FormEvent, useState, useEffect } from "react";
import axios from "axios";
import { notify } from "../../Utils/notif";
import "./Register.css";
import { useNavigate } from "react-router-dom";

const urlLink = "http://localhost:8080/api/auth/register";

export const Register = () => {
  const [userName, setUserName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [userType, setUserType] = useState("CUSTOMER");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    console.log("Submitting registration form");

    if (password.length < 5 || password.length > 20) {
      setError("Password must be between 5 and 20 characters long");
      notify.error("Password must be between 5 and 20 characters long");
      return;
    }

    if (password !== confirmPassword) {
      setError("Passwords do not match");
      notify.error("Passwords do not match");
      return;
    }

    if (email.length < 5 || email.length > 20) {
      setError("Email must be between 5 and 20 characters long");
      notify.error("Email must be between 5 and 20 characters long");
      return;
    }

    if (
      userType === "COMPANY" &&
      (userName.length < 5 || userName.length > 20)
    ) {
      setError("Company name must be between 5 and 20 characters long");
      notify.error("Company name must be between 5 and 20 characters long");
      return;
    }

    if (
      userType === "CUSTOMER" &&
      (firstName.length < 5 ||
        firstName.length > 20 ||
        lastName.length < 5 ||
        lastName.length > 20)
    ) {
      setError(
        "First name and last name must each be between 5 and 20 characters long"
      );
      notify.error(
        "First name and last name must each be between 5 and 20 characters long"
      );
      return;
    }

    const userDetails = {
      email,
      password,
      userType,
      userName: userType === "COMPANY" ? userName : `${firstName}_${lastName}`,
    };

    console.log("User details:", userDetails);

    try {
      const response = await axios.post(urlLink, userDetails);
      const token = response.data; // Assuming your backend sends back a JWT token directly
      localStorage.setItem("jwtToken", token); // Store token in localStorage
      notify.success("Registration successful!");
      console.log("Registration successful. Token:", token);
      navigate("/login");
    } catch (error) {
      if (axios.isAxiosError(error)) {
        setError(error.message || "This email already in use. Please try a.");
        notify.error(error.message || "This email already in use. Please try a.");
        console.error("Registration error:", error.message || "This email already in use. Please try a.");
      } else {
        setError("An error occurred. Please try again.");
        notify.error("An error occurred. Please try again.");
        console.error("Registration error:", error);
      }
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h1>Register</h1>
      <select value={userType} onChange={(e) => setUserType(e.target.value)}>
        <option value="CUSTOMER">Customer</option>
        <option value="COMPANY">Company</option>
        <option value="ADMIN">Admin</option>
      </select>
      {userType === "COMPANY" && (
        <input
          type="text"
          placeholder="Company Name"
          value={userName}
          onChange={(e) => setUserName(e.target.value)}
        />
      )}
      {userType === "CUSTOMER" && (
        <div>
          <input
            type="text"
            placeholder="First Name"
            value={firstName}
            onChange={(e) => setFirstName(e.target.value)}
          />
          <input
            type="text"
            placeholder="Last Name"
            value={lastName}
            onChange={(e) => setLastName(e.target.value)}
          />
        </div>
      )}
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
      <button type="submit">Submit</button>
    </form>
  );
};

export default Register;
