import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { SharedOrderPageRoutingModule } from './shared-order-routing.module';

import { SharedOrderPage } from './shared-order.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    SharedOrderPageRoutingModule
  ],
  declarations: [SharedOrderPage]
})
export class SharedOrderPageModule {}
