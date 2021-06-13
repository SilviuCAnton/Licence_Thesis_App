import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {DashboardRoutingModule} from './dashboard-routing.module';
import {MainComponent} from './pages/main/main.component';
import {IonicModule} from '@ionic/angular';
import {DisplayOrderComponent} from './pages/display-order/display-order.component';


@NgModule({
    declarations: [
        MainComponent,
        DisplayOrderComponent
    ],
    imports: [
        CommonModule,
        DashboardRoutingModule,
        IonicModule
    ]
})
export class DashboardModule {
}
