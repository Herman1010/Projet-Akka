// CartContext.js
import React, { createContext, useContext, useState } from 'react';

// Créer un contexte
const CartContext = createContext();

// Fournisseur de contexte pour gérer l'état global du panier
export const CartProvider = ({ children }) => {
  const [cartItems, setCartItems] = useState([]);

  return (
    <CartContext.Provider value={{ cartItems, setCartItems }}>
      {children}
    </CartContext.Provider>
  );
};

// Utilitaire pour utiliser le contexte du panier dans les composants
export const useCart = () => useContext(CartContext);
