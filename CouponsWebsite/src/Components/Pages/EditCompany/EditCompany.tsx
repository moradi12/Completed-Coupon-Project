import React, { useState, useEffect } from 'react';
import { Box, Button, Container, TextField, Typography } from '@mui/material';
import { useParams, useNavigate } from 'react-router-dom';
import { notify } from '../../Utils/notif';
import axiosJWT from '../../Utils/axiosJWT';
import { AxiosError } from 'axios';

const EditCompany: React.FC = () => {
  const { companyId } = useParams<{ companyId: string }>();
  const [company, setCompany] = useState({ id: companyId, name: '', email: '', password: '' });
  const navigate = useNavigate();

  useEffect(() => {
    console.log('companyId:', companyId);  // Debugging line
    if (companyId) {
      axiosJWT.get(`http://localhost:8080/companies/${companyId}`)
        .then(response => {
          setCompany(response.data);
        })
        .catch((error: AxiosError) => {
          notify.error('Error fetching company details');
          console.error('Error fetching company details', error.message);
        });
    } else {
      notify.error('Company ID is undefined');
    }
  }, [companyId]);

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    setCompany((prevCompany) => ({ ...prevCompany, [name]: value }));
  };

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    
    axiosJWT.put(`http://localhost:8080/companies/${companyId}`, company)
      .then(() => {
        notify.success('Company updated successfully');
        navigate('/all');
      })
      .catch((error: AxiosError) => {
        notify.error('Error updating company details');
        console.error('Error updating company details', error.message);
      });
  };

  return (
    <Container maxWidth="sm">
      <Box sx={{ mt: 4 }}>
        <Typography variant="h4" gutterBottom>
          Edit Company
        </Typography>
        <form onSubmit={handleSubmit}>
          <TextField
            fullWidth
            label="Name"
            name="name"
            value={company.name}
            onChange={handleChange}
            margin="normal"
            variant="outlined"
          />
          <TextField
            fullWidth
            label="Email"
            name="email"
            value={company.email}
            onChange={handleChange}
            margin="normal"
            variant="outlined"
          />
          <TextField
            fullWidth
            label="Password"
            name="password"
            value={company.password}
            onChange={handleChange}
            margin="normal"
            variant="outlined"
            type="password"
          />
          <Button type="submit" variant="contained" color="primary">
            Save
          </Button>
        </form>
      </Box>
    </Container>
  );
};

export default EditCompany;
