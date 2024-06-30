import { useNavigate } from "react-router-dom";
import "./CompanyCoupons.css";
import { useEffect, useState } from "react";
import { couponSystem } from "../../Redux/Store";
import { checkData } from "../../Utils/checkData";

interface Company {
    id: number;
    name: string;
    email: string;
    coupons?: Coupon[];
  }
  
  interface Coupon {
    id: number;
    title: string;
    description: string;
    price: number;
  }
  



export function CompanyCoupons(): JSX.Element {
    const [deleteMessage, setDeleteMessage] = useState<string | null>(null);
    const currentUserType = couponSystem.getState().auth.userType;
  
    const navigate = useNavigate();

    useEffect(() => {
      checkData();
    }, [navigate]);  
  




    return (
        <div className="CompanyCoupons">
			
        </div>
    );
}
