import {Component, Input, OnInit} from '@angular/core';
import {NavController} from '@ionic/angular';
import {ShoppingCartService} from 'src/app/services/shopping-cart-service/shopping-cart.service';
import {AppPaths} from '../../../../domain/app-paths';

@Component({
    selector: 'app-footer',
    templateUrl: './footer.component.html',
    styleUrls: ['./footer.component.scss'],
})
export class FooterComponent implements OnInit {

    @Input() isPrincipalClient: boolean;

    constructor(readonly shoppingCartService: ShoppingCartService,
                private readonly navController: NavController) {
    }

    ngOnInit() {
    }

    goToCheckout(): void {
        this.isPrincipalClient
            ? this.navController.navigateForward(AppPaths.CHECKOUT_PAGE).then()
            : this.navController.navigateForward(AppPaths.CHECKOUT_PAGE_SECONDARY_CLIENT).then();
    }
}
