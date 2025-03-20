import React from 'react';

const Navbar = () => {
    return (
        <nav style={styles.navbar}>
            <ul style={styles.navList}>
                <li style={styles.navItem}><a style={styles.navLink} href="/">Home</a></li>
                <li style={styles.navItem}><a style={styles.navLink} href="/login">Login</a></li>
                <li style={styles.navItem}><a style={styles.navLink} href="/portfolio">Portfolio</a></li>
                <li style={styles.navItem}><a style={styles.navLink} href="/articles">Article</a></li>
                <li style={styles.navItem}><a style={styles.navLink} href="/clients">Client</a></li>
            </ul>
        </nav>
    );
};

const styles = {
    navbar: {
        backgroundColor: '#333',
        color: 'white',
        padding: '10px 20px',
    },
    navList: {
        listStyle: 'none',
        display: 'flex',
        justifyContent: 'left',
        margin: 0,
        padding: 0,
    },
    navItem: {
        margin: '0 10px',
    },
    navLink: {
        color: 'white',
        textDecoration: 'none',
        fontSize: '16px',
        fontWeight: 'bold',
    }
};

export default Navbar;
