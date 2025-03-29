// src/components/BaseFiles.js
import React from 'react';
import { Link } from 'react-router-dom';

const BaseFiles = () => {
  return (
    <div className="container-fluid p-4">
      <h1 className="mt-4">Base Files</h1>
      <p>请选择一个子菜单以管理基础文件。</p>
      <ul className="list-group">
        <li className="list-group-item">
          <Link to="/base-files/office">Office</Link> - 管理办公室信息
        </li>
        <li className="list-group-item">
          <Link to="/base-files/department">Department</Link> - 管理部门信息
        </li>
      </ul>
    </div>
  );
};

export default BaseFiles;