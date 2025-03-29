// src/components/Dashboard.js
import React, { useState, useEffect } from 'react';
import { fetchPayments } from '../api';

const Dashboard = () => {
  const [stats, setStats] = useState({ totalPayments: 0, completed: 0, exceptions: 0 });
  const [message, setMessage] = useState('');

  useEffect(() => {
    fetchDashboardStats();
  }, []);

  const fetchDashboardStats = async () => {
    try {
      const payments = await fetchPayments();
      const total = payments.length;
      const completed = payments.filter(p => p.status === 'completed').length;
      const exceptions = payments.filter(p => p.status === 'exception').length;
      setStats({ totalPayments: total, completed, exceptions });
      setMessage('仪表盘数据加载成功');
    } catch (error) {
      setMessage(error.message);
    }
  };

  return (
    <div className="container-fluid p-4">
      <h1 className="mt-4">Dashboard</h1>
      <p>{message}</p>
      <div className="row">
        <div className="col-md-4">
          <div className="card text-white bg-primary mb-3">
            <div className="card-body">
              <h5 className="card-title">总支付记录</h5>
              <p className="card-text">{stats.totalPayments}</p>
            </div>
          </div>
        </div>
        <div className="col-md-4">
          <div className="card text-white bg-success mb-3">
            <div className="card-body">
              <h5 className="card-title">已完成</h5>
              <p className="card-text">{stats.completed}</p>
            </div>
          </div>
        </div>
        <div className="col-md-4">
          <div className="card text-white bg-danger mb-3">
            <div className="card-body">
              <h5 className="card-title">异常</h5>
              <p className="card-text">{stats.exceptions}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;