import React, {Component} from 'react'
import app from 'firebase/app';
import * as FirebaseUI from 'firebaseui';
import Firebase from '../Firebase';
import firebase from 'firebase'
import {Card, Container} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import { withAuthorization} from '../Session';
import {AuthUserContext} from '../Session';


class UserProfile extends Component {
    setData(name, email){
        let obj = {name: name, email: email}
        sessionStorage.setItem("User", JSON.stringify(obj));
    }
    getData(){
        let data = sessionStorage.getItem('User');
        data = JSON.parse(data);
        console.log(data.name);
        return data
    }
    check(){
        if (localStorage.getItem("User") === null)
            return false;
        else {
            return true;
        }
    }
}
export default UserProfile;
