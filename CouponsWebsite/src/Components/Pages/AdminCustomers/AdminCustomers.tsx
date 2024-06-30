import React, { useEffect, useState, useCallback } from 'react';
import { notify } from '../../Utils/notif';
import { checkData } from '../../Utils/checkData';
import axiosJWT from '../../Utils/axiosJWT';
import { UserType } from '../../Models/UserType';
import { couponSystem } from '../../Redux/Store';
import { useNavigate } from 'react-router-dom';
import './AdminCustomers.css';

interface Customer {
    customerID: number;
    name: string;
    email: string;
}

const AdminCustomers: React.FC = () => {
    const [customers, setCustomers] = useState<Customer[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    const fetchCustomers = useCallback(async () => {
        setLoading(true);
        setError(null);

        const token = sessionStorage.getItem('jwt');
        if (!token) {
            setLoading(false);
            setError('No JWT token found');
            notify.error('No JWT token found');
            return;
        }

        try {
            const response = await axiosJWT.get('http://localhost:8080/api/admin/admin/customers', {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            setCustomers(response.data);
            notify.success('Customers fetched successfully');
        } catch (error) {
            handleApiError(error, 'fetching customers');
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        checkData(); 
        const userType = couponSystem.getState().auth.userType;
        if (userType !== UserType.ADMIN) {
            navigate("/all");
            notify.error("You are not authorized to view this page.");
        } else {
            fetchCustomers();
        }
    }, [fetchCustomers, navigate]);

    const handleDelete = async (customerId: number) => {
        const token = sessionStorage.getItem('jwt');
        if (!token) {
            notify.error('No JWT token found');
            return;
        }

        try {
            const response = await axiosJWT.delete(`http://localhost:8080/api/admin/customers/${customerId}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            if (response.status === 200) {
                setCustomers(customers.filter(customer => customer.customerID !== customerId));
                notify.success('Customer deleted successfully');
            } else {
                notify.error('Failed to delete customer');
            }
        } catch (error) {
            handleApiError(error, 'deleting the customer');
        }
    };

    const handleApiError = (error: any, action: string) => {
        let errorMessage = `An error occurred while ${action}`;
        if (error.response) {
            switch (error.response.status) {
                case 401:
                    errorMessage = 'JWT token has expired or is invalid';
                    break;
                case 403:
                    errorMessage = 'Forbidden: Insufficient privileges';
                    break;
                case 404:
                    errorMessage = 'Customer not found';
                    break;
                default:
                    errorMessage = error.response.data.message || errorMessage;
                    break;
            }
        }
        setError(errorMessage);
        notify.error(errorMessage);
    };

    return (
        <div className="container">
            <h1 className="title">Admin - Customers</h1>
            {loading && <p className="loading">Loading...</p>}
            {error && <p className="error">{error}</p>}
            {!loading && !error && (
                <ul className="customer-list">
                    {customers.map((customer) => (
                        <li key={customer.customerID} className="customer-item">
                            <span className="customer-name">{customer.name}</span> ({customer.email})
                            <button className="delete-button" onClick={() => handleDelete(customer.customerID)}>Delete</button>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default AdminCustomers;
