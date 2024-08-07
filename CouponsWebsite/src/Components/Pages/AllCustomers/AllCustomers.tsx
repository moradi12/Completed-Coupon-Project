import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { UserType } from "../../Models/UserType";
import { couponSystem } from "../../Redux/Store";
import axiosJWT from "../../Utils/axiosJWT";
import { checkData } from "../../Utils/checkData";
import { notify } from "../../Utils/notif";
import "./AllCustomers.css";

type Customer = {
  customerID: number;
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  coupons: Coupon[];
};

type Coupon = {
  id: number;
  description: string;
};

export function AllCustomers(): JSX.Element {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [loading, setLoading] = useState(true);
  const [showDeleteConfirmation, setShowDeleteConfirmation] = useState(false);
  const [deletingCustomerId, setDeletingCustomerId] = useState<number | null>(
    null
  );
  const navigate = useNavigate(); // Hook for navigation

  useEffect(() => {
    const fetchData = async () => {
      try {
        await checkData();
        if (couponSystem.getState().auth.userType !== UserType.ADMIN) {
          navigate("/all");
          notify.error("You are not authorized to view this page.");
        }
        await fetchCustomers();
        setLoading(false);
      } catch (error) {
        console.error("Authentication or userType check failed:", error);
      }
    };

    fetchData();
  }, [navigate]);

  const fetchCustomers = async () => {
    try {
      const response = await axiosJWT.get<Customer[]>(
        "http://localhost:8080/api/admin/customers"
      );
      setCustomers(response.data);
      console.log(customers);
      console.log(response.data);
    } catch (error) {
      console.error("Error fetching customers:", error);
    }
  };

  const handleDeleteClick = (customerId: number) => {
    setDeletingCustomerId(customerId);
    setShowDeleteConfirmation(true);
  };

  const handleConfirmDelete = () => {
    deletingCustomerId &&
      axiosJWT
        .delete(
          `http://localhost:8080/api/admin/customers/${deletingCustomerId}`
        )
        .then((res) => {
          console.log(res.data);
        })
        .then(() => {
          notify.success("Customer deleted successfully.");
          setDeletingCustomerId(null);
          setShowDeleteConfirmation(false);
        })
        .catch((error) => {
          console.error("Error deleting customer:", error);
          const axiosError = error as {
            response?: { data?: { message?: string } };
            message?: string;
          };
          const errorMessage =
            axiosError.response?.data?.message ||
            axiosError.message ||
            "Failed to delete customer";
          notify.error(`Failed to delete customer: ${errorMessage}`);
          setDeletingCustomerId(null);
          setShowDeleteConfirmation(false);
        });
  };

  const handleCancelDelete = () => {
    setDeletingCustomerId(null);
    setShowDeleteConfirmation(false);
  };

  const renderCoupons = (coupons: Coupon[]) => {
    return coupons.length > 0 ? (
      <ul>
        {coupons.map((coupon) => (
          <li key={coupon.id}>
            <p>
              <strong>Coupon ID:</strong> {coupon.id}
            </p>
            <p>
              <strong>Description:</strong> {coupon.description}
            </p>
          </li>
        ))}
      </ul>
    ) : (
      <p>No coupons available</p>
    );
  };

  if (loading) {
    return <p>Loading...</p>;
  }

  return (
    <div className="AllCustomers">
      <h1>All Customers</h1>
      {customers.map((customer) => (
        <div key={customer.customerID} className="customer-card">
          <p>
            <strong>Name:</strong> {customer.firstName} {customer.lastName}
          </p>
          <p>
            <strong>Email:</strong> {customer.email}
          </p>
          <p>
            <strong>Customer ID:</strong> {customer.customerID}
          </p>
          <button onClick={() => handleDeleteClick(customer.customerID)}>
            Delete
          </button>
          <div className="customer-coupons">
            <h3>Coupons:</h3>
            {renderCoupons(customer.coupons)}
          </div>
        </div>
      ))}

      {/* Delete Confirmation Modal */}
      {showDeleteConfirmation && (
        <div className="modal">
          <p>Are you sure you want to delete this customer?</p>
          <div>
            <button onClick={handleConfirmDelete}>Yes</button>
            <button onClick={handleCancelDelete}>No</button>
          </div>
        </div>
      )}
    </div>
  );
}
