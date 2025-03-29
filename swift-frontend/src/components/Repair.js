// src/components/Repair.js
import React, { useState, useEffect } from 'react';
import { fetchPayments, updatePayment } from '../api';

const Repair = () => {
  const [payments, setPayments] = useState([]);
  const [selectedPayment, setSelectedPayment] = useState(null);
  const [message, setMessage] = useState('');

  useEffect(() => {
    fetchPaymentsData();
  }, []);

  const fetchPaymentsData = async () => {
    try {
      const data = await fetchPayments();
      setPayments(data);
      setMessage('支付记录加载成功');
    } catch (error) {
      setMessage(error.message);
    }
  };

  const handleRepair = async (e) => {
    e.preventDefault();
    if (!selectedPayment) return;
    try {
      await updatePayment(selectedPayment.id, selectedPayment);
      fetchPaymentsData();
      setMessage('支付记录修复成功');
      setSelectedPayment(null);
    } catch (error) {
      setMessage(error.message);
    }
  };

  const handleChange = (e) => {
    setSelectedPayment({ ...selectedPayment, [e.target.name]: e.target.value });
  };

  return (
    <div className="container-fluid p-4">
      <h1 className="mt-4">Repair</h1>
      <p>{message}</p>
      <table className="table mb-4">
        <thead>
          <tr>
            <th>ID</th>
            <th>Message ID</th>
            <th>Debtor Name</th>
            <th>Creditor Name</th>
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
              <td>
                <button
                  className="btn btn-warning"
                  onClick={() => setSelectedPayment(payment)}
                >
                  修复
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {selectedPayment && (
        <form onSubmit={handleRepair}>
          <div className="mb-3">
            <label className="form-label">Message ID</label>
            <input
              type="text"
              className="form-control"
              name="msgId"
              value={selectedPayment.msgId}
              onChange={handleChange}
            />
          </div>
          <div className="mb-3">
            <label className="form-label">Debtor Name</label>
            <input
              type="text"
              className="form-control"
              name="dbtrNm"
              value={selectedPayment.dbtrNm}
              onChange={handleChange}
            />
          </div>
          <button type="submit" className="btn btn-primary">保存修复</button>
        </form>
      )}
    </div>
  );
};

export default Repair;