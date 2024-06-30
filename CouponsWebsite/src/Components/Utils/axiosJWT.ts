import axios from 'axios';
import { couponSystem } from '../Redux/Store';
import { updateTokenAction } from '../Redux/AuthReducer';

const axiosJWT = axios.create();

axiosJWT.interceptors.request.use(
    request => {       
        request.headers.Authorization = `Bearer ${couponSystem.getState().auth.token}`;
        console.log("BEFORE POST",request.headers.Authorization)
        return request;
    }
);

axiosJWT.interceptors.response.use(
    response => {
        const authorization:string = response.headers.authorization.split(' ')[1];
        couponSystem.dispatch(updateTokenAction(authorization));      
        sessionStorage.setItem('jwt', authorization);               
        return response;
    }
);

export default axiosJWT;
