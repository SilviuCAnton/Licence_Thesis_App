import {NgModule} from '@angular/core';
import {PreloadAllModules, RouterModule, Routes} from '@angular/router';

const routes: Routes = [
    {
        path: 'menu',
        loadChildren: () => import('./pages/menu/menu.module').then(m => m.MenuPageModule)
    },
    {
        path: 'checkout',
        loadChildren: () => import('./pages/checkout/checkout.module').then(m => m.CheckoutPageModule)
    },
    {
        path: 'payment',
        loadChildren: () => import('./pages/payment/payment.module').then(m => m.PaymentPageModule)
    },
    {
        path: 'sharedOrder',
        loadChildren: () => import('./pages/shared-order/shared-order.module').then(m => m.SharedOrderPageModule)
    },
    {
        path: '',
        redirectTo: 'menu/-1',
        pathMatch: 'full'
    },

];

@NgModule({
    imports: [
        RouterModule.forRoot(routes, {preloadingStrategy: PreloadAllModules})
    ],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
