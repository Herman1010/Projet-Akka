import React from 'react';

const Background = () => {
    return (
        <div style={styles.container}>
            
        </div>
    );
};

const styles = {
    container: {
        height: '100vh',  // Utilise 100% de la hauteur de la fenêtre
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',  // Centre verticalement
        alignItems: 'center',  // Centre horizontalement
        backgroundImage: 'url("https://www.latribudesexperts.fr/wp-content/uploads/2021/03/outils-pour-creer-boutique-en-ligne.jpg")', // Ajoutez votre propre URL ici
        backgroundSize: 'cover',  // Couvre toute la zone visible sans changer les proportions de l'image
        backgroundPosition: 'center',  // Centre l'image dans le conteneur
        backgroundRepeat: 'no-repeat',
        width: '100%',  // Utilise 100% de la largeur
        position: 'relative',  // Assure que le footer ne chevauche pas
    },
    heading: {
        color: 'white',  // Choisissez une couleur qui se démarque sur votre image de fond
        fontSize: '48px',  // Taille du texte
        textShadow: '2px 2px 8px rgba(0, 0, 0, 0.8)'  // Ajoute un ombrage pour améliorer la lisibilité
    }
};

export default Background;
