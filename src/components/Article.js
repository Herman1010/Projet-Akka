import React, { useState, useEffect } from "react";
import axios from 'axios';
import { Link } from "react-router-dom";
import PositionCard from "./PositionCard";

const Position = () => {
    const [positions, setPositions] = useState([]);
    const [deleteMsg, setDeleteMsg] = useState(false);
    const [portefeuilles, setPortefeuilles] = useState([]);
    const [actifs, setActifs] = useState([]);
    const [newPosition, setNewPosition] = useState({
        portefeuille_id: '',
        actif_id: '',
        quantite: '',
        prix_achat: '',
    });
    const [errorMessage, setErrorMessage] = useState("");

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
            [name]: value === "" ? "" : isNaN(value) ? value : Number(value)
        }));
    };

    const addPosition = async () => {
        // Vérification des champs obligatoires
        if (!newPosition.portefeuille_id || !newPosition.actif_id || !newPosition.quantite || !newPosition.prix_achat) {
            setErrorMessage("Tous les champs doivent être remplis.");
            return;
        }

        // Définition de la date automatiquement (local date/heure)
        const date = new Date().toISOString().replace("Z", ""); // Supprime "Z" pour obtenir un format compatible

        const payload = {
            portefeuille_id: newPosition.portefeuille_id,
            actif_id: newPosition.actif_id,
            quantite: newPosition.quantite,
            prix_achat: newPosition.prix_achat,
            date_achat: date,
        };

        console.log("Payload envoyé :", payload);

        try {
            const response = await axios.post('http://localhost:8080/api/position', payload);
            setPositions([...positions, response.data]);
            setNewPosition({ portefeuille_id: '', actif_id: '', quantite: '', prix_achat: '' }); // Réinitialisation des champs
            setErrorMessage(""); // Réinitialiser le message d'erreur après succès
        } catch (error) {
            console.error('Erreur lors de l’ajout', error);
            setErrorMessage("Une erreur est survenue lors de l'ajout de la position.");
        }
    };

    return (
        <div>
            <h1>Positions</h1>
            {deleteMsg && <div>La position a été supprimée avec succès</div>}

            {errorMessage && <div style={{ color: 'red', marginBottom: '10px' }}>{errorMessage}</div>}

            <select
                name="portefeuille_id"
                value={newPosition.portefeuille_id}
                onChange={handleInputChange}
            >
                <option value="">Sélectionnez un Portefeuille</option>
                {portefeuilles.map((portefeuille) => (
                    <option key={portefeuille.id} value={portefeuille.id}>
                        {portefeuille.nom}
                    </option>
                ))}
            </select>

            <select
                name="actif_id"
                value={newPosition.actif_id}
                onChange={handleInputChange}
            >
                <option value="">Sélectionnez un Actif</option>
                {actifs.map((actif) => (
                    <option key={actif.id} value={actif.id}>
                        {actif.nom}
                    </option>
                ))}
            </select>
            <input type="number" name="quantite" placeholder="Quantité" value={newPosition.quantite} onChange={handleInputChange} />
            <input type="number" name="prix_achat" placeholder="Prix Achat" value={newPosition.prix_achat} onChange={handleInputChange} />
            
            {/* Suppression de l'input pour la date_achat */}
            <button onClick={addPosition}>Acheter</button>

            {/* Affichage des PositionCard avec un conteneur flex */}
            <div className="position-list">
                {positions.map((position) => (
                    <div className="position-card" key={position.id}>
                        <PositionCard position={position} onDeletePosition={deletePosition} />
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Position;
