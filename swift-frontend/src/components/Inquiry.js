// src/components/Inquiry.js
import React, { useState, useEffect } from 'react';
import axios from 'axios';

const Inquiry = () => {
  const [payments, setPayments] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [message, setMessage] = useState('');

  useEffect(() => {
    fetchPayments();
  }, []);

  const fetchPayments = async () => {
    try {
      const response = await axios.get('/api/payments', { params: { search: searchTerm } });
      setPayments(response.data);
      setMessage('支付记录查询成功');
    } catch (error) {
      setMessage('查询失败: ' + error.message);
    }
  };

  const handleSearch = (e) => {
    e.preventDefault();
    fetchPayments();
  };

  return (
    <div className="container-fluid p-4">
      <h1 className="mt-4">Inquiry</h1>
      <p>{message}</p>
      <form onSubmit={handleSearch} className="mb-4">
        <div className="input-group">
          <input
            type="text"
            className="form-control"
            placeholder="搜索支付记录..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          <button type="submit" className="btn btn-primary">搜索</button>
        </div>
      </form>
      <table className="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Message Type</th>
            <th>Message ID</th>
            <th>Debtor Name</th>
            <th>Creditor Name</th>
            <th>Amount</th>
          </tr>
        </thead>
        <tbody>
          {payments.map((payment) => (
            <tr key={payment.id}>
              <td>{payment.id}</td>
              <td>{payment.messageType}</td>
              <td>{payment.msgId}</td>
              <td>{payment.dbtrNm}</td>
              <td>{payment.cdtrNm}</td>
              <td>{payment.instdAmt} {payment.instdAmtCcy}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Inquiry;