// src/App.js
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { useEffect, useState } from 'react';
// import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import Dashboard from './components/Dashboard';
import Payments from './components/Payments';
import PaymentEntry from './components/PaymentEntry';
import Inquiry from './components/Inquiry';
import Repair from './components/Repair';
import Approve from './components/Approve';
import Release from './components/Release';
import Accounting from './components/Accounting';
import Configuration from './components/Configuration';
import BaseFiles from './components/BaseFiles';
import Office from './components/Office';
import About from './components/About';

const App = () => {
  const [status, setStatus] = useState('');
  useEffect(() => {
    fetch('http://localhost:8081/api/health')
      .then(res => res.text())
      .then(data => setStatus(data));
  }, []);

  return (
    <Router>
      <div className="d-flex">
        <Sidebar />
        <div id="page-content-wrapper" className="flex-grow-1">
          <nav className="navbar navbar-expand-lg navbar-light bg-light border-bottom">
            <button className="btn btn-primary" id="menu-toggle">
              <i className="fas fa-bars"></i>
            </button>
            <div className="ms-auto pe-3">
              <span className="navbar-text">Welcome, Charles King</span>
              <p>Backend Status: {status}</p>
            </div>
          </nav>
          <Routes>
            <Route path="/dashboard/processes" element={<Dashboard />} />
            <Route path="/dashboard/wires-status/completed" element={<Dashboard />} />
            <Route path="/dashboard/wires-status/exceptions" element={<Dashboard />} />
            <Route path="/dashboard/wires-status/future" element={<Dashboard />} />
            <Route path="/payments/payment-entry" element={<PaymentEntry />} />            
            <Route path="/payments/inquiry" element={<Inquiry />} />
            <Route path="/payments/repair" element={<Repair />} />
            <Route path="/payments/approve" element={<Approve />} />
            <Route path="/payments/release" element={<Release />} />
            <Route path="/payments" element={<Payments />} />
            <Route path="/accounting/account-list" element={<Accounting />} />
            <Route path="/accounting/account-details" element={<Accounting />} />
            <Route path="/accounting" element={<Accounting />} />
            <Route path="/configuration/customer" element={<Configuration />} />
            <Route path="/configuration/account" element={<Configuration />} />
            <Route path="/configuration/channels" element={<Configuration />} />
            <Route path="/configuration/rma" element={<Configuration />} />
            <Route path="/configuration" element={<Configuration />} />
            <Route path="/base-files/office" element={<Office />} />
            <Route path="/base-files/department" element={<BaseFiles />} />
            <Route path="/base-files" element={<BaseFiles />} />
            <Route path="/about" element={<About />} />
            <Route path="/" element={<Dashboard />} />
          </Routes>
        </div>
      </div>
   
    </Router>
  );
};

export default App;