import React from 'react'
import Header from 'components/app/header'

/**
 * Top-level application component.
 * The common page elements are rendered here (menus, logos etc).
 * react-router passes the active page contents in 'props.children'.
 */
const App = () => (
    <div>
        <h2>Text analysis tool</h2>
        <Header/>
    </div>
)

export default App
