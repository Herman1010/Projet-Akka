import React, { useState, useEffect } from 'react';
import jsPDF from 'jspdf';
import './InvoiceComponent.css'; // Importer le fichier de style CSS

function InvoiceComponent() {
    const [step, setStep] = useState(1);
    const [selectedArticles, setSelectedArticles] = useState([]);
    const [clientName, setClientName] = useState('');
    const [articles, setArticles] = useState([]);
    const [clients, setClients] = useState([]);
    const [quantities, setQuantities] = useState({});
    const [invoiceNumber, setInvoiceNumber] = useState('');

    useEffect(() => {
        
        fetch('http://localhost:3000/api/articles')
            .then(response => response.json())
            .then(data => setArticles(data))
            .catch(error => console.error('Erreur lors de la récupération des articles:', error));

       
        fetch('http://localhost:3000/api/clients')
            .then(response => response.json())
            .then(data => setClients(data))
            .catch(error => console.error('Erreur lors de la récupération des clients:', error));
    }, []);

    const nextStep = () => {
        setStep(step + 1);
    }

    const handleArticleSelection = (event) => {
        const selectedOptions = Array.from(event.target.selectedOptions, option => option.value);
        setSelectedArticles(selectedOptions);
    }




    const generateInvoicez = () => {
        let doc = new jsPDF();
        let y = 20;
    
        // Titre de la facture
        doc.setFontSize(20);
        doc.text("Facture", 105, y, { align: 'center' });
    
        // Informations sur le client
        doc.setFontSize(12);
        y += 10;
        doc.text("Facturé à : " + clientName, 10, y);
        y += 7;
        doc.text("Numéro de facture : " + invoiceNumber, 10, y);
    
        // Liste des articles achetés
        y += 15;
        doc.setFontSize(14);
        doc.text("Articles :", 10, y);
        y += 7;
        let totalPrice = 0;
        selectedArticles.forEach(articleId => {
            const article = articles.find(a => a.id === articleId);
            let quantity = parseInt(quantities[articleId]);
            let price = article.prix * quantity;
            totalPrice += price;
            doc.text(article.nom + " - Quantité : " + quantity + " - Prix : " + price.toFixed(2) + " €", 20, y);
            y += 7;
        });
    
        // Prix total
        y += 7;
        doc.setFontSize(14);
        doc.text("Total :  ", 10, y);
        doc.text(" "+totalPrice.toFixed(2) + " €", 20, y);
    
        // Téléchargement du fichier PDF
        doc.save("facture.pdf");
    }
    

    const generateInvoice = () => {
        let doc = new jsPDF();
        let y = 20;
    
        // Titre de la facture
        doc.setTextColor(44, 62, 80); // Couleur du texte
        doc.setFontSize(20);
        doc.text("Facture", 105, y, { align: 'center' });
        y += 10;
    
        // Informations sur le client
        doc.setFontSize(12);
        doc.text("Facturé à : " + clientName, 10, y);
        y += 7;
        doc.text("Numéro de facture : " + invoiceNumber, 10, y);
        y += 7;
        // Date système
        let currentDate = new Date().toLocaleDateString();
        doc.text("Date : " + currentDate, 10, y);
        y += 15;
    
        // Tableau des articles achetés
        doc.setFillColor(41, 128, 185); // Couleur de fond du tableau
        doc.setTextColor(0, 0, 0); // Couleur du texte dans le tableau
        doc.setFontSize(14);
        let headers = ["Nom", "Prix unitaire", "Quantité", "Total"];
        let tableRows = [headers];
        let totalPrice = 0;
    
        // Ajouter chaque article au tableau
        selectedArticles.forEach(articleId => {
            const article = articles.find(a => a.id === articleId);
            let quantity = parseInt(quantities[articleId]);
            let price = article.prix;
            let totalItemPrice = price * quantity;
            totalPrice += totalItemPrice;
    
            tableRows.push([article.nom, price.toFixed(2) + " €", ""+quantity, totalItemPrice.toFixed(2) + " €"]);
        });
    
        // Dessiner le tableau manuellement avec des bordures et des couleurs
        let tableColWidths = [90, 40, 30, 30]; // Largeur des colonnes
        let tableY = y;
        tableRows.forEach((rowData, rowIndex) => {
            rowData.forEach((cellData, cellIndex) => {
                if (rowIndex === 0) {
                    doc.setFillColor(44, 62, 80); // Couleur de fond pour la première ligne (en-têtes)
                    doc.setTextColor(255,255,255);
                } else {
                    doc.setFillColor(200, 200, 200); // Couleur de fond pour les autres lignes (données)
                    doc.setTextColor(0,0,0);
                }
                doc.rect(10 + (tableColWidths.slice(0, cellIndex).reduce((a, b) => a + b, 0)), tableY, tableColWidths[cellIndex], 7, 'F');
                doc.text(cellData, 10 + (tableColWidths.slice(0, cellIndex).reduce((a, b) => a + b, 0)) + 1, tableY + 5);
            });
            tableY += 7;
        });
    
        // Prix total
        doc.setFillColor(41, 128, 185); // Couleur de fond pour la zone du total
        doc.setTextColor(255, 255, 255); // Couleur du texte pour le total
        doc.rect(10, tableY + 10, 40, 7, 'F'); // Rectangle de fond pour le total
        doc.setFontSize(14);
        doc.text("Total :", 10, tableY + 15);
        doc.setTextColor(255,255,200);
        doc.text(totalPrice.toFixed(2) + " €", 25, tableY + 15);
    
        // Téléchargement du fichier PDF
        doc.save("facture N°"+invoiceNumber+".pdf");
    }
    
    
    const renderStepContent = () => {
        switch (step) {
            case 1:
                return (
                    <div className="step-container">
                        <h3>Étape 1 : Choix des articles</h3>
                        <select id="articles" multiple onChange={handleArticleSelection}>
                            {articles.map(article => (
                                <option key={article.id} value={article.id}>{article.nom}</option>
                            ))}
                        </select>
                        <button className='btn btn-primary' onClick={nextStep}>Suivant</button>
                    </div>
                );
            case 2:
                return (
                    <div className="step-container">
                        <h3>Étape 2 : Information du client</h3>
                        <select value={clientName} onChange={(e) => setClientName(e.target.value)}>
                            {clients.map(client => (
                                <option key={client.id} value={client.nom}>{client.nom}</option>
                            ))}
                        </select>
                        <button className='btn btn-primary' onClick={nextStep}>Suivant</button>
                    </div>
                );
            case 3:
                return (
                    <div className="step-container">
                        <h3>Étape 3 : Sélection des quantités et numéro de facture</h3>
                        <table>
                            <thead>
                                <tr>
                                    <th>Article</th>
                                    <th>Quantité</th>
                                </tr>
                            </thead>
                            <tbody>
                                {selectedArticles.map(articleId => {
                                    const article = articles.find(a => a.id === articleId);
                                    return (
                                        <tr key={articleId}>
                                            <td>{article.nom}</td>
                                            <td><input type="number" value={quantities[articleId] || ''} onChange={(e) => setQuantities({ ...quantities, [articleId]: e.target.value })} placeholder="Quantité" /></td>
                                        </tr>
                                    );
                                })}
                            </tbody>
                        </table>
                        <input type="text" value={invoiceNumber} onChange={(e) => setInvoiceNumber(e.target.value)} placeholder="Numéro de facture" />
                        <button className='btn btn-sucess' onClick={generateInvoice}>Générer facture</button>
                    </div>
                );
            default:
                return null;
        }
    }

    return (
        <div style={styles.container}>
            <div style={styles.overlay}></div>
            <h1 style={styles.heading}>Facturation</h1>
            <div className="progress-bar">
                <div className={`step ${step === 1 ? 'active' : ''}`}>1</div>
                <div className={`step ${step === 2 ? 'active' : ''}`}>2</div>
                <div className={`step ${step === 3 ? 'active' : ''}`}>3</div>
            </div>
            {renderStepContent()}
        </div>
    );
}



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


export default InvoiceComponent;
