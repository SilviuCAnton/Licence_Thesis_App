import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpUtils} from 'src/app/domain/http-utils';
import {Order} from 'src/app/domain/order';
import {environment} from 'src/environments/environment';
import {MenuItemWrapper} from '../../domain/menu-item';
import {MenuService} from '../menu-service/menu.service';

@Injectable({
    providedIn: 'root',
})
export class OrderService {

    private readonly baseUrl = `${environment.urlApi}/orders`;

    constructor(private readonly httpClient: HttpClient) {
    }

    saveOrder(order: Order): Observable<Order> {
        return this.httpClient.post<Order>(
            this.baseUrl,
            order,
            HttpUtils.getDefaultHeaders()
        );
    }

    /***
     * Saves an order to the corresponding backend DTO
     */
    saveOrderSpread(
        comments: string,
        menuItems: MenuItemWrapper[],
    ): Observable<Order> {
        const menuItemIds = [];
        menuItems.forEach((item) => {
            for (let i = 0; i < item.frequency; i++) {
                menuItemIds.push(item.menuItem.id);
            }
        });
        const tableId = MenuService.tableId;
        console.log(tableId);
        return this.saveOrder({comments, menuItemIds, tableId});
    }

    setOrderStatus(orderId: number, isDone: boolean = true) {
        let suffix = null;
        if (isDone) {
            suffix = 'markAsDone';
        } else {
            suffix = 'markAsNotDone';
        }

        return this.httpClient.put(
            `${this.baseUrl}/${orderId}/${suffix}`,
            null,
            HttpUtils.getDefaultHeaders()
        );
    }
}
