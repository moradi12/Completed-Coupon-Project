import { jwtDecode } from "jwt-decode";
import { loginAction } from "../Redux/AuthReducer";
import { couponSystem } from "../Redux/Store";

type jwtData = {
    userType: string,
    userName: string,
    id: number,
    sub: string,
    iat: number,
    exp: number
};

export const checkData = () => {
    const state = couponSystem.getState();
    
    if (!state.auth.token || state.auth.token.length < 10) {
        try {
            const storedJwt = sessionStorage.getItem("jwt");
            if (!storedJwt) {
                return; 
            }            
            const JWT = storedJwt;
            const decoded_jwt = jwtDecode<jwtData>(JWT);
            console.log(decoded_jwt);
            let myAuth = {
                id: decoded_jwt.id,
                email: decoded_jwt.sub,
                name: decoded_jwt.userName,
                token: JWT,
                userType: decoded_jwt.userType,
                isLogged: true
            };

            couponSystem.dispatch(loginAction(myAuth));
        } catch (error) {
            console.error("Error decoding JWT", error);
            return;
        }
    }
};
