/*import { useState, useEffect } from "react";
import "./PortfolioDashboard.css"; // Import du fichier CSS

const PortfolioDashboard = () => {
  const [portfolio, setPortfolio] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Simuler l'appel API pour récupérer les données du portefeuille
  useEffect(() => {
    const fetchPortfolio = async () => {
      try {
        const response = await fetch("https://localhost:8080/api/portefeuilles"); // Remplace par l'URL de ton API
        if (!response.ok) {
          throw new Error("Impossible de récupérer les données du portefeuille");
        }
        const data = await response.json();
        setPortfolio(data); // Supposons que la réponse a la structure { totalValue, assets }
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchPortfolio();
  }, []);

  if (loading) {
    return <div className="loading">Chargement...</div>;
  }

  if (error) {
    return <div className="error">Erreur : {error}</div>;
  }

  const totalValue = portfolio.totalValue;
  const assets = portfolio.assets;

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <h1>Dashboard du Portefeuille</h1>
        <div className="portfolio-summary">
          <h2>Valeur Totale</h2>
          <p>${totalValue.toLocaleString()}</p>
        </div>
      </div>
      <div className="assets-list">
        <h3>Répartition des Investissements</h3>
        <ul>
          {assets.map((asset, index) => (
            <li key={index} className="asset-item">
              <span>{asset.type}</span>
              <span>${asset.value.toLocaleString()}</span>
              <div
                className="asset-bar"
                style={{ width: `${(asset.value / totalValue) * 100}%` }}
              />
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default PortfolioDashboard;*/


import React from 'react';
import './PortfolioDashboard.css';

const Dashboard = () => {
  // Données statiques pour tester
  const portfolio = [
    { name: 'Apple', quantity: 10, price: 150, initialValue: 130 },
    { name: 'Tesla', quantity: 5, price: 700, initialValue: 650 },
    { name: 'Amazon', quantity: 3, price: 3000, initialValue: 2800 },
    { name: 'Google', quantity: 7, price: 2800, initialValue: 2500 },
    { name: 'Microsoft', quantity: 8, price: 200, initialValue: 180 }
  ];

  // Calculs
  const totalBalance = portfolio.reduce((acc, stock) => acc + stock.quantity * stock.price, 0);
  const totalChange = portfolio.reduce((acc, stock) => acc + (stock.quantity * stock.price - stock.quantity * stock.initialValue), 0);

  return (
    <div style={styles.container}>
        <div style={styles.overlay}></div>
    <div style={styles.cardContainer} className="dashboard-container">
      <div className="header">
        <h1>Tableau de Bord du Portefeuille</h1>
        <p>Suivi de vos investissements</p>
      </div>
      <div className="summary">
        <div className="summary-item">
          <h2>Total du Portefeuille</h2>
          <p className="value">${totalBalance.toFixed(2)}</p>
        </div>
        <div className="summary-item">
          <h2>Variation Totale</h2>
          <p className={`value ${totalChange >= 0 ? 'positive' : 'negative'}`}>
            {totalChange >= 0 ? '+' : ''}${totalChange.toFixed(2)}
          </p>
        </div>
      </div>
      <div className="portfolio-details">
        <h2>Détails du Portefeuille</h2>
        <table>
          <thead>
            <tr>
              <th>Action</th>
              <th>Quantité</th>
              <th>Prix Actuel</th>
              <th>Valeur Initiale</th>
              <th>Variation</th>
            </tr>
          </thead>
          <tbody>
            {portfolio.map((stock, index) => (
              <tr key={index}>
                <td>{stock.name}</td>
                <td>{stock.quantity}</td>
                <td>${stock.price.toFixed(2)}</td>
                <td>${(stock.quantity * stock.initialValue).toFixed(2)}</td>
                <td className={stock.price > stock.initialValue ? 'positive' : 'negative'}>
                  {stock.price > stock.initialValue ? '+' : ''}
                  ${(stock.quantity * (stock.price - stock.initialValue)).toFixed(2)}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <div className="chart-section">
        <h2>Graphiques d'Analyse</h2>
        {/* Graphiques peuvent être ajoutés ici avec des bibliothèques comme Chart.js */}
      </div>
    </div>
    </div>
  );
};
const styles = {
    container: {
        height: '100vh',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        backgroundImage: 'url("https://www.latribudesexperts.fr/wp-content/uploads/2021/03/outils-pour-creer-boutique-en-ligne.jpg")',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat',
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        width: '100%',
        position: 'relative',
    },
    heading: {
        color: 'white',
        fontSize: '48px',
        textShadow: '2px 2px 8px rgba(0, 0, 0, 0.8)',
        position: 'relative',
        zIndex: 1,
    },
    cardContainer: {
        color: 'black',
        zIndex: 2,
        display: 'flex',
        flexWrap: 'wrap',
        justifyContent: 'space-around',
        marginTop: '20px',
        width: '80%', /* Largeur de la carte container */
    },
    overlay: {
        position: 'absolute',
        top: 0,
        left: 0,
        width: '100%',
        height: '100%',
        backgroundColor: 'rgba(0, 0, 0, 0.7)',
    },
};
export default Dashboard;
