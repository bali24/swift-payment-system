// src/components/Office.js
import React, { useState, useEffect } from 'react';
import { fetchOffices, addOffice, updateOffice, deleteOffice } from '../api';

const Office = () => {
  const [offices, setOffices] = useState([]);
  const [formData, setFormData] = useState({
    officeId: '',
    officeName: '',
    status: 'A',
    businessDate: '',
    defaultCurrency: '',
    transitNo: '',
    countryCode: '',
    paymentAllowed: 'Y',
    lastUpdate: new Date().toISOString().slice(0, 19).replace('T', ' '),
    updateUser: 'admin',
  });
  const [editMode, setEditMode] = useState(false);
  const [message, setMessage] = useState('');

  useEffect(() => {
    fetchOfficeData();
  }, []);

  const fetchOfficeData = async () => {
    try {
      const data = await fetchOffices();
      setOffices(data);
      setMessage('办公室数据加载成功');
    } catch (error) {
      setMessage(error.message);
    }
  };

  const handleAdd = async (e) => {
    e.preventDefault();
    try {
      await addOffice(formData);
      setFormData({
        officeId: '',
        officeName: '',
        status: 'A',
        businessDate: '',
        defaultCurrency: '',
        transitNo: '',
        countryCode: '',
        paymentAllowed: 'Y',
        lastUpdate: new Date().toISOString().slice(0, 19).replace('T', ' '),
        updateUser: 'admin',
      });
      fetchOfficeData();
      setMessage('办公室添加成功');
    } catch (error) {
      setMessage(error.message);
    }
  };

  const handleUpdate = async (e) => {
    e.preventDefault();
    try {
      await updateOffice(formData.officeId, formData);
      setEditMode(false);
      setFormData({ ...formData, officeId: '', officeName: '', businessDate: '' });
      fetchOfficeData();
      setMessage('办公室更新成功');
    } catch (error) {
      setMessage(error.message);
    }
  };

  const handleDelete = async (officeId) => {
    try {
      await deleteOffice(officeId);
      fetchOfficeData();
      setMessage('办公室删除成功');
    } catch (error) {
      setMessage(error.message);
    }
  };

  const handleEdit = (office) => {
    setEditMode(true);
    setFormData(office);
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.id]: e.target.value });
  };

  return (
    <div className="container-fluid p-4">
      <h1 className="mt-4">Office Management</h1>
      <p>{message}</p>

      {/* 添加/更新表单 */}
      <form onSubmit={editMode ? handleUpdate : handleAdd} className="row g-3 mb-4">
        <div className="col-md-3">
          <label htmlFor="officeId" className="form-label">Office ID</label>
          <input
            type="text"
            className="form-control"
            id="officeId"
            value={formData.officeId}
            onChange={handleChange}
            maxLength="3"
            disabled={editMode}
            required
          />
        </div>
        <div className="col-md-6">
          <label htmlFor="officeName" className="form-label">Office Name</label>
          <input
            type="text"
            className="form-control"
            id="officeName"
            value={formData.officeName}
            onChange={handleChange}
            maxLength="35"
            required
          />
        </div>
        <div className="col-md-3">
          <label htmlFor="status" className="form-label">Status</label>
          <select
            className="form-select"
            id="status"
            value={formData.status}
            onChange={handleChange}
          >
            <option value="A">Active</option>
            <option value="I">Inactive</option>
          </select>
        </div>
        <div className="col-md-4">
          <label htmlFor="businessDate" className="form-label">Business Date</label>
          <input
            type="date"
            className="form-control"
            id="businessDate"
            value={formData.businessDate}
            onChange={handleChange}
            required
          />
        </div>
        <div className="col-md-4">
          <label htmlFor="defaultCurrency" className="form-label">Default Currency</label>
          <input
            type="text"
            className="form-control"
            id="defaultCurrency"
            value={formData.defaultCurrency}
            onChange={handleChange}
            maxLength="3"
            required
          />
        </div>
        <div className="col-md-4">
          <label htmlFor="transitNo" className="form-label">Transit No</label>
          <input
            type="text"
            className="form-control"
            id="transitNo"
            value={formData.transitNo}
            onChange={handleChange}
            maxLength="5"
          />
        </div>
        <div className="col-md-4">
          <label htmlFor="countryCode" className="form-label">Country Code</label>
          <input
            type="text"
            className="form-control"
            id="countryCode"
            value={formData.countryCode}
            onChange={handleChange}
            maxLength="2"
            required
          />
        </div>
        <div className="col-md-4">
          <label htmlFor="paymentAllowed" className="form-label">Payment Allowed</label>
          <select
            className="form-select"
            id="paymentAllowed"
            value={formData.paymentAllowed}
            onChange={handleChange}
          >
            <option value="Y">Yes</option>
            <option value="N">No</option>
          </select>
        </div>
        <div className="col-md-4">
          <label htmlFor="updateUser" className="form-label">Update User</label>
          <input
            type="text"
            className="form-control"
            id="updateUser"
            value={formData.updateUser}
            onChange={handleChange}
            maxLength="20"
            required
          />
        </div>
        <div className="col-12">
          <button type="submit" className="btn btn-primary">
            {editMode ? 'Update Office' : 'Add Office'}
          </button>
          {editMode && (
            <button
              type="button"
              className="btn btn-secondary ms-2"
              onClick={() => setEditMode(false)}
            >
              Cancel
            </button>
          )}
        </div>
      </form>

      {/* 数据表格 */}
      <table className="table">
        <thead>
          <tr>
            <th>Office ID</th>
            <th>Office Name</th>
            <th>Status</th>
            <th>Business Date</th>
            <th>Currency</th>
            <th>Transit No</th>
            <th>Country</th>
            <th>Payment</th>
            <th>Last Update</th>
            <th>User</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {offices.map((office) => (
            <tr key={office.officeId}>
              <td>{office.officeId}</td>
              <td>{office.officeName}</td>
              <td>{office.status}</td>
              <td>{office.businessDate}</td>
              <td>{office.defaultCurrency}</td>
              <td>{office.transitNo}</td>
              <td>{office.countryCode}</td>
              <td>{office.paymentAllowed}</td>
              <td>{office.lastUpdate}</td>
              <td>{office.updateUser}</td>
              <td>
                <button
                  className="btn btn-warning me-2"
                  onClick={() => handleEdit(office)}
                >
                  Edit
                </button>
                <button
                  className="btn btn-danger"
                  onClick={() => handleDelete(office.officeId)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Office;