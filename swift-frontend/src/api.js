// src/api.js
import axios from 'axios';

// 设置默认的 API 基础 URL（根据你的后端服务调整）
const API_BASE_URL = 'http://localhost:8081/api'; // 示例后端地址，可替换为实际地址

// 配置 Axios 默认设置
axios.defaults.baseURL = API_BASE_URL;
axios.defaults.headers.common['Content-Type'] = 'application/json';

// 查询所有支付记录
export const fetchPayments = async (searchTerm = '') => {
  try {
    const response = await axios.get('/payments', {
      params: { search: searchTerm }, // 支持搜索参数
    });
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch payments: ' + error.message);
  }
};

// 添加支付记录（支持 pacs.008 和 pacs.009）
export const addPayment = async (paymentData) => {
  const url = paymentData.messageType === 'pacs.008' ? '/process-pacs008' : '/process-pacs009';
  try {
    const response = await axios.post(url, paymentData);
    return response.data;
  } catch (error) {
    throw new Error('Failed to add payment: ' + error.message);
  }
};

// 更新支付记录
export const updatePayment = async (id, paymentData) => {
  const url = paymentData.messageType === 'pacs.008' ? `/process-pacs008/${id}` : `/process-pacs009/${id}`;
  try {
    const response = await axios.put(url, paymentData);
    return response.data;
  } catch (error) {
    throw new Error('Failed to update payment: ' + error.message);
  }
};

// 删除支付记录
export const deletePayment = async (id) => {
  try {
    const response = await axios.delete(`/payments/${id}`);
    return response.data;
  } catch (error) {
    throw new Error('Failed to delete payment: ' + error.message);
  }
};

// 查询特定类型的支付记录（可选扩展）
export const fetchPaymentsByType = async (messageType) => {
  try {
    const response = await axios.get('/payments', {
      params: { messageType },
    });
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch payments by type: ' + error.message);
  }
};

// 新增 Office 相关函数
export const fetchOffices = async () => {
  try {
    const response = await axios.get('/offices');
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch offices: ' + error.message);
  }
};

export const addOffice = async (officeData) => {
  try {
    const response = await axios.post('/offices', officeData);
    return response.data;
  } catch (error) {
    throw new Error('Failed to add office: ' + error.message);
  }
};

export const updateOffice = async (officeId, officeData) => {
  try {
    const response = await axios.put(`/offices/${officeId}`, officeData);
    return response.data;
  } catch (error) {
    throw new Error('Failed to update office: ' + error.message);
  }
};

export const deleteOffice = async (officeId) => {
  try {
    const response = await axios.delete(`/offices/${officeId}`);
    return response.data;
  } catch (error) {
    throw new Error('Failed to delete office: ' + error.message);
  }
};