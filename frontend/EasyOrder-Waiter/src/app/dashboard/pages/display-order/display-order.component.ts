import {Component, Input, OnInit} from '@angular/core';
import {DisplayOrder} from '../../../domain/display-order';
import {AppConstants} from 'src/app/domain/app-constants';
import {ModalController} from '@ionic/angular';

@Component({
    selector: 'app-display-menu-item',
    templateUrl: './display-order.component.html',
    styleUrls: ['./display-order.component.scss'],
})
export class DisplayOrderComponent implements OnInit {

    readonly abs = Math.abs;
    readonly AppConstants = AppConstants;
    @Input() order: DisplayOrder;

    constructor(private readonly modalController: ModalController) {
    }

    ngOnInit() {
    }

    dismiss(): void {
        this.modalController.dismiss().then();
    }
}
