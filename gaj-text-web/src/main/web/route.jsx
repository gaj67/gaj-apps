import React from 'react'
import ReactDOM from 'react-dom'
import { BrowserRouter as Router } from 'react-router-dom';
import App from 'components/app/app'

/**
 * 'require("{resource_name}")' statements act as "imports" for assets.
 * Webpack will resolve the path and convert resources to embedded scripts or URLs.
 */
require("main.styl");

/**
 * Route configuration for the Application
 * 
 * Also see components/app/header.jsx.
 */
ReactDOM.render((
	<Router>
	    <App/>
	</Router>
), document.getElementById('app'))