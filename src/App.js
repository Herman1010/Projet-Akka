import React from 'react';
import './App.css';
import Navbar from './components/Navbar';
import Background from './components/Background';
import Home from './components/Home';
import Footer from './components/Footer';
import Article from './components/Article';
import Client from './components/Client';
import Auth from './components/Auth';
import CreateClient from './components/CreateClient';
import PortfolioDashboard from './components/PortfolioDashboard';
import { BrowserRouter } from 'react-router-dom';
import { Routes, Route } from 'react-router-dom';
import CreateArticle from './components/CreateArticle';
import Invoice from './components/Invoice';
import InvoiceComponent from './components/InvoiceComponent';
import PortefeuilleCard from "./components/PortefeuilleCard";
import Portefeuille from "./components/Portefeuille";



function App() {
  return (
    <div className="App">
      <Navbar />
      <BrowserRouter>
      <Routes>
        
        <Route path='/' element={<Home />} />
        <Route path='/articles' element={<Article />} />
        <Route path='/articles/create' element={<CreateArticle />} />
        <Route path='/clients' element={<Utilisateur />} />
        <Route path='/clients/create' element={<CreateClient />} />
        <Route path='/factures' element={<Invoice/>} />
        <Route path='/login' element={<Auth/>}/>
        <Route path='/portfolio' element={<PortfolioDashboard/>}/>
        <Route path='/editFactures' element={<InvoiceComponent/>} />
          <Route path='/portefeuille' element={<Portefeuille/>} />

      </Routes>
      </BrowserRouter>
      <Footer />
    </div>
  );
}

export default App;
