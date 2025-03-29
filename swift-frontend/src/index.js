// src/index.js
import React from 'react';
import { createRoot } from 'react-dom/client'; // 引入 createRoot
import App from './App';
import 'bootstrap/dist/js/bootstrap.bundle.min';

// 获取挂载点
const container = document.getElementById('root');

// 创建根节点
const root = createRoot(container);

// 渲染应用
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);