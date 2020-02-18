import React, {Component} from 'react';
import { Form, FormGroup, Input } from 'reactstrap';

import './App.css';

class App extends Component {
  emptyPhoto = {};

  constructor(props) {
    super(props);
    this.state = {
      photo: this.emptyPhoto,
    };
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleChange(event) {
    const target = event.target;
    const value = target.value;
    let photo = {...this.state.photo};
    photo.date = value;
    this.setState({photo});
  }

  async handleSubmit(event) {
    event.preventDefault();
    const {photo} = this.state;
    if (photo.date) {
      const response = await fetch('/photos/' + photo.date);
      const body = await response.json();
      this.setState({photo: body, isLoading: false, notFound: !body.url});
    }
  }

  render() {
    const {photo, isLoading, notFound} = this.state;

    if (isLoading) {
      return <p>Loading...</p>;
    }

    return (
      <div className="App">
        <header className="App-header">
          <Form onSubmit={this.handleSubmit}>
            <FormGroup>
              <Input
                type="text"
                name="date"
                id="date"
                value={photo.date || ''}
                onChange={this.handleChange}
                placeholder="Date"/>
            </FormGroup>
          </Form>
          {photo.url ?
            (
              <div className="App-intro">
                <img alt="NASA mars rover" src={photo.url}/>
              </div>
            ) : null
          }
          {notFound && !photo.error ?
            (
              <div className="App-intro">
                No image found for selected date
              </div>
            ) : null
          }
          {!notFound && !photo.error ?
            (
              <div className="App-intro">
                Input a date and hit enter
              </div>
            ) : null
          }
          {photo.error ?
            (
              <div className="App-intro">
                Error loading image: {photo.error}
              </div>
            ) : null
          }
        </header>
      </div>
    );
  }
}

export default App;