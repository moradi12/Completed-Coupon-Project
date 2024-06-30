import React, { useState } from "react";
import "./Login.css";
import axios, { AxiosError } from "axios";
import { useDispatch } from "react-redux";
import { UserType } from "../../Models/UserType";
import { Credentials } from "../../Models/Credentials ";
import { useNavigate } from "react-router-dom";
import { loginAction } from "../../Redux/AuthReducer";
import { couponSystem } from "../../Redux/Store";

const Login = (props: { setRender: Function }) => {
  const [credentials, setCredentials] = useState<Credentials>({
    email: "",
    password: "",
    userType: UserType.CUSTOMER,
  });
  const [error, setError] = useState<string>("");
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const navigate = useNavigate();
  //const dispatch = useDispatch();

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError("");
    setIsLoading(true);

    console.log("Submitting login request with credentials:", credentials);

    axios
      .post("http://localhost:8080/api/auth/login", credentials)
      .then((res) => {
        const JWT = res.headers["authorization"].split(" ")[1];
        setIsLoading(false);
        console.log("Login successful. Response:", res);
        couponSystem.dispatch(
          loginAction({
            token: JWT,
            email: credentials.email,
            name: res.data.userName,
            userType: credentials.userType,
            isLogged: true,
            id: res.data.id,
          })
        );
        console.log(couponSystem.getState().auth);
        sessionStorage.setItem("jwt", JWT);
        navigate("/all");
      })
      .catch((error: AxiosError) => {
        setIsLoading(false);
        setError("Login failed. Please check your credentials.");
        console.error("Login error:", error.message);
      })
      .finally(() => {
        props.setRender(true);
      });
  };

  return (
    <div className="Login">
      <form onSubmit={handleSubmit}>
        <h2>Login</h2>
        {error && <p className="error">{error}</p>}
        <div>
          <label htmlFor="email">Email</label>
          <input
            type="email"
            id="email"
            value={credentials.email}
            onChange={(e) =>
              setCredentials({ ...credentials, email: e.target.value })
            }
            required
          />
        </div>
        <div>
          <label htmlFor="password">Password</label>
          <input
            type="password"
            id="password"
            value={credentials.password}
            onChange={(e) =>
              setCredentials({ ...credentials, password: e.target.value })
            }
            required
          />
        </div>
        <div>
          <label>User Type</label>
          <div className="userTypeRadio">
            <label>
              <input
                type="radio"
                name="userType"
                value={UserType.CUSTOMER}
                checked={credentials.userType === UserType.CUSTOMER}
                onChange={() =>
                  setCredentials({
                    ...credentials,
                    userType: UserType.CUSTOMER,
                  })
                }
              />
              Customer
            </label>
            <label>
              <input
                type="radio"
                name="userType"
                value={UserType.COMPANY}
                checked={credentials.userType === UserType.COMPANY}
                onChange={() =>
                  setCredentials({ ...credentials, userType: UserType.COMPANY })
                }
              />
              Company
            </label>
            <label>
              <input
                type="radio"
                name="userType"
                value={UserType.ADMIN}
                checked={credentials.userType === UserType.ADMIN}
                onChange={() =>
                  setCredentials({ ...credentials, userType: UserType.ADMIN })
                }
              />
              Admin
            </label>
          </div>
        </div>
        <button type="submit" disabled={isLoading}>
          {isLoading ? "Logging in..." : "Login"}
        </button>
      </form>
    </div>
  );
};

export default Login;
