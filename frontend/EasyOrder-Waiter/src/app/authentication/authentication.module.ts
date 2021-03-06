import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {AuthenticationRoutingModule} from './authentication-routing.module';
import {LoginComponent} from './pages/login/login.component';
import {IonicModule} from '@ionic/angular';
import {FormsModule} from '@angular/forms';
import {RegisterComponent} from './pages/register/register.component';


@NgModule({
    declarations: [
        LoginComponent,
        RegisterComponent
    ],
    imports: [
        CommonModule,
        AuthenticationRoutingModule,
        IonicModule,
        FormsModule
    ]
})
export class AuthenticationModule {
}
