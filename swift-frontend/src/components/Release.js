// src/components/Release.js
import React, { useState, useEffect } from 'react';
import { fetchPayments, updatePayment } from '../api';

const Release = () => {
  const [payments, setPayments] = useState([]);
  const [message, setMessage] = useState('');

  useEffect(() => {
    fetchPaymentsData();
  }, []);

  const fetchPaymentsData = async () => {
    try {
      const data = await fetchPayments();
      setPayments(data.filter(p => p.status === 'approved')); // 只显示已审批的
      setMessage('待释放支付记录加载成功');
    } catch (error) {
      setMessage(error.message);
    }
  };

  const handleRelease = async (id) => {
    try {
      await updatePayment(id, { status: 'released' });
      fetchPaymentsData();
      setMessage('支付记录释放成功');
    } catch (error) {
      setMessage(error.message);
    }
  };

  return (
    <div className="container-fluid p-4">
      <h1 className="mt-4">Release</h1>
      <p>{message}</p>
      <table className="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Message ID</th>
            <th>Debtor Name</th>
            <th>Creditor Name</th>
            <th>Amount</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          {payments.map((payment) => (
            <tr key={payment.id}>
              <td>{payment.id}</td>
              <td>{payment.msgId}</td>
              <td>{payment.dbtrNm}</td>
              <td>{payment.cdtrNm}</td>
              <td>{payment.instdAmt} {payment.instdAmtCcy}</td>
              <td>
                <button
                  className="btn btn-primary"
                  onClick={() => handleRelease(payment.id)}
                >
                  释放
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Release;