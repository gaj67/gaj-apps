import React from 'react'

class Analysis extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            value: ''
        };
        
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }
    
    handleChange(event) {
        this.setState({value: event.target.value});
    }
    
    handleSubmit(event) {
        alert('Text submitted: ' + this.state.value);
        event.preventDefault();
    }
    
    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <label>Text input:</label>
                <br/>
                <textarea cols={100} rows={5} value={this.state.value} onChange={this.handleChange} />
                <br/>
                <input type="submit" value="Analyse" />
            </form>
        );
    }
}

export default Analysis