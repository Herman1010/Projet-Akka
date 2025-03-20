import React from 'react';

const PositionCard = ({ position, onDeletePosition }) => {
    return (
        <div className="list-group-item">
            <p><strong>Portefeuille ID:</strong> {position.portefeuille_id}</p>
            <p><strong>Actif ID:</strong> {position.actif_id}</p>
            <p><strong>Quantité:</strong> {position.quantite}</p>
            <p><strong>Prix Achat:</strong> {position.prix_achat}</p>
            <p><strong>Date Achat:</strong> {position.date_achat}</p>
            <button onClick={() => onDeletePosition(position.id)} className='btn-danger'>Supprimer</button>
        </div>
    );
};

export default PositionCard;
