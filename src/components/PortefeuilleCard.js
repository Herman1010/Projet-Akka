import React from 'react';
import './Portefeuille.css';

const PortefeuilleCard = ({ portefeuille, onDeletePortefeuille }) => {
    return (
        <div className="list-group-item">
            <p><strong>Nom:</strong> {portefeuille.nom}</p>
            <p><strong>Devise:</strong> {portefeuille.devise}</p>
            <p><strong>Valeur Initiale:</strong> {portefeuille.valeurInitiale}</p>
            <button onClick={() => onDeletePortefeuille(portefeuille.id)} className='btn-danger'>Supprimer</button>
        </div>
    );
};

export default PortefeuilleCard;
