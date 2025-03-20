import React, { useState, useEffect } from "react";
import axios from "axios";
import PortefeuilleCard from "./PortefeuilleCard";
import "./Portefeuille.css";

const Portefeuille = () => {
    const [portefeuilles, setPortefeuilles] = useState([]);
    const [deleteMsg, setDeleteMsg] = useState(false);
    const [newPortefeuille, setNewPortefeuille] = useState({
        utilisateur_id: 1,
        nom: "",
        devise: "",
        valeurInitiale: ""
    });
    const [errorMessage, setErrorMessage] = useState("");
    const devises = ['USD', 'EUR', 'GBP', 'BTC', 'ETH'];
    useEffect(() => {
        const fetchPortefeuilles = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/portefeuilles');
                setPortefeuilles(response.data);
            } catch (error) {
                console.error("Erreur lors de la récupération des portefeuilles", error);
            }
        };
        fetchPortefeuilles();
    }, []);

    const deletePortefeuille = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/api/portefeuilles/${id}`);
            setDeleteMsg(true);
            setPortefeuilles(portefeuilles.filter((portefeuille) => portefeuille.id !== id));
        } catch (error) {
            console.error("Erreur lors de la suppression du portefeuille", error);
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setNewPortefeuille((prevState) => ({
            ...prevState,
            [name]: value === "" ? "" : isNaN(value) ? value : Number(value)
        }));
        setErrorMessage("");
    };

    const addPortefeuille = async () => {
        if (isNaN(newPortefeuille.valeurInitiale) || Number(newPortefeuille.valeurInitiale) <= 0) {
            setErrorMessage("La valeur initiale doit être un nombre positif.");
            return;
        }
        if (!newPortefeuille.nom || !newPortefeuille.devise || !newPortefeuille.valeurInitiale) {
            setErrorMessage("Tous les champs doivent être remplis.");
            return;
        }


        const payload = {
            utilisateurId: newPortefeuille.utilisateur_id,
            nom: newPortefeuille.nom,
            devise: newPortefeuille.devise,
            valeurInitiale: newPortefeuille.valeurInitiale
        };

        try {
            const response = await axios.post('http://localhost:8080/api/portefeuilles', payload);
            setPortefeuilles([...portefeuilles, response.data]);
            setNewPortefeuille({ utilisateur_id: 1, nom: "", devise: "", valeurInitiale: "" });
            setErrorMessage("");
            const fetchPortefeuilles = async () => {
                try {
                    const response = await axios.get('http://localhost:8080/api/portefeuilles');
                    setPortefeuilles(response.data);
                } catch (error) {
                    console.error("Erreur lors de la récupération des portefeuilles", error);
                }
            };
            fetchPortefeuilles();
        } catch (error) {
            console.error("Erreur lors de l’ajout du portefeuille", error);
            setErrorMessage("Une erreur est survenue lors de l'ajout du portefeuille.");
        }
    };

    return (
        <div>
            <h1>Portefeuille</h1>
            {deleteMsg && <div>La position a été supprimée avec succès</div>}

            {errorMessage && <div style={{ color: 'red', marginBottom: '10px' }}>{errorMessage}</div>}

            <div className="portefeuille-form">
            <input
                type="text"
                name="nom"
                placeholder="Nom du Portefeuille"
                value={newPortefeuille.nom}
                onChange={handleInputChange}
            />

            <select
                name="devise"
                value={newPortefeuille.devise}
                onChange={handleInputChange}
            >
                {devises.map((devise) => (
                    <option key={devise} value={devise}>
                        {devise}
                    </option>
                ))}
            </select>

            <input
                type="number"
                name="valeurInitiale"
                placeholder="Valeur Initiale"
                value={newPortefeuille.valeurInitiale}
                onChange={handleInputChange}
            />

            <button className="btn-primary" onClick={addPortefeuille}>Ajouter un Portefeuille</button>
        </div>

            <div className="portefeuille-list">
                {portefeuilles.map((portefeuille) => (
                    <div className="portefeuille-card" key={portefeuille.id}>
                        <PortefeuilleCard portefeuille={portefeuille} onDeletePortefeuille={deletePortefeuille} />
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Portefeuille;
