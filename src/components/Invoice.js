import React from 'react';
import './Invoice.css'; // Fichier CSS pour le style
import MyCard from './Card';
import { useCart } from './CartContext';

const Invoice = ({}) => {
    const { cartItems, setCartItems} = useCart();
    return (
        <div className="invoice-container">
            <div className="header">
                <h2>Facture</h2>
                <div className="invoice-details">
                    <div>Numéro de facture:455555 </div>
                    <div>Date:15-02-2026 </div>
                </div>
            </div>
            <div className="customer-details">
                <h3>Client</h3>
                <div>Nom:yyhhhhh </div>
                <div>Adresse:fhhhjkjkilil</div>
            </div>
            <div className="items">
                <h3>Articles</h3>
                <table>
                    <thead>
                        <tr>
                            <th>Nom</th>
                            <th>Quantité</th>
                            <th>Prix unitaire</th>
                            <th>Total</th>
                        </tr>
                    </thead>
                    <tbody>
                      
                      {cartItems.map((item, index) => {
                        return(
                            <tr key={index}>
                                 <td>{item.nom}</td>
                                <td>{item.qte}</td>
                                <td>{item.prix}</td>
                            </tr>
                        )
                
                      
                
                        })}
                     
                    </tbody>
                </table>
            </div>
            <div className="total">
                <h3>Total: </h3>
            </div>
        </div>
    );
};

//const calculateTotal = (items) => {
  //  return items.reduce((total, item) => total + item.quantity * item.unitPrice, 0);
//};

export default Invoice;
