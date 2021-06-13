import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';

import {IonicModule} from '@ionic/angular';

import {CheckoutPageRoutingModule} from './checkout-routing.module';

import {CheckoutPage} from './principal/checkout.page';
import {SecondaryCheckoutPage} from './secondary/secondary-checkout.page';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        IonicModule,
        CheckoutPageRoutingModule
    ],
    declarations: [CheckoutPage, SecondaryCheckoutPage]
})
export class CheckoutPageModule {
}
