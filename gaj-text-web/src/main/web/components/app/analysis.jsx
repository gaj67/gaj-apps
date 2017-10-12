import React from 'react'
import axios from 'axios'

const getOutputType = function(contentType) {
    if (contentType.startsWith('application/json')) return 'json';
    if (contentType.startsWith('text/html')) return 'html';
    return 'string';
}

class Analysis extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            input: '',
            output: null,
            outputType: null
        };
        
        this.getOutputAsString = this.getOutputAsString.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.tokeniseText = this.remoteCall.bind(this, 'api/text/tokenise/');
        this.gatherWords = this.remoteCall.bind(this, 'api/fd/gather/');
    }
    
    getOutputAsString() {
        if (this.state.output && this.state.outputType && this.state.outputType != 'html') {
            return (typeof this.state.output === 'string') ? this.state.output : JSON.stringify(this.state.output, undefined, 2);
        }
        return '';        
    }
    
    handleChange(event) {
        this.setState({input: event.target.value});
    }
    
    remoteCall(uri, event) {
        //event.preventDefault();
        this.setState({output: 'Loading...', outputType: 'string'});
        axios.get(
            uri + encodeURIComponent(this.state.input)
        ).then(response => {
            this.setState({output: response.data, outputType: getOutputType(response.headers['content-type'])});
        }).catch(error => {
            this.setState({output: 'Error loading!', outputType: 'string'});
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
                    <div className={(this.state.outputType && this.state.outputType != 'html') ? 'show-block' : 'show-none'}>
                        <textarea cols={100} rows={20} value={this.getOutputAsString()} readOnly />
                    </div>
                    <div className={(this.state.outputType && this.state.outputType == 'html') ? 'show-block' : 'show-none'}>
                        To be implemented!
                    </div>
                </div>
                <br/>
                <input type="submit" value="Tokenise" onClick={this.tokeniseText} />
                <input type="submit" value="Gather" onClick={this.gatherWords} />
            </form>
        );
    }
}

export default Analysis