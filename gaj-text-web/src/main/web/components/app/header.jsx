import React from 'react'
import { Link, Route } from 'react-router-dom';
import Home from 'components/app/home'
import Analysis from 'components/app/analysis'

/**
 * Adds a header bar with links to sub-pages.
 * 
 * Also see route.jsx.
 */
const Header = () => (
    <div>
        <nav className="navbar navbar-default">
            <ul className="nav navbar-nav">
                <li><img src={require("components/app/logo.gif")} className="logosq"/></li>
                <li><Link to="/">Home</Link></li>
                <li><Link to="/analysis">Analysis</Link></li>
            </ul>
        </nav>
        <Route exact path="/" component={Home} />
        <Route path="/analysis" component={Analysis} />
    </div>
);

export default Header