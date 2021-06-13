import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {CheckoutPage} from './principal/checkout.page';
import {SecondaryCheckoutPage} from './secondary/secondary-checkout.page';

const routes: Routes = [
    {
        path: '',
        component: CheckoutPage
    },
    {
        path: 'secondary',
        component: SecondaryCheckoutPage
    }

];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class CheckoutPageRoutingModule {
}
