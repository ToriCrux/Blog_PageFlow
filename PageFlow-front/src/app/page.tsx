// src/app/page.tsx
import React from 'react';

import { Metadata } from 'next';
import Login from './Login/page';

export const metadata: Metadata = {
  title: 'Home | Projeto de Bloco'
}

const HomePage: React.FC = () => {
  return <Login />;
};

export default HomePage;
