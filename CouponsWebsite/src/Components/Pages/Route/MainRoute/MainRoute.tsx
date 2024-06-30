import { Route, Routes } from "react-router-dom";
import { AllCoupons, Main } from "../../../Layout/Main/Main";
import { Page404 } from "../../Page404/Page404";
import { AddCoupon } from "../../AddCoupon/AddCoupon";
import AllCompanies from "../../AllCompanies/AllCompanies";
import { Register } from "../../Register/Register";
import Login from "../../Login/Login";
import { AllCustomers } from "../../AllCustomers/AllCustomers";
import AddCompanyAdmin from "../../AddCompanyAdmin/AddCompanyAdmin";
import AddCustomerAdmin from "../../AddCustomerAdmin/AddCustomerAdmin";
import DeleteCompany from "../../DeleteCompany/DeleteCompany";
import { AllGuestCoupons } from "../../AllGuestCoupons/AllGuestCoupons";
import EditCompany from "../../EditCompany/EditCompany";
import { CustomerCoupons } from "../../CustomerCoupons/CustomerCoupons"; 
import AdminCustomers from "../../AdminCustomers/AdminCustomers"; // Import the new component

export function MainRoute(props: { setRender: Function }): JSX.Element {
    return (
        <div className="MainRoute">
            <Routes>
                <Route path="/" element={<Main />} />
                <Route path="/register" element={<Register />} />
                <Route path="/add" element={<AddCoupon />} />
                <Route path="/admin/add/company" element={<AddCompanyAdmin />} />
                <Route path="/admin/add/customer" element={<AddCustomerAdmin />} />
                <Route path="/company/delete" element={<DeleteCompany />} />
                <Route path="guest/all" element={<AllGuestCoupons />} />
                <Route path="/edit/company/:companyId" element={<EditCompany />} />
                <Route path="/all/companies" element={<AllCompanies />} />
                <Route path="/all" element={<AllCoupons />} />
                <Route path="/login" element={<Login setRender={props.setRender} />} />
                <Route path="/all/customers" element={<AllCustomers />} />
                <Route path="/customer-coupons" element={<CustomerCoupons />} /> 
                <Route path="/admin/customers" element={<AdminCustomers />} /> {/* Added the new route */}
                <Route path="*" element={<Page404 />} />
            </Routes>
        </div>
    );
}
