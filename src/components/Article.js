import React, { useState, useEffect } from "react";
import axios from 'axios';
import { Link } from "react-router-dom";
import PositionCard from "./PositionCard";

const Position = () => {
    const [positions, setPositions] = useState([]);
    const [deleteMsg, setDeleteMsg] = useState(false);
    const [newPosition, setNewPosition] = useState({
        portefeuille_id: '',
        actif_id: '',
        quantite: '',
        prix_achat: '',
        date_achat: ''
    });

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/position');
                setPositions(response.data);
            } catch (error) {
                console.error('Erreur lors du chargement des positions', error);
            }
        };
        fetchData();
    }, []);

    const deletePosition = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/api/position/${id}`);
            setDeleteMsg(true);
            setPositions(positions.filter(pos => pos.id !== id));
        } catch (error) {
            console.error('Erreur lors de la suppression', error);
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setNewPosition(prevState => ({
            ...prevState,
            [name]: value === "" ? "" : isNaN(value) ? value : Number(value) // Convertir en nombre
        }));
    };


    const addPosition = async () => {
        const date = new Date(newPosition.date_achat).toISOString().replace("Z", ""); // Supprime "Z"

        const payload = {
            portefeuille_id: newPosition.portefeuille_id || 0,
            actif_id: newPosition.actif_id || 0,
            quantite: newPosition.quantite || 0,
            prix_achat: newPosition.prix_achat || 0,
            date_achat: date
        };

        console.log("Payload envoyé :", payload);

        try {
            const response = await axios.post('http://localhost:8080/api/position', payload);
            setPositions([...positions, response.data]);
            setNewPosition({ portefeuille_id: '', actif_id: '', quantite: '', prix_achat: '', date_achat: '' });
        } catch (error) {
            console.error('Erreur lors de l’ajout', error);
        }
    };



    return (
        <div>
            <h1>Positions</h1>
            <Link to='/position/create'>Créer une Position</Link>
            {deleteMsg && <div>La position a été supprimée avec succès</div>}

            <input type="number" name="portefeuille_id" placeholder="Portefeuille ID" value={newPosition.portefeuille_id} onChange={handleInputChange} />
            <input type="number" name="actif_id" placeholder="Actif ID" value={newPosition.actif_id} onChange={handleInputChange} />
            <input type="number" name="quantite" placeholder="Quantité" value={newPosition.quantite} onChange={handleInputChange} />
            <input type="number" name="prix_achat" placeholder="Prix Achat" value={newPosition.prix_achat} onChange={handleInputChange} />
            <input type="date" name="date_achat" value={newPosition.date_achat} onChange={handleInputChange} />
            <button onClick={addPosition}>Ajouter</button>

            <div>
                {positions.map((position) => (
                    <PositionCard key={position.id} position={position} onDeletePosition={deletePosition} />
                ))}
            </div>
        </div>
    );
};

export default Position;
