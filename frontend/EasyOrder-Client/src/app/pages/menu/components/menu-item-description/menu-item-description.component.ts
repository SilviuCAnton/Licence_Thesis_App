import {Component, Input, OnInit} from '@angular/core';
import {ModalController} from '@ionic/angular';
import {MenuItem} from 'src/app/domain/menu-item';
import {ShoppingCartService} from 'src/app/services/shopping-cart-service/shopping-cart.service';
import {environment} from '../../../../../environments/environment';

@Component({
    selector: 'app-menu-item-description',
    templateUrl: './menu-item-description.component.html',
    styleUrls: ['./menu-item-description.component.scss'],
})
export class MenuItemDescriptionComponent implements OnInit {
    @Input() menuItem: MenuItem;
    imageUrl: string = environment.urlApi;
    @Input() showAddInCart = true;
    ordersNumber = 1;

    constructor(private modalCtrl: ModalController,
                private shoppingCartService: ShoppingCartService) {
    }

    ngOnInit() {
    }

    dismiss() {
        this.modalCtrl.dismiss().then();
    }

    decreaseOrderNumber() {
        this.ordersNumber = this.ordersNumber - 1;
    }

    increaseOrderNumber() {
        this.ordersNumber = this.ordersNumber + 1;
    }

    addCart() {
        this.shoppingCartService.addMenuItem(this.menuItem, this.ordersNumber);
        this.dismiss();
    }
}
