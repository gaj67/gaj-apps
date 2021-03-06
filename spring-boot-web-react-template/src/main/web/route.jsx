import React from 'react'
import ReactDOM from 'react-dom'
import { Router, Route, IndexRoute, browserHistory } from 'react-router'
import App from 'components/app/app'
import About from 'components/app/about'
import Home from 'components/app/home'

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
	<Router history={browserHistory}>
		<Route path="/" component={App}>
			<IndexRoute component={Home} />
			<Route path="about" component={About} />
		</Route>
	</Router>
), document.getElementById('app'))