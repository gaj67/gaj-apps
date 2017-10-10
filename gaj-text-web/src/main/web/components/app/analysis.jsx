import React from 'react'
import axios from 'axios'

class Analysis extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            input: '',
            output: ''
        };
        
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }
    
    handleChange(event) {
        this.setState({input: event.target.value});
    }
    
    handleSubmit(event) {
        alert('Text submitted: ' + this.state.input);
        event.preventDefault();
        this.setState({output: this.state.input});
        /*
        axios.get(
            'api/tokenise?text=' + encodeURIComponent(this.state.input)
        ).then(output => {
            alert('Response received: ' + output);
            this.setState({output: output});
        }).catch(error => {
            console.log(error);
        }); 
        */
    }
    
    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <div>
                    <label>Text input:</label>
                    <br/>
                    <textarea cols={100} rows={5} value={this.state.input} onChange={this.handleChange} />
                </div>
                <div className={(this.state.output == '') ? 'show-none' : 'show-block'}>
                    <br/>
                    <label>Analysed output:</label>
                    <br/>
                    <textarea cols={100} rows={5} value={this.state.output} readOnly />
                </div>
                <br/>
                <input type="submit" value="Analyse" />
            </form>
        );
    }
}

export default Analysis