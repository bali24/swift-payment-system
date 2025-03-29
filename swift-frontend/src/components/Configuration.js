// src/components/Configuration.js
import React from 'react';
import { Link } from 'react-router-dom';

const Configuration = () => {
  return (
    <div className="container-fluid p-4">
      <h1 className="mt-4">Configuration</h1>
      <p>请选择一个子菜单以配置系统设置。</p>
      <ul className="list-group">
        <li className="list-group-item">
          <Link to="/configuration/customer">Customer</Link> - 管理客户信息
        </li>
        <li className="list-group-item">
          <Link to="/configuration/account">Account</Link> - 管理账户信息
        </li>
        <li className="list-group-item">
          <Link to="/configuration/channels">Channels</Link> - 管理渠道设置
        </li>
        <li className="list-group-item">
          <Link to="/configuration/rma">RMA</Link> - 管理 RMA 设置
        </li>
      </ul>
    </div>
  );
};

export default Configuration;