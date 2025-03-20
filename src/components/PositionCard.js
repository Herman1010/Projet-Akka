import React, { useEffect, useState } from 'react';
import axios from "axios";

const PositionCard = ({ position, onDeletePosition }) => {
    const [portefeuilles, setPortefeuilles] = useState([]);
    const [actifs, setActifs] = useState([]);

    const [portefeuilleName, setPortefeuilleName] = useState('');
    const [actifName, setActifName] = useState('');

    useEffect(() => {
        const fetchPortefeuilles = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/portefeuilles');
                setPortefeuilles(response.data);
            } catch (error) {
                console.error('Erreur lors de la récupération des portefeuilles:', error);
            }
        };
        fetchPortefeuilles();
    }, []);

    useEffect(() => {
        const fetchActifs = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/actifs');
                setActifs(response.data);
            } catch (error) {
                console.error('Erreur lors de la récupération des actifs:', error);
            }
        };
        fetchActifs();
    }, []);

    useEffect(() => {
        const portefeuille = portefeuilles.find(p => p.id === position.portefeuille_id);
        if (portefeuille) {
            setPortefeuilleName(portefeuille.nom);
        }
    }, [portefeuilles, position.portefeuille_id]);

    useEffect(() => {
        const actif = actifs.find(a => a.id === position.actif_id);
        if (actif) {
            setActifName(actif.nom);
        }
    }, [actifs, position.actif_id]);

    return (
        <div className="list-group-item">
            <p><strong>Portefeuille:</strong> {portefeuilleName}</p>
            <p><strong>Actif:</strong> {actifName}</p>
            <p><strong>Quantité:</strong> {position.quantite}</p>
            <p><strong>Prix Achat:</strong> {position.prix_achat} €</p>
            <p><strong>Date Achat:</strong> {position.date_achat}</p>
            <button onClick={() => onDeletePosition(position.id)} className='btn-danger'>Vendre</button>
        </div>
    );
};

export default PositionCard;
