// src/components/Accounting.js
import React from 'react';
import { Link } from 'react-router-dom';

const Accounting = () => {
  return (
    <div className="container-fluid p-4">
      <h1 className="mt-4">Accounting</h1>
      <p>请选择一个子菜单以管理账户信息。</p>
      <ul className="list-group">
        <li className="list-group-item">
          <Link to="/accounting/account-list">Account List</Link> - 查看账户列表
        </li>
        <li className="list-group-item">
          <Link to="/accounting/account-details">Account Details</Link> - 查看账户详情
        </li>
      </ul>
    </div>
  );
};

export default Accounting;