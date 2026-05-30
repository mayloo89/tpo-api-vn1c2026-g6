import React from 'react';
import { Link } from 'react-router-dom';
import './Home.css';

const Home = () => {
return (
<div className="home">
<section className="home__hero">
<h1 className="home__title">Bienvenido a E-Commerce</h1>
<p className="home__subtitle">
Tu tienda online con los mejores productos al mejor precio
</p>
<Link to="/products" className="home__cta">
Ver Productos
</Link>
</section>
</div>
);
};

export default Home;
