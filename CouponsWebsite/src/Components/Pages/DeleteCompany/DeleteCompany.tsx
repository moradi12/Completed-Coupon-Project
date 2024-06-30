// DeleteCompany.tsx
import "./DeleteCompany.css";
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { TextField, Button, Box, Typography, Container } from '@mui/material';
import { checkData } from "../../Utils/checkData";
import { couponSystem } from "../../Redux/Store";
import { UserType } from "../../Models/UserType";
import { notify } from "../../Utils/notif";
import { useNavigate } from "react-router-dom";

const DeleteCompany: React.FC = () => {
  const [companyId, setCompanyId] = useState<string>('');
  const [message, setMessage] = useState<string | null>(null);
  const [userType, setUserType] = useState<UserType | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    if(couponSystem.getState().auth.userType !== UserType.ADMIN){
      navigate("/all");
      notify.error("You are not authorized to view this page.");
  }
    
    checkData(); 
    const authId = couponSystem.getState().auth.id;

    // Fetch user data including userType (assuming it's returned from backend)
    axios.get(`http://localhost:8080/users/${authId}`)
      .then(response => {
        setUserType(response.data.userType); 
      })
      .catch(error => {
        console.error('Error fetching user data:', error);
      });
  }, []);

  const handleDeleteCompany = () => {
    if (!companyId) {
      setMessage('Company ID must be provided');
      return;
    }

    // Check if user is admin before proceeding
    if (userType !== UserType.ADMIN) {
      setMessage('Unauthorized: Only admins can delete companies');
      return;
    }

    axios.delete(`http://localhost:8080/companies/delete/${companyId}`)
      .then(response => {
        setMessage('Company successfully deleted');
        setCompanyId('');
      })
      .catch(error => {
        if (axios.isAxiosError(error)) {
          if (error.response?.status === 404) {
            setMessage('Company not found');
          } else {
            setMessage(error.response?.data || 'Error deleting company');
          }
        } else {
          setMessage('Error deleting company');
        }
      });
  };

  return (
    <Container maxWidth="sm">
      <Box sx={{ mt: 4 }}>
        <Typography variant="h4" gutterBottom>
          Delete Company
        </Typography>
        {message && <Typography color="error">{message}</Typography>}
        <Box component="form" sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
          <TextField
            label="Company ID"
            variant="outlined"
            value={companyId}
            onChange={(e) => setCompanyId(e.target.value)}
            required
          />
          <Button variant="contained" color="primary" onClick={handleDeleteCompany}>
            Delete Company
          </Button>
        </Box>
      </Box>
    </Container>
  );
};

export default DeleteCompany;
