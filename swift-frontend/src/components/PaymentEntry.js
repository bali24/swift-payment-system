// src/components/PaymentEntry.js
import React, { useState, useEffect } from 'react';
import { fetchPayments, addPayment, updatePayment, deletePayment } from '../api';

const PaymentEntry = () => {
  const [payments, setPayments] = useState([]);
  const [formData, setFormData] = useState({
    messageType: 'pacs.008',
    msgId: '',
    creDtTm: '',
    nbOfTxs: '',
    ttlIntrBkSttlmAmt: '',
    ttlIntrBkSttlmCcy: '',
    pmtIdInstrId: '',
    pmtIdEndToEndId: '',
    instdAmt: '',
    instdAmtCcy: '',
    dbtrNm: '',
    dbtrAcctId: '',
    dbtrAgtBIC: '',
    cdtrNm: '',
    cdtrAcctId: '',
    cdtrAgtBIC: '',
  });
  const [message, setMessage] = useState('');

  useEffect(() => {
    fetchPaymentsData();
  }, []);

  const fetchPaymentsData = async () => {
    try {
      const data = await fetchPayments();
      setPayments(data);
      setMessage('支付记录查询成功');
    } catch (error) {
      setMessage(error.message);
    }
  };

  const handleAdd = async (e) => {
    e.preventDefault();
    try {
      await addPayment(formData);
      setFormData({ ...formData, msgId: '', creDtTm: '', nbOfTxs: '' });
      fetchPaymentsData();
      setMessage('支付记录添加成功');
    } catch (error) {
      setMessage(error.message);
    }
  };

  const handleUpdate = async (id) => {
    try {
      await updatePayment(id, formData);
      fetchPaymentsData();
      setMessage('支付记录更新成功');
    } catch (error) {
      setMessage(error.message);
    }
  };

  const handleDelete = async (id) => {
    try {
      await deletePayment(id);
      fetchPaymentsData();
      setMessage('支付记录删除成功');
    } catch (error) {
      setMessage(error.message);
    }
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.id]: e.target.value });
  };

  return (
    <div className="container-fluid p-4">
      <h1 className="mt-4">Payment Entry</h1>
      <p>{message}</p>

      {/* 添加支付记录表单 */}
      <form onSubmit={handleAdd} className="row g-3">
        <div className="col-12">
          <label htmlFor="messageType" className="form-label">Message Type</label>
          <select id="messageType" className="form-select" value={formData.messageType} onChange={handleChange}>
            <option value="pacs.008">pacs.008</option>
            <option value="pacs.009">pacs.009</option>
          </select>
        </div>
        <div className="col-md-6">
          <label htmlFor="msgId" className="form-label">Message ID</label>
          <input type="text" className="form-control" id="msgId" value={formData.msgId} onChange={handleChange} required />
        </div>
        <div className="col-md-6">
          <label htmlFor="creDtTm" className="form-label">Creation Date Time</label>
          <input type="text" className="form-control" id="creDtTm" value={formData.creDtTm} onChange={handleChange} required />
        </div>
        <div className="col-md-6">
          <label htmlFor="nbOfTxs" className="form-label">Number of Transactions</label>
          <input type="text" className="form-control" id="nbOfTxs" value={formData.nbOfTxs} onChange={handleChange} required />
        </div>
        <div className="col-md-6">
          <label htmlFor="ttlIntrBkSttlmAmt" className="form-label">Total Interbank Settlement Amount</label>
          <input type="number" step="0.01" className="form-control" id="ttlIntrBkSttlmAmt" value={formData.ttlIntrBkSttlmAmt} onChange={handleChange} />
        </div>
        <div className="col-md-6">
          <label htmlFor="ttlIntrBkSttlmCcy" className="form-label">Currency</label>
          <input type="text" className="form-control" id="ttlIntrBkSttlmCcy" value={formData.ttlIntrBkSttlmCcy} onChange={handleChange} />
        </div>
        <div className="col-md-6">
          <label htmlFor="pmtIdInstrId" className="form-label">Instruction ID</label>
          <input type="text" className="form-control" id="pmtIdInstrId" value={formData.pmtIdInstrId} onChange={handleChange} required />
        </div>
        <div className="col-md-6">
          <label htmlFor="pmtIdEndToEndId" className="form-label">End to End ID</label>
          <input type="text" className="form-control" id="pmtIdEndToEndId" value={formData.pmtIdEndToEndId} onChange={handleChange} required />
        </div>
        <div className="col-md-6">
          <label htmlFor="instdAmt" className="form-label">Instructed Amount</label>
          <input type="number" step="0.01" className="form-control" id="instdAmt" value={formData.instdAmt} onChange={handleChange} required />
        </div>
        <div className="col-md-6">
          <label htmlFor="instdAmtCcy" className="form-label">Instructed Amount Currency</label>
          <input type="text" className="form-control" id="instdAmtCcy" value={formData.instdAmtCcy} onChange={handleChange} required />
        </div>
        <div className="col-md-6">
          <label htmlFor="dbtrNm" className="form-label">Debtor Name</label>
          <input type="text" className="form-control" id="dbtrNm" value={formData.dbtrNm} onChange={handleChange} required />
        </div>
        <div className="col-md-6">
          <label htmlFor="dbtrAcctId" className="form-label">Debtor Account ID</label>
          <input type="text" className="form-control" id="dbtrAcctId" value={formData.dbtrAcctId} onChange={handleChange} required />
        </div>
        <div className="col-md-6">
          <label htmlFor="dbtrAgtBIC" className="form-label">Debtor Agent BIC</label>
          <input type="text" className="form-control" id="dbtrAgtBIC" value={formData.dbtrAgtBIC} onChange={handleChange} required />
        </div>
        <div className="col-md-6">
          <label htmlFor="cdtrNm" className="form-label">Creditor Name</label>
          <input type="text" className="form-control" id="cdtrNm" value={formData.cdtrNm} onChange={handleChange} required />
        </div>
        <div className="col-md-6">
          <label htmlFor="cdtrAcctId" className="form-label">Creditor Account ID</label>
          <input type="text" className="form-control" id="cdtrAcctId" value={formData.cdtrAcctId} onChange={handleChange} required />
        </div>
        <div className="col-md-6">
          <label htmlFor="cdtrAgtBIC" className="form-label">Creditor Agent BIC</label>
          <input type="text" className="form-control" id="cdtrAgtBIC" value={formData.cdtrAgtBIC} onChange={handleChange} required />
        </div>
        <div className="col-12">
          <button type="submit" className="btn btn-primary">添加支付记录</button>
        </div>
      </form>

      {/* 支付记录表格 */}
      <table className="table mt-4">
        <thead>
          <tr>
            <th>ID</th>
            <th>Message Type</th>
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
              <td>{payment.messageType}</td>
              <td>{payment.msgId}</td>
              <td>{payment.dbtrNm}</td>
              <td>{payment.cdtrNm}</td>
              <td>{payment.instdAmt} {payment.instdAmtCcy}</td>
              <td>
                <button className="btn btn-warning me-2" onClick={() => handleUpdate(payment.id)}>更新</button>
                <button className="btn btn-danger" onClick={() => handleDelete(payment.id)}>删除</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default PaymentEntry;