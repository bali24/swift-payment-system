// src/components/Payments.js
import React from 'react';
import { Link } from 'react-router-dom';

const Payments = () => {
  return (
    <div className="container-fluid p-4">
      <h1 className="mt-4">Payments</h1>
      <p>请选择一个子菜单以管理支付记录。</p>
      <ul className="list-group">
        <li className="list-group-item">
          <Link to="/payments/payment-entry">Payment Entry</Link> - 添加新支付记录
        </li>        
        <li className="list-group-item">
          <Link to="/payments/inquiry">Inquiry</Link> - 查询支付记录
        </li>
        <li className="list-group-item">
          <Link to="/payments/repair">Repair</Link> - 修复支付记录
        </li>
        <li className="list-group-item">
          <Link to="/payments/approve">Approve</Link> - 审批支付记录
        </li>
        <li className="list-group-item">
          <Link to="/payments/release">Release</Link> - 释放支付记录
        </li>
      </ul>
    </div>
  );
};

export default Payments;