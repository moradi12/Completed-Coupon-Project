import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Box, Typography, Container, List, ListItem, ListItemText, Divider, Button } from '@mui/material';
import { UserType } from '../../Models/UserType';
import './AllCompanies.css';
import { couponSystem } from '../../Redux/Store';
import { checkData } from '../../Utils/checkData';
import { useNavigate } from 'react-router-dom';
import { notify } from '../../Utils/notif';
import axiosJWT from '../../Utils/axiosJWT';

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

const AllCompanies: React.FC = () => {
  const [companies, setCompanies] = useState<Company[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [deleteMessage, setDeleteMessage] = useState<string | null>(null);
  const currentUserType = couponSystem.getState().auth.userType;
  const navigate = useNavigate();

  useEffect(() => {
    checkData();
    
    if (couponSystem.getState().auth.userType !== UserType.ADMIN) {
      navigate("/all");
      notify.error("You are not authorized to view this page.");
      return;
    }

   
    fetchCompanies();
  }, [navigate]);

  const fetchCompanies = () => {
    axiosJWT.get('http://localhost:8080/api/admin/companies')
      .then(response => {
       setCompanies(response.data);
        
      })
      .catch(error => {
        if (axios.isAxiosError(error)) {
          setError(JSON.stringify(error.response?.data || 'Error fetching companies'));
        } else {
          setError('Error fetching companies');
        }
      });
  };

  const handleDelete = (companyId: number) => {
    const state = couponSystem.getState();
    const token = state.auth?.token;

    if (!token || token.split('.').length !== 3) {
      setError('Invalid token. Please log in again.');
      return;
    }

    axiosJWT.delete(`http://localhost:8080/companies/${companyId}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    .then(() => {
      setCompanies(prevCompanies => prevCompanies.filter(company => company.id !== companyId));
      setDeleteMessage('Company deleted successfully');
    })
    .catch(error => {
      if (axios.isAxiosError(error)) {
        setError(JSON.stringify(error.response?.data || 'Error deleting company'));
      } else {
        setError('Error deleting company');
      }
      setDeleteMessage('Failed to delete company');
    });
  };

  const handleEdit = (companyId: number) => {
    navigate(`/edit/company/${companyId}`);
  };

  return (
    <Container maxWidth="md" className="container">
      <Box sx={{ mt: 4 }}>
        <Typography variant="h4" gutterBottom>
          All Companies
        </Typography>
        {error && <Typography color="error">{error}</Typography>}
        {deleteMessage && <Typography color={deleteMessage.includes('successfully') ? 'primary' : 'error'}>{deleteMessage}</Typography>}
        <List className="company-list">
          {companies.map((company) => (
            <React.Fragment key={company.id}>
              <ListItem className="company-item">
                <ListItemText
                  primary={<Typography variant="h6">{company.name}</Typography>}
                  secondary={
                    <>
                      <Typography variant="body2" className="company-info"><strong>Email:</strong> {company.email}</Typography>
                      <Typography variant="body2" className="company-info"><strong>ID:</strong> {company.id}</Typography>
                      {company.coupons && company.coupons.length > 0 && (
                        <Box className="coupons">
                          <Typography variant="subtitle1">Coupons:</Typography>
                          <List>
                            {company.coupons.map((coupon) => (
                              <ListItem key={coupon.id} className="coupon-item">
                                <ListItemText
                                  primary={<Typography variant="body1">{coupon.title}</Typography>}
                                  secondary={
                                    <>
                                      <Typography variant="body2"><strong>Description:</strong> {coupon.description}</Typography>
                                      <Typography variant="body2"><strong>Price:</strong> {coupon.price}</Typography>
                                    </>
                                  }
                                />
                              </ListItem>
                            ))}
                          </List>
                        </Box>
                      )}
                    </>
                  }
                />
                {currentUserType === UserType.ADMIN && (
                  <>
                    <Button variant="outlined" color="primary" onClick={() => handleEdit(company.id)}>Edit</Button>
                    <Button variant="outlined" color="secondary" onClick={() => handleDelete(company.id)}>Delete</Button>
                  </>

                )}
              </ListItem>
              <Divider />
            </React.Fragment>
          ))}
        </List>
      </Box>
    </Container>
  );
};

export default AllCompanies;
