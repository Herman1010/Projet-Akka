// Article.js
import React, { useState, useEffect } from "react";
import axios from 'axios';
import './Article.css';
import { Link } from "react-router-dom";
import Card from "./Card";

import { useCart } from "./CartContext";

const Article = () => {
    const [articles, setArticles] = useState([]);
    const [deleteMsg, setDeleteMsg] = useState(false);
   // const [cartItems, setCartItems] = useState([]);
   const { cartItems, setCartItems } = useCart();

    const DeleteArticle = async (id) => {
        try {
            await axios.delete(`http://localhost:3000/api/articles/${id}`);
            const response = await axios.get('http://localhost:3000/api/articles');
            setDeleteMsg(true);
            setArticles(response.data);
        } catch (error) {
            console.error('Something went wrong!!', error);
        }
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('http://localhost:3000/api/articles');
                if (response) {
                    setArticles(response.data);
                }
            } catch (error) {
                console.error('Something went wrong!', error);
            }
        };
        fetchData();
    }, []);

    const handleAddToCart = (article) => {
        setCartItems([...cartItems, article]);
    };

    return (
        <div style={styles.container} >
            <div style={styles.overlay}></div>
            <h1 style={styles.heading}>Articles</h1>
            <Link className="btn-primary" to='/articles/create'>Create Article</Link>
            {deleteMsg &&
                <div style={{ backgroungColor: '#34cd60', color: '#34cd60', padding: '10px', borderRadius: '5px', zIndex: 2 }}>
                    L'article a été supprimé avec succès
                </div>
            }
            <div style={styles.cardContainer}>
                {articles.map((article) => (
                    <Card
                      key={article.id}
                      article={article}
                      onAddToCart={handleAddToCart}
                      onDeleteArticle={DeleteArticle}
                    />
                ))}
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

export default Article;
