import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {IonicModule} from '@ionic/angular';
import {FormsModule} from '@angular/forms';
import {MenuPage} from './menu.page';

import {MenuPageRoutingModule} from './menu-routing.module';
import {HeaderComponent} from './components/header/header.component';
import {MenuItemComponent} from './components/menu-item/menu-item.component';
import {MenuItemDescriptionComponent} from './components/menu-item-description/menu-item-description.component';
import {FooterComponent} from './components/footer/footer.component';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        IonicModule,
        MenuPageRoutingModule,
    ],
    declarations: [MenuPage, HeaderComponent, MenuItemComponent, MenuItemDescriptionComponent, FooterComponent],
})
export class MenuPageModule {
}
