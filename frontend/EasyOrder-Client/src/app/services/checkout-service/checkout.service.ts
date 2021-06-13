import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {ClientMenu} from 'src/app/domain/client-menu';
import {ClientOrder} from 'src/app/domain/client-order';
import {MenuItem, MenuItemWrapper} from 'src/app/domain/menu-item';
import {MenuSection} from 'src/app/domain/menu-section';

@Injectable({
    providedIn: 'root'
})
export class CheckoutService {

    private clientMenu: ClientMenu[] = [];
    private menuItems: MenuItem[] = [];
    public clientSubject = new BehaviorSubject<ClientOrder[]>([]);

    constructor() {
    }
    addNewCustomer(nickname: string, menuItemIds: number[]): void {
        let ok = 0;
        this.clientMenu.forEach(element => {
            if (element.nickname === nickname) {
                element.menuItemIds = menuItemIds;
                ok = 1;
            }
        });

        if (ok === 0) {
            this.clientMenu.push({nickname, menuItemIds});
        }
    }

    setMenuList(menuSection: MenuSection[]) {
        // tslint:disable-next-line:prefer-for-of
        for (let i = 0; i < menuSection.length; i++) {
            menuSection[i].menuItems.forEach((element: any) => {
                element.category = menuSection[i].categoryName;
                this.menuItems.push(element);
            });
        }
    }

    removeAllItems(): void {
        this.clientMenu = [];
    }

    getOrderedItemsCount(): number {
        let count = 0;
        this.clientMenu.forEach(clientInfo => {
            count += clientInfo.menuItemIds.length;
        });
        return count;
    }

    /***
     * Group menu items by client nickname
     */
    getCustomerList(): ClientOrder[] {
        const clientOrder: ClientOrder[] = [];
        let menuItemsWrapper: MenuItemWrapper[] = [];
        this.clientMenu.forEach(clientInfo => {
            const menuItemIds = clientInfo.menuItemIds;
            const nickname = clientInfo.nickname;
            let frequency = 1;
            menuItemIds.sort();

            for (let i = 0; i < menuItemIds.length; i++) {
                if (menuItemIds[i] !== menuItemIds[i + 1]) {
                    let menuItem: MenuItem;

                    // tslint:disable-next-line:prefer-for-of
                    for (let j = 0; j < this.menuItems.length; j++) {
                        if (this.menuItems[j].id === menuItemIds[i]) {
                            menuItem = this.menuItems[j];
                        }
                    }

                    menuItemsWrapper.push(
                        {
                            frequency,
                            menuItem,
                        }
                    );
                    frequency = 1;
                } else {
                    frequency++;
                }
            }

            clientOrder.push({
                nickname,
                orders: menuItemsWrapper
            });
            menuItemsWrapper = [];
        });

        this.clientSubject.next(clientOrder);
        return clientOrder;
    }
}
