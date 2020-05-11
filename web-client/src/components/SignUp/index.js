import React, {Component} from 'react';
import {Link, withRouter} from 'react-router-dom';
import {Form, Button, Col, Row, Container, InputGroup, FormControl, Image} from 'react-bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css';
import {compose} from 'recompose'
import IntlTelInput from 'react-bootstrap-intl-tel-input'

import { withFirebase} from '../Firebase';
import * as ROUTES from '../../constants/routes';
import passwordHash from 'password-hash';

const SignUpPage = () => (
<Container>
    <h1>Sign Up</h1>
    <Row>
        <Col>
            <Image src="https://cdn.clipart.email/67eb7275954071dc550f09f7a1644779_tour-travel-png-vectors-tours-and-travels-png-free-transparent-_900-834.png" height='300' width='400' />
        </Col>
        <Col>
            <SignUpForm />
        </Col>
    </Row>
</Container>
);

const INITIAL_STATE = {
  displayName: '',
  email: '',
  passwordOne: '',
  passwordTwo: '',
  error: null,
};

class SignUpFormBase extends Component {
  constructor(props) {
    super(props);
    this.state = { ...INITIAL_STATE};
  }

  onSubmit = event => {
    const {displayName, email, passwordOne} = this.state;

    let pass = passwordHash.generate(passwordOne)
      const response =
      fetch('http://52.70.2.41:5002/signup', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json'},
          body: JSON.stringify({
              email: email,
              name: displayName,
              password: passwordOne
          }),
      }).then(() =>  {
          this.props.firebase
            .createUser(email, passwordOne, displayName)
            .then(authUser => {
              this.setState({...INITIAL_STATE});
              this.props.history.push(ROUTES.HOME);
            })
            .catch(error => {
              this.setState({error});
          });
        })
        .catch(error => {
          this.setState({error});
      });


      event.preventDefault();
  };

  onChange = event => {
    this.setState({ [event.target.name]: event.target.value});
  };

  render() {
    const {
      displayName,
      email,
      passwordOne,
      passwordTwo,
      error
    } = this.state;

    const isInvalid =
         passwordOne !== passwordTwo ||
         passwordOne === '' ||
         email === '' ||
         displayName === '';

    return (
      <Form onSubmit={this.onSubmit}>
        <Form.Group>
          <Form.Label>Full Name</Form.Label>
          <Form.Control
            name="displayName"
            value={displayName}
            onChange={this.onChange}
            type="text"
            placeholder="Full Name"
          />
          <Form.Label className="mx-auto my-2">Email</Form.Label>
          <Form.Control
            name="email"
            value={email}
            onChange={this.onChange}
            type="text"
            placeholder="Email Address"
          />
          <Form.Label className="mx-auto my-2">Password</Form.Label>
          <Form.Control
            name="passwordOne"
            value={passwordOne}
            onChange={this.onChange}
            type="password"
            placeholder="Password"
          />
          <Form.Label className="mx-auto my-2">Confirm Password</Form.Label>
          <Form.Control
            name="passwordTwo"
            value={passwordTwo}
            onChange={this.onChange}
            type="password"
            placeholder="Confirm Password"
          />
          <Button className="mx-auto my-3" disabled={isInvalid} type="submit">Sign Up</Button>
          {error && <p>{error.message}</p>}

          </Form.Group>
      </Form>
    )
  }
}
const SignUpForm = compose (
  withRouter,
  withFirebase,
)(SignUpFormBase);

const SignUpLink = () => (
  <p>
  Don't have an account? <Link to={ROUTES.SIGN_UP}>Sign Up</Link>
  </p>
);
export default SignUpPage;
export { SignUpForm, SignUpLink};
