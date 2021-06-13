import {Component} from '@angular/core';
import {WebSocketService} from '../../../services/web-socket/web-socket.service';
import {DisplayOrder} from '../../../domain/display-order';
import {StorageService} from '../../../services/storage/storage.service';
import {AppConstants} from '../../../domain/app-constants';
import {OrderService} from '../../../services/order/order-service';
import {ModalController, NavController, ViewWillEnter} from '@ionic/angular';
import {DisplayOrderComponent} from '../display-order/display-order.component';
import {noop} from 'rxjs';
import {ToastService} from '../../../services/toast/toast.service';
import {AppPaths} from '../../../domain/app-paths';
import {AuthenticationService} from '../../../services/authentication/authentication.service';

@Component({
    selector: 'app-main',
    templateUrl: './main.component.html',
    styleUrls: ['./main.component.scss'],
})
export class MainComponent implements ViewWillEnter {

    readonly abs = Math.abs;
    readonly AppConstants = AppConstants;
    readonly orders: DisplayOrder[] = [];
    username: string;

    constructor(private readonly webSocketService: WebSocketService,
                private readonly authenticationService: AuthenticationService,
                private readonly storageService: StorageService,
                private readonly orderService: OrderService,
                private readonly modalController: ModalController,
                private readonly toastService: ToastService,
                private readonly navController: NavController) {
    }

    /***
     * Subscribe to web socket notifications each time this page is entered
     * */
    ionViewWillEnter(): void {
        this.getAndWatchOrders();
    }

    private getAndWatchOrders(): void {
        this.storageService.get<string>(AppConstants.USERNAME).then(username => this.username = username);
        this.storageService.get<number>(AppConstants.USER_ID).then(userId => this.getUndoneOrders(userId));
        this.webSocketService.getMessages().subscribe(order => this.orders.push(order));
    }

    private getUndoneOrders(userId: number): void {
        this.orderService.getOrdersForWaiterWithStatus(userId, false)
            .subscribe(orders => this.orders.push(...orders));
    }

    async presentModal(order: DisplayOrder): Promise<void> {
        const modal = await this.modalController.create({
            component: DisplayOrderComponent,
            componentProps: {
                order,
            }
        });
        return await modal.present();
    }

    /***
     * If the order is marked as done successfully remove the corresponding order from the view
     */
    markAsDone(orderId: number): void {
        this.orderService.markOrderAsDone(orderId).subscribe(
            () => {
                const index: number = this.orders.findIndex(it => it.id === orderId);
                index !== -1 ? this.orders.splice(index, 1) : noop();
            },
            err => this.handleError(err)
        );
    }

    logOut(): void {
        this.authenticationService.logout();
        this.navController.navigateRoot(AppPaths.LOGIN).then();
    }

    private handleError(err: any): void {
        let error;
        try {
            error = JSON.parse(err.error).message;
        } catch (e) {
            error = err;
        }
        this.toastService.presentError(error).then();
    }
}
