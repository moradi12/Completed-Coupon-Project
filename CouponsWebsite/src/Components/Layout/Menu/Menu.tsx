import { NavLink } from "react-router-dom";
import "./Menu.css";
import { useEffect, useState } from "react";
import { checkData } from "../../Utils/checkData";
import { couponSystem } from "../../Redux/Store";
import { loginAction } from "../../Redux/AuthReducer";

export function Menu(props: {
  render: boolean;
  setRender: Function;
}): JSX.Element {
  const [menuType, setMenuType] = useState("guest");

  useEffect(() => {
    checkData();
    setMenuType(couponSystem.getState().auth.userType);
    props.setRender(false);
  }, [props.render]);

  const guestMenu = (
    <>
      | <NavLink to="/all">Coupons </NavLink>
    </>
  );

  const adminMenu = (
    <>
      | <NavLink to="/all">Coupon List</NavLink>|{" "}
      <NavLink to="/all/companies">List of Companies</NavLink>|{" "}
      <NavLink to="/admin/add/company">Add company</NavLink>|{" "}
      <NavLink to="/admin/customers">Customer List</NavLink>{" "}
    </>
  );

  const companyMenu = (
    <>
      | <NavLink to="/all">Coupon List</NavLink>|{" "}
      <NavLink to="/add">Add Coupon</NavLink>
    </>
  );

  const customerMenu = (
    <>
      | <NavLink to="/all">Coupon List</NavLink>{" "}
      | <NavLink to="/customer-coupons">My Coupons</NavLink> 
      |
    </>
  );

  const handleMenu = () => {
    switch (menuType) {
      case "ADMIN":
        return adminMenu;
      case "COMPANY":
        return companyMenu;
      case "CUSTOMER":
        return customerMenu;
      default:
        return guestMenu;
    }
  };

  return <div className="Menu">{handleMenu()}</div>;
}
