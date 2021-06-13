import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {NavController} from '@ionic/angular';
import {TempOrder} from 'src/app/domain/temp-order';
import {TempOrderService} from 'src/app/services/temp-order-service/temp-order-service.service';
import {ToastService} from 'src/app/services/toast-service/toast.service';

@Component({
    selector: 'app-shared-order',
    templateUrl: './shared-order.page.html',
    styleUrls: ['./shared-order.page.scss'],
})
export class SharedOrderPage implements OnInit {

    nickname: string;
    sessionId: string;

    constructor(
        private readonly tempOrderService: TempOrderService,
        private readonly navController: NavController,
        private readonly route: ActivatedRoute,
        private readonly toastService: ToastService) {
    }

    ngOnInit(): void {
        this.sessionId = this.route.snapshot.queryParamMap.get('sessionId');
    }

    save(): void {
        const tempOrder = new TempOrder();
        tempOrder.nickname = this.nickname;
        tempOrder.sessionId = this.sessionId;
        tempOrder.menuItemIds = [];
        this.tempOrderService.save(tempOrder).subscribe(
            () => {
                this.navController.navigateForward('/menu/secondary').then();
            },
            () => this.toastService.presentError('This nickname is already in use! Please try another one!')
                .then()
        );
    }

}
