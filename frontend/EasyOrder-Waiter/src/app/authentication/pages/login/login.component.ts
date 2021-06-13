import {Component, OnInit} from '@angular/core';
import {NavController} from '@ionic/angular';
import {AuthenticationService} from '../../../services/authentication/authentication.service';
import {User} from '../../../domain/user';
import {AppPaths} from '../../../domain/app-paths';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {

    username: string;
    password: string;
    usernameErrors = [];
    passwordErrors = [];

    constructor(private readonly authenticationService: AuthenticationService,
                private readonly navController: NavController) {
    }

    ngOnInit(): void {
    }

    /***
     * Validates live the username inputs
     */
    validateUsername() {
        this.usernameErrors = [];
        let invalid = false;
        if (this.username == null) {
            this.usernameErrors.push('Username cannot be empty');
            invalid = true;
        } else {
            if (this.username.length > 128) {
                this.usernameErrors.push('Username cannot be over 128 characters');
                invalid = true;
            }
            if (this.username.length < 3) {
                this.usernameErrors.push('Username cannot be less than 3 characters');
                invalid = true;
            }
        }
        return invalid;
    }

    /***
     * Validates live the password inputs
     */
    validatePassword() {
        this.passwordErrors = [];
        let invalid = false;
        if (this.password == null) {
            this.passwordErrors.push('Password cannot be empty');
            invalid = true;
        } else {
            if (this.password.length > 128) {
                this.passwordErrors.push('Password cannot be over 128 characters');
                invalid = true;
            }
            if (this.password.length < 3) {
                this.passwordErrors.push('Password cannot be less than 3 characters');
                invalid = true;
            }
        }

        return invalid;
    }

    logIn(): void {
        const user = new User();
        user.username = this.username;
        user.password = this.password;
        this.authenticationService.login(user).subscribe(
            () => this.navController.navigateForward(AppPaths.DASHBOARD).then(),
            err => console.log(err)
        );
    }
}
