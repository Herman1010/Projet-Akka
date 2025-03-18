// Card.js
import React from 'react';
import './Article.css';


const Card = ({ article, onAddToCart, onDeleteArticle }) => {
  return (
    <div className="list-group-item">
      <h3>{article.nom}</h3>
      <p>{article.description}</p>
      <p>Prix: {article.prix}€</p>
      <p>Quantité en stock: {article.qte}</p>
      {/* Boutons pour ajouter l'article au panier et supprimer l'article */}
      <button onClick={() => onAddToCart(article)} className='btn-primary'>Ajouter au panier</button>
      <button onClick={() => onDeleteArticle(article.id)} className='btn-danger'>Supprimer</button>
    </div>
  );
};

export default Card;
