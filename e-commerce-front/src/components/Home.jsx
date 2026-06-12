import React from 'react';
import { Link } from 'react-router-dom';
import './Home.css';

const Home = () => (
  <main className="home">
    <section className="home__hero">
      <span className="home__eyebrow">Electrónica &amp; Tech</span>
      <h1 className="home__title">Los mejores<br />productos al mejor<br />precio.</h1>
      <p className="home__subtitle">
        Explorá nuestro catálogo, guardá tus favoritos y comprá con seguridad.
      </p>
      <div className="home__actions">
        <Link to="/products" className="home__cta-primary">Ver productos</Link>
        <Link to="/auth" className="home__cta-ghost">Crear cuenta</Link>
      </div>
    </section>

    <section className="home__features">
      <div className="home__feature">
        <div className="home__feature-mark">01</div>
        <h3>Catálogo completo</h3>
        <p>Smartphones, notebooks, periféricos y más. Todo en un solo lugar.</p>
      </div>
      <div className="home__feature">
        <div className="home__feature-mark">02</div>
        <h3>Favoritos y carrito</h3>
        <p>Guardá los productos que te interesan y comprá cuando estés listo.</p>
      </div>
      <div className="home__feature">
        <div className="home__feature-mark">03</div>
        <h3>Tu cuenta, tu historial</h3>
        <p>Gestioná tus datos y seguí tus compras desde un solo panel.</p>
      </div>
    </section>
  </main>
);

export default Home;
