import { combineReducers } from '@reduxjs/toolkit';
import { AuthReducer } from './AuthReducer';

const rootReducer = combineReducers({
    auth: AuthReducer,
    
    // add other reducers here
});

export type RootState = ReturnType<typeof rootReducer>;
export default rootReducer;