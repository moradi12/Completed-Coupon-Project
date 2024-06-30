import "./UpdateCompany.css";
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { TextField, Button, Box, Typography, Container } from '@mui/material';
import { Company } from "../../Models/Company";
import { checkData } from "../../Utils/checkData";
import { couponSystem } from "../../Redux/Store"; 
import axiosJWT from "../../Utils/axiosJWT";

const UpdateCompany: React.FC = () => {
  const [companyId, setCompanyId] = useState<string>('');
  const [companyName, setCompanyName] = useState<string>('');
  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [message, setMessage] = useState<string | null>(null);

  useEffect(() => {
    checkData();
  }, []);

  const handleUpdateCompany = () => {
    if (!companyId || !companyName || !email || !password) {
      setMessage('All fields must be filled out');
      return;
    }

    const authId = couponSystem.getState().auth.id;  // Get the auth ID
    const jwt = couponSystem.getState().auth.token;    // Get the JWT token
    const updatedCompany = new Company(Number(companyId), companyName, email, password);

    axiosJWT.put(`http://localhost:8080/companies/${authId}`, updatedCompany, {
      headers: {
        Authorization: `Bearer ${jwt}`
      }
    })
      .then(response => {
        setMessage('Company updated successfully');
        // setCompanyId('');
        setCompanyName('');
        setEmail('');
        setPassword('');
      })
      .catch(error => {
        if (axios.isAxiosError(error)) {
          if (error.response?.status === 409 || (error.response?.data?.message?.includes('Duplicate entry'))) {
            setMessage('Email or company name already exists');
          } else {
            setMessage(error.response?.data?.message || 'Error updating company');
          }
        } else {
          setMessage('Error updating company');
        }
      });
  };

  return (
    <Container maxWidth="sm" className="UpdateCompany">
      <Box sx={{ mt: 4 }}>
        <Typography variant="h4" gutterBottom>
          Update Company
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
          <TextField
            label="Name"
            variant="outlined"
            value={companyName}
            onChange={(e) => setCompanyName(e.target.value)}
            required
          />
          <TextField
            label="Email"
            variant="outlined"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <TextField
            label="Password"
            variant="outlined"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <Button variant="contained" color="primary" onClick={handleUpdateCompany}>
            Update Company
          </Button>
        </Box>
      </Box>
    </Container>
  );
};

export default UpdateCompany;
