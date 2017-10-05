import React from 'react'
import { Link } from 'react-router'

/**
 * Adds a header bar with links to sub-pages.
 * 
 * Also see route.jsx.
 */
const Header = React.createClass({

    render() {
        return (
            <div>
                <nav className="navbar navbar-default">
                    <ul className="nav navbar-nav">
                		<li><img src={require("components/app/Penfold.gif")} className="logosq"/></li>
                        <li><Link to="/">Home</Link></li>
                        <li><Link to="/analysis">Analysis</Link></li>
                    </ul>
                </nav>
            </div>
        );
    }

});

export default Header