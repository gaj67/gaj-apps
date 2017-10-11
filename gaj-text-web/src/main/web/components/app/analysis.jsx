import React from 'react'
import axios from 'axios'

const showAsString = function(value) {
    if (value) {
        return (typeof value === 'string') ? value : JSON.stringify(value, undefined, 2);
    }
    return '';
};

class Analysis extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            input: '',
            output: ''
        };
        
        this.handleChange = this.handleChange.bind(this);
        this.tokeniseText = this.remoteCall.bind(this, 'api/text/tokenise/');
        this.gatherWords = this.remoteCall.bind(this, 'api/fd/gather/');
    }
    
    handleChange(event) {
        this.setState({input: event.target.value});
    }
    
    remoteCall(uri, event) {
        event.preventDefault();
        axios.get(
            uri + encodeURIComponent(this.state.input)
        ).then(response => {
            this.setState({output: response.data});
        }).catch(error => {
            console.log(error);
        }); 
    }
    
    render() {
        return (
            <form onSubmit={e => e.preventDefault()}>
                <div>
                    <label>Text input:</label>
                    <br/>
                    <textarea cols={100} rows={5} value={this.state.input} onChange={this.handleChange} />
                </div>
                <div className={this.state.output ? 'show-block' : 'show-none'}>
                    <br/>
                    <label>Analysed output:</label>
                    <br/>
                    <textarea cols={100} rows={20} value={showAsString(this.state.output)} readOnly />
                </div>
                <br/>
                <input type="submit" value="Tokenise" onClick={this.tokeniseText} />
                <input type="submit" value="Gather" onClick={this.gatherWords} />
            </form>
        );
    }
}

export default Analysis