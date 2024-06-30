import { Button, ButtonGroup } from "@mui/material";
import "./Header.css";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { checkData } from "../../Utils/checkData";
import { couponSystem } from "../../Redux/Store";
import { logoutAction } from "../../Redux/AuthReducer";

export function Header(props: { setRender: Function }): JSX.Element {
  const navigate = useNavigate();
  const [isLogged, setIsLogged] = useState(false);
  useEffect(() => {
    checkData();
    if (couponSystem.getState().auth.isLogged) {
      setIsLogged(true);
    }
  }, []);
  console.log(isLogged);
  couponSystem.subscribe(() => {
    setIsLogged(couponSystem.getState().auth.isLogged);
  });

  const handleLoginLogout = () => {
    if (isLogged) {
      console.log("HERE");
      sessionStorage.removeItem("jwt");
      couponSystem.dispatch(logoutAction());
    }
    props.setRender(true);
    navigate("/login");
  };

  const handleRegister = () => {
    navigate("/register");
    props.setRender(true);
  };
  return (
    <div className="Header">
      <div className="header-spacer"></div> {}
      <h1 className="header-title">Coupon System</h1> {/* Centered title */}
      <div className="header-buttons">
        {" "}
        {/* New div for buttons */}
        <ButtonGroup variant="contained" size="small">
          {" "}
          {/* Set button size */}
          <Button type="submit" color="primary" onClick={handleLoginLogout}>
            {!isLogged ? "Login" : "Logout"}
          </Button>
          {!isLogged && (
            <Button type="submit" color="primary" onClick={handleRegister}>
              Register
            </Button>
          )}
        </ButtonGroup>
      </div>
    </div>
  );
}
