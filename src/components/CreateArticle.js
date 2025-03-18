import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './create.css';

const CreateArticle = () => {
    const navigate = useNavigate();
    const  [newArticle, setNewArticle] = useState({});
    

    const handleChange = (e) => {
        setNewArticle({
            ...newArticle,
            [e.target.name]: e.target.value,
        });
    };
    const handleSubmit = async(e)=>{
        e.preventDefault();
        try{
            await axios.post('http://localhost:3000/api/articles', newArticle);
            navigate('/articles');
        }catch (error){
            console.error('Error creating article:', error);
        }
    };
    return (
        <div style={styles.container}>
            <div style={styles.overlay}></div>
        <div className='container mt-5'>
            <h1 className='mb-4'>Create Article</h1>
            <div className='form-group'>
                <div className='form-control'>
                    <label>Nom</label>
                    <input
                        type='text'
                        name='nom'
                        onChange={handleChange}
                        required
                        placeholder='Entrez le nom'
                    />
                </div>

                <div className='form-control'>
                    <label>Description</label>
                    <input
                        type='text'
                        name='description'
                        onChange={handleChange}
                        required
                        placeholder='Entrez la description'
                    />
                </div>

                <div className='form-control'>
                    <label>Prix</label>
                    <input
                        type='text'
                        name='prix'
                        onChange={handleChange}
                        required
                        placeholder='Entrez le prix'
                    />
                </div>

                <div className='form-control'>
                    <label>Quantité</label>
                    <input
                        type='text'
                        name='qte'
                        onChange={handleChange}
                        required
                        placeholder='Entrez la quantité'
                    />
                </div>

                <button className='custom-button' type='submit' onClick={handleSubmit}>Create Article</button>

            </div>

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
        color:'black',
        zIndex:2,
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


export default CreateArticle;