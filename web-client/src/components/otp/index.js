import React from 'react';
import {BrowserRouter as Router, Route} from 'react-router-dom';
import Navigation from '../Navigation';
import BookingPage from '../Booking';
import SignUpPage from '../SignUp';
import SignInPage from '../SignIn';
import HomePage from '../Home';
import {Form, Button, Row, Col, Container, Image, Modal} from 'react-bootstrap'

import * as ROUTES from '../../constants/routes';
import {withFirebase} from '../Firebase';

import {withAuthentication} from '../Session';


const otp = () => (
    <Container>
    <Modal.Dialog>
    <Modal.Header closeButton>
    <Modal.Title>Enter OTP</Modal.Title>
    </Modal.Header>
    <Modal.Body>
        <Form>
            <Form.Group controlId="formBasicEmail">
                <Form.Control name="source"  type="number" placeholder="Enter OTP" />
            </Form.Group>

        </Form>
    </Modal.Body>
    <Modal.Footer>
    <Button  variant="primary">Confirm</Button>
    </Modal.Footer>
    </Modal.Dialog>

  </Container>
);

export default withAuthentication(otp);
