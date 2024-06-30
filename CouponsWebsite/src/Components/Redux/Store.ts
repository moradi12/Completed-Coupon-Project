import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { AuthReducer } from "./AuthReducer";
import { CompanyReducer } from "./Company/CompanyReducer";

const reducers = combineReducers({ auth: AuthReducer,company:CompanyReducer });

export const couponSystem = configureStore({
    reducer: reducers,
    middleware: (getDefaultMiddleware) => getDefaultMiddleware({ serializableCheck: false })
});
