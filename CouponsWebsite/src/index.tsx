import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';
import { BrowserRouter } from 'react-router-dom';
import { MainLayout } from './Components/Layout/MainLayout/MainLayout';
import { Provider } from 'react-redux';
import { couponSystem } from './Components/Redux/Store';

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

root.render(
  <Provider store={couponSystem}>
    <BrowserRouter>
      <MainLayout />
    </BrowserRouter>
  </Provider>
);

reportWebVitals();
