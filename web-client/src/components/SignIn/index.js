import React, {Component} from 'react';
import {Link, withRouter} from 'react-router-dom';
import {Form, Button, Row, Col, Container, Image, Modal} from 'react-bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css';
import {compose} from 'recompose'
import firebase from 'firebase'
import passwordHash from 'password-hash'

import { withFirebase} from '../Firebase';
import * as ROUTES from '../../constants/routes';
import { SignUpLink} from '../SignUp'
import UserProfile from '../UserProfile';

const SignInPage = () => (
  <Container>
  <center>
  <h1 >SIGN IN</h1>
  </center>
    <Row>
    <Col>
      <Image src="https://cdn.clipart.email/67eb7275954071dc550f09f7a1644779_tour-travel-png-vectors-tours-and-travels-png-free-transparent-_900-834.png" height='300' width='400' />
    </Col>
    <Col>
      <SignInForm />
      <SignUpLink />
    </Col>
    </Row>
  </Container>
);

const INITIAL_STATE = {
  email: '',
  password: '',
  error: null,
};


class SignInFormBase extends Component {
  constructor(props) {
    super(props);
    this.state = { ...INITIAL_STATE};
    this.state = {
        signin:true,
        otp: false
    }
  }

  onSubmit = event => {
    const { email, password} = this.state;

        let pass = String(passwordHash.generate(password))
          const response =
          fetch('http://52.70.2.41:5002/login' , {
              method: 'POST',
              headers: { 'Content-Type': 'application/json'},
              body: JSON.stringify({
                  email: email,
                  password: password
              }),
          }).then(response => response.json())
          .then((resp) =>  {
              console.log(resp)
              this.setState({
                  signin:false,
                  email: resp.Email,
                  name: resp.Name,
                  password: password,
                  otp:true,
                  otpToConfirm: resp.otp
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

    otpConfirm = event => {
        var str1 = this.state.otpToConfirm.toString();
        var str2 = this.state.otpEntered.toString();
        if (str1 == str2){
           //UserProfile.getDetails(resp.Name, resp.Email)
           let obj = {name: this.state.name, email: this.state.email}
           sessionStorage.setItem("User", JSON.stringify(obj));
           this.props.firebase
          .signIn(this.state.email, this.state.password)
          .then(authUser => {
          this.setState({...INITIAL_STATE});
          this.props.history.push(ROUTES.HOME);
          })
        }
        else {
            this.setState({...INITIAL_STATE});
        }
       event.preventDefault();
    }

  onChange = event => {

    this.setState({ [event.target.name]: event.target.value});
  };
  handleOtpChange = (event) => {
      this.setState({
          otpEntered: event.target.value
      })
  }

  render() {
    const {
      email,
      password,
      error
    } = this.state;

    const isInvalid =
         password === '' ||
         email === ''

     var signin = {
          display:this.state.signin ?'block':'none'
        };
        var otp = {
             display:this.state.otp ?'block':'none'
           };
    return (
        <div>
        <div style={signin}>
        <Form onSubmit={this.onSubmit}>
        <Form.Label>Email</Form.Label>
        <Form.Control
          name="email"
          value={email}
          onChange={this.onChange}
          type="text"
          placeholder="Email Address"
        />
        <Form.Label className="mx-auto my-2">Password</Form.Label>
        <Form.Control
          name="password"
          value={password}
          onChange={this.onChange}
          type="password"
          placeholder="Password"
        />
        <Button className="mx-auto my-3" disabled={isInvalid} type="submit">Sign In</Button>
        {error && <p>{error.message}</p>}
        </Form>
        </div>
        <div style={otp}>
            <Container>
            <Modal.Dialog>
            <Modal.Header closeButton>
            <Modal.Title>Enter OTP</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group controlId="formBasicEmail">
                        <Form.Control name="source" onChange={this.handleOtpChange} type="number" placeholder="Enter OTP" />
                    </Form.Group>

                </Form>
            </Modal.Body>
            <Modal.Footer>
            <Button onClick={this.otpConfirm} variant="primary">Confirm</Button>
            </Modal.Footer>
            </Modal.Dialog>

        </Container>
        </div>
        </div>
    )
  }
}
const SignInForm = compose (
  withRouter,
  withFirebase,
)(SignInFormBase);


export default SignInPage;
export { SignInForm};
