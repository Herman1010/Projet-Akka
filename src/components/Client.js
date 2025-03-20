import React, { useState, useEffect} from "react";
import axios from 'axios';
import './Article.css'
import { Link } from "react-router-dom";

const Client = () => {
    const [clients, setClients] = useState([]);
    const [deleteMsg, setDeleteMsg]=useState(false)

    const DeleteClient = async(id)=>{
        try{
            await axios.delete(`http://localhost:8080/api/users/${id}`)
            const response = await axios.get('http://localhost:8080/api/users');
            setDeleteMsg(true);
            //inputRef.current.scrollIntoView({ behavior: 'smooth' });
            setClients(response.data);
        }catch(error) {
            console.error('Something went wrong!!', error);
        }
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/users');
                if (response) {
                    setClients(response.data);
                }
            } catch (error) {
                console.error('Something went wrong!', error);
            }
        };
        fetchData();
    }, []);

    return (
        <div style={styles.container} >
            <div style={styles.overlay}></div>
            <h1 style={styles.heading}>Clients</h1>
            <Link className="btn-primary" to='/clients/create'>Add Client</Link> 
            { deleteMsg &&
                <div style={{ backgroungColor: '#34cd60', color:'#34cd60', padding:'10px', borderRadius:'5px', zIndex:2}}>
                    Le client a été supprimé avec succès
                </div> 
            }
            <div style={styles.cardContainer}>
                {clients.map((client) => (
                    <div key={client.id} className="list-group-item">
                        <h3>{client.nom} <br></br></h3>
                        <p>Adresse: {client.email}</p> 
                        
                        <button onClick={()=>DeleteClient(client.id)} className="btn-danger">
                           Delete
                        </button>
                    </div>
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

export default Client;
