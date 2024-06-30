import "./AddCompany.css";
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { TextField, Button, Box, Typography, Container } from '@mui/material';
import { Company } from "../../Models/Company";
import { checkData } from "../../Utils/checkData";
import { couponSystem } from "../../Redux/Store";  // Import the couponSystem

const AddCompany: React.FC = () => {
  const [companyName, setCompanyName] = useState<string>('');
  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [message, setMessage] = useState<string | null>(null);

  useEffect(() => {
    checkData();
  }, []);

  const handleAddCompany = () => {
    if (!companyName || !email || !password) {
      setMessage('All fields must be filled out');
      return;
    }

    const authId = couponSystem.getState().auth.id;  // Get the auth ID
    const newCompany = new Company(0, companyName, email, password);

    axios.post(`http://localhost:8080/companies/add/${authId}`, newCompany)
      .then(response => {
        setMessage(response.data);
        setCompanyName('');
        setEmail('');
        setPassword('');
      })
      .catch(error => {
        if (axios.isAxiosError(error)) {
          if (error.response?.status === 409 || (error.response?.data?.message?.includes('Duplicate entry'))) {
            setMessage('Email or company name already exists');
          } else {
            setMessage(error.response?.data || 'Error adding company');
          }
        } else {
          setMessage('Error adding company');
        }
      });
  };

  return (
    <Container maxWidth="sm">
      <Box sx={{ mt: 4 }}>
        <Typography variant="h4" gutterBottom>
          Add New Company
        </Typography>
        {message && <Typography color="error">{message}</Typography>}
        <Box component="form" sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
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
          <Button variant="contained" color="primary" onClick={handleAddCompany}>
            Add Company
          </Button>
        </Box>
      </Box>
    </Container>
  );
};

export default AddCompany;
