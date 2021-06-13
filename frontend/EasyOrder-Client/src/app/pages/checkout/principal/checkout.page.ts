import {Component, OnInit} from '@angular/core';
import {ShoppingCartService} from 'src/app/services/shopping-cart-service/shopping-cart.service';
import {OrderService} from '../../../services/order-service/order.service';
import {MenuItem, MenuItemWrapper} from '../../../domain/menu-item';
import {MenuItemDescriptionComponent} from '../../menu/components/menu-item-description/menu-item-description.component';
import {ModalController, NavController} from '@ionic/angular';
import {CheckoutService} from 'src/app/services/checkout-service/checkout.service';
import {WebSocketService} from 'src/app/services/web-socket-service/web-socket-service.service';
import {ToastService} from 'src/app/services/toast-service/toast.service';
import {ClientOrder} from 'src/app/domain/client-order';

@Component({
    selector: 'app-checkout',
    templateUrl: './checkout.page.html',
    styleUrls: ['./checkout.page.scss'],
})
export class CheckoutPage implements OnInit {

    menuItems: MenuItemWrapper[] = [];
    clientOrder: ClientOrder[] = [];

    totalPrice = 0;

    constructor(private readonly shoppingCartService: ShoppingCartService,
                private readonly orderService: OrderService,
                private readonly webSocketService: WebSocketService,
                private readonly toastService: ToastService,
                private readonly checkoutService: CheckoutService,
                private readonly modalController: ModalController,
                private readonly navController: NavController) {
    }

    ngOnInit() {
        this.menuItems = this.shoppingCartService.getMenuItems();
        this.checkoutService.clientSubject.subscribe((res: ClientOrder[]) => {
            this.clientOrder = res;
            this.computeTotalPrice();
        })
        this.computeTotalPrice();
    }

    isEmpty(): boolean {
        if (this.menuItems.length > 0)
            return false;
        for (let client of this.clientOrder)
            if (client.orders.length > 0)
                return false;
        return true;
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
        let sum = 0;
        this.menuItems.forEach(item => sum += item.menuItem.price * item.frequency);

        this.clientOrder.forEach(elementClientOrder => {
            elementClientOrder.orders.forEach(item => sum += item.menuItem.price * item.frequency);
        });
        this.totalPrice = sum;
    }

    handlePay() {
        this.clientOrder.forEach(element => {
            this.menuItems.push(...element.orders);
        });

        this.navController.navigateForward('/payment');
    }
}
