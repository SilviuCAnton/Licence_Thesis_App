import {Component, OnInit} from '@angular/core';
import {MenuItem, MenuItemWrapper} from '../../../domain/menu-item';
import {ShoppingCartService} from '../../../services/shopping-cart-service/shopping-cart.service';
import {OrderService} from '../../../services/order-service/order.service';
import {ModalController} from '@ionic/angular';
import {MenuItemDescriptionComponent} from '../../menu/components/menu-item-description/menu-item-description.component';

@Component({
    selector: 'app-secondary',
    templateUrl: './secondary-checkout.page.html',
    styleUrls: ['./secondary-checkout.page.scss'],
})
export class SecondaryCheckoutPage implements OnInit {

    menuItems: MenuItemWrapper[] = [];
    totalPrice = 0;
    isDone = false;

    constructor(private readonly shoppingCartService: ShoppingCartService,
                private readonly orderService: OrderService,
                private readonly modalController: ModalController) {
    }

    ngOnInit() {
        this.menuItems = this.shoppingCartService.getMenuItems();
        this.computeTotalPrice();
    }

    showItem(menuItem: MenuItem): void {
        this.presentModal(menuItem).then();
    }

    async presentModal(menuItem: MenuItem) {
        const modal = await this.modalController.create({
            component: MenuItemDescriptionComponent,
            componentProps: {
                menuItem,
                showAddInCart: false
            }
        });
        return await modal.present();
    }

    decreaseOrderNumber(item: MenuItemWrapper): void {
        this.shoppingCartService.removeMenuItem(item.menuItem.id, 1);
        this.totalPrice -= item.menuItem.price;
    }

    increaseOrderNumber(item: MenuItemWrapper): void {
        this.shoppingCartService.addMenuItem(item.menuItem, 1);
        this.totalPrice += item.menuItem.price;
    }

    computeTotalPrice(): void {
        this.menuItems.forEach(item => this.totalPrice += item.menuItem.price * item.frequency);
    }

    /***
     * The order is done
     */
    handleDone(): void {
        this.isDone = true;
    }
}
