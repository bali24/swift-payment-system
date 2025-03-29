// src/components/Sidebar.js
import React from 'react';
import { NavLink } from 'react-router-dom';

const Sidebar = () => {
  return (
    <div className="bg-dark border-end" id="sidebar-wrapper">
      <div className="sidebar-heading text-white py-3 text-center">
        <i className="fas fa-university me-2"></i>Payment System
      </div>
      <div className="list-group list-group-flush">
        {/* Dashboard */}
        <a
          href="#dashboardSubmenu"
          className="list-group-item list-group-item-action bg-dark text-white"
          data-bs-toggle="collapse"
        >
          <i className="fas fa-tachometer-alt me-2"></i>Dashboard
        </a>
        <div className="collapse" id="dashboardSubmenu">
          <NavLink to="/dashboard/processes" className="list-group-item list-group-item-action bg-secondary text-white ps-4">
            Processes
          </NavLink>
          <a
            href="#wirestatusSubmenu"
            className="list-group-item list-group-item-action bg-secondary text-white ps-4"
            data-bs-toggle="collapse"
          >
            Wires Status
          </a>
          <div className="collapse ps-5" id="wirestatusSubmenu">
            <NavLink to="/dashboard/wires-status/completed" className="list-group-item list-group-item-action bg-secondary text-white">
              Completed
            </NavLink>
            <NavLink to="/dashboard/wires-status/exceptions" className="list-group-item list-group-item-action bg-secondary text-white">
              Exceptions
            </NavLink>
            <NavLink to="/dashboard/wires-status/future" className="list-group-item list-group-item-action bg-secondary text-white">
              Future
            </NavLink>
          </div>
        </div>

        {/* Payments */}
        <a
          href="#paymentsSubmenu"
          className="list-group-item list-group-item-action bg-dark text-white"
          data-bs-toggle="collapse"
        >
          <i className="fas fa-money-check-alt me-2"></i>Payments
        </a>
        <div className="collapse" id="paymentsSubmenu">
        <NavLink to="/payments/payment-entry" className="list-group-item list-group-item-action bg-secondary text-white ps-4">
            Payment Entry
          </NavLink>          
          <NavLink to="/payments/inquiry" className="list-group-item list-group-item-action bg-secondary text-white ps-4">
            Inquiry
          </NavLink>
          <NavLink to="/payments/repair" className="list-group-item list-group-item-action bg-secondary text-white ps-4">
            Repair
          </NavLink>
          <NavLink to="/payments/approve" className="list-group-item list-group-item-action bg-secondary text-white ps-4">
            Approve
          </NavLink>
          <NavLink to="/payments/release" className="list-group-item list-group-item-action bg-secondary text-white ps-4">
            Release
          </NavLink>
        </div>

        {/* Accounting */}
        <a
          href="#accountingSubmenu"
          className="list-group-item list-group-item-action bg-dark text-white"
          data-bs-toggle="collapse"
        >
          <i className="fas fa-book me-2"></i>Accounting
        </a>
        <div className="collapse" id="accountingSubmenu">
          <NavLink to="/accounting/account-list" className="list-group-item list-group-item-action bg-secondary text-white ps-4">
            Account List
          </NavLink>
          <NavLink to="/accounting/account-details" className="list-group-item list-group-item-action bg-secondary text-white ps-4">
            Account Details
          </NavLink>
        </div>

        {/* Configuration */}
        <a
          href="#configSubmenu"
          className="list-group-item list-group-item-action bg-dark text-white"
          data-bs-toggle="collapse"
        >
          <i className="fas fa-cogs me-2"></i>Configuration
        </a>
        <div className="collapse" id="configSubmenu">
          <NavLink to="/configuration/customer" className="list-group-item list-group-item-action bg-secondary text-white ps-4">
            Customer
          </NavLink>
          <NavLink to="/configuration/account" className="list-group-item list-group-item-action bg-secondary text-white ps-4">
            Account
          </NavLink>
          <NavLink to="/configuration/channels" className="list-group-item list-group-item-action bg-secondary text-white ps-4">
            Channels
          </NavLink>
          <NavLink to="/configuration/rma" className="list-group-item list-group-item-action bg-secondary text-white ps-4">
            RMA
          </NavLink>
        </div>

        {/* Base Files */}
        <a
          href="#baseFilesSubmenu"
          className="list-group-item list-group-item-action bg-dark text-white"
          data-bs-toggle="collapse"
        >
          <i className="fas fa-folder me-2"></i>Base Files
        </a>
        <div className="collapse" id="baseFilesSubmenu">
          <NavLink to="/base-files/office" className="list-group-item list-group-item-action bg-secondary text-white ps-4">
            Office
          </NavLink>
          <NavLink to="/base-files/department" className="list-group-item list-group-item-action bg-secondary text-white ps-4">
            Department
          </NavLink>
        </div>

        {/* About */}
        <NavLink to="/about" className="list-group-item list-group-item-action bg-dark text-white">
          <i className="fas fa-info-circle me-2"></i>About
        </NavLink>
      </div>
    </div>
  );
};

export default Sidebar;