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
  }

  handleChange = (event) => {
    const target = event.target;
    const value = target.value;
    let photo = {...this.state.photo};
    photo.date = value;
    this.setState({photo});
  };

  handleSubmit = async (event) => {
    event.preventDefault();
    const {photo} = this.state;
    if (photo.date) {
      const response = await fetch('/photos/' + photo.date);
      const body = await response.json();
      this.setState({photo: body, isLoading: false, notFound: !body.url});
    }
  };

  render() {
    const {photo, isLoading, notFound} = this.state;

    if (isLoading) {
      return <p>Loading...</p>;
    }

    let appIntroContent;
    if(notFound && !photo.error) {
      appIntroContent = "No image found for selected date";
    } else if (!notFound && !photo.error) {
      appIntroContent = "Input a date and hit enter";
    } else if (photo.error) {
      appIntroContent = `Error loading image: ${photo.error}`
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
          {photo.url && (
            <div className="App-intro">
              <img alt="NASA mars rover" src={photo.url}/>
            </div>
          )}
          {appIntroContent && !photo.url && (
            <div className="App-intro">
              {appIntroContent}
            </div>
          )}
        </header>
      </div>
    );
  }
}

export default App;