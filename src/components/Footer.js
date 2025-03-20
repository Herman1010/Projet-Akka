import React from 'react';

const Footer = () => {
    return (
        <footer style={styles.footer}>
            <p style={styles.text}>© 2025 PROJECT Scala. All Rights Reserved.</p>
        </footer>
    );
};

const styles = {
    footer: {
        backgroundColor: '#333',
        color: 'white',
        textAlign: 'center',
        padding: '10px 20px',
        position: 'fixed',
        left: '0',
        bottom: '0',
        width: '100%'
    },
    text: {
        margin: '0'
    }
};

export default Footer;
