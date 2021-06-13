import {Injectable} from '@angular/core';
import {MenuItem, MenuItemWrapper} from 'src/app/domain/menu-item';
import {AppConstants} from '../../domain/app-constants';
import {StorageService} from '../storage/storage.service';
import {TempOrder} from '../../domain/temp-order';
import {TempOrderService} from '../temp-order-service/temp-order-service.service';
import {CheckoutService} from "../checkout-service/checkout.service";
import {Router} from "@angular/router";

@Injectable({
    providedIn: 'root',
})
export class ShoppingCartService {

    private menuItems: MenuItemWrapper[];
    private tempOrder: TempOrder;

    constructor(private readonly route: Router,
                private readonly storageService: StorageService,
                private readonly tempOrderService: TempOrderService,
                private readonly checkoutService: CheckoutService) {
        this.menuItems = [];
        this.storageService.get<string>(AppConstants.NICKNAME).then(nickname => {
            if (!nickname) {
                return;
            }
            this.tempOrder = new TempOrder();
            this.tempOrder.nickname = nickname;
            this.tempOrder.menuItemIds = [];
            this.storageService.get<string>(AppConstants.SESSION_ID)
                .then(sessionId => this.tempOrder.sessionId = sessionId);
        });
    }

    getAllOrderedItemsCount(): number {
        return this.getSize() + this.checkoutService.getOrderedItemsCount();
    }

    getSize(): number {
        let size = 0;
        this.menuItems.forEach((menuItem) => {
            size += menuItem.frequency;
        });

        return size;
    }

    getFrequency(menuItem: MenuItem): number {
        return this.menuItems.find((x) => x.menuItem.id === menuItem.id).frequency;
    }

    computeTotalPrice(): number {
        let sum = 0;
        this.menuItems.forEach(item => sum += item.menuItem.price * item.frequency);

        this.checkoutService.getCustomerList().forEach(elementClientOrder => {
            elementClientOrder.orders.forEach(item => sum += item.menuItem.price * item.frequency);
        });
        return sum;
    }

    getMenuItems(): MenuItemWrapper[] {
        return this.menuItems;
    }

    public MenuItem(menuItem: MenuItem, frequency?: number): MenuItemWrapper {
        if (frequency === undefined) {
            frequency = 1;
        }

        let result = this.menuItems.find((x) => x.menuItem.id === menuItem.id);
        if (result === undefined) {
            result = {menuItem, frequency};
            this.menuItems.push(result);
        } else {
            result.frequency += frequency;
        }

        return result;
    }

    /***
     * Adds an item to the cart and sends update to the corresponding tempOrder if the client is secondary
     */
    public addMenuItem(menuItem: MenuItem, frequency?: number): MenuItemWrapper {
        if (frequency === undefined) {
            frequency = 1;
        }

        let result = this.menuItems.find((x) => x.menuItem.id === menuItem.id);
        if (result === undefined) {
            result = {menuItem, frequency};
            this.menuItems.push(result);
        } else {
            result.frequency += frequency;
        }
        this.sendUpdateTempOrder();
        return result;
    }

    /***
     * Adds more items to the cart and notifies the backend
     */
    public addMenuItemRange(menuItems: MenuItem[]): MenuItemWrapper[] {
        const result = [];
        menuItems.forEach((menuItem) => {
            result.push(this.addMenuItem(menuItem));
        });
        this.sendUpdateTempOrder();
        return result;
    }

    /***
     * Removes an item and notifies the backend
     */
    public removeMenuItem(
        menuItemId: number,
        frequency?: number
    ): MenuItemWrapper {
        if (frequency === undefined) {
            frequency = 1;
        }

        const result = this.menuItems.find((x) => x.menuItem.id === menuItemId);
        if (result === undefined) {
            throw new Error(`MenuItem with id ${menuItemId} doesn't exist!`);
        } else {
            result.frequency -= frequency;
            if (result.frequency <= 0) {
                this.menuItems.splice(this.menuItems.indexOf(result), 1);
            }
        }
        this.sendUpdateTempOrder();
        return result;
    }

    /***
     * Removes more than 1 item and notifies the backend
     */
    public removeMenuItemRange(menuItemIds: number[]): MenuItemWrapper[] {
        const result = [];
        menuItemIds.forEach((menuItemId) => {
            result.push(this.removeMenuItem(menuItemId));
        });
        this.sendUpdateTempOrder();
        return result;
    }

    /***
     * Removes all items and notifies the backend
     */
    removeAllItems(): void {
        this.menuItems = [];
        this.checkoutService.removeAllItems();
        this.sendUpdateTempOrder();
    }

    /***
     * Send update only if the client is secondary (accessed the link via the shared one from the principal client)
     */
    sendUpdateTempOrder(): void {
        if (!this.route.url.endsWith(AppConstants.SECONDARY_CLIENT)) {
            return;
        }
        if (!this.tempOrder) {
            return;
        }
        const ids = [];
        this.menuItems.forEach(menuItem => {
            for (let i = 0; i < menuItem.frequency; i++) {
                ids.push(menuItem.menuItem.id);
            }
        });
        this.tempOrder.menuItemIds = ids;
        this.tempOrderService.update(this.tempOrder).subscribe();
    }
}
