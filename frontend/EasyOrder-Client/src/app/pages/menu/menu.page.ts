import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {MenuSection} from 'src/app/domain/menu-section';
import {MenuService} from '../../services/menu-service/menu.service';
import {noop} from 'rxjs';
import {AppConstants} from '../../domain/app-constants';
import {SessionService} from '../../services/session-service/session.service';
import {StorageService} from '../../services/storage/storage.service';
import {WebSocketService} from '../../services/web-socket-service/web-socket-service.service';
import {environment} from 'src/environments/environment';
import {ToastService} from 'src/app/services/toast-service/toast.service';
import {CheckoutService} from 'src/app/services/checkout-service/checkout.service';
import {ShoppingCartService} from 'src/app/services/shopping-cart-service/shopping-cart.service';
import {SocialSharing} from '@ionic-native/social-sharing/ngx';

@Component({
    selector: 'app-menu',
    templateUrl: 'menu.page.html',
    styleUrls: ['menu.page.scss'],
})
export class MenuPage implements OnInit {

    menuSection: MenuSection[];
    categories: string[] = [];
    currentMenu: MenuSection;
    isPrincipalClient = true;
    linkShare: string = environment.url + '/sharedOrder?sessionId=';

    /***
     * The user has a tableId in the url => principal client else is secondary
     */
    constructor(private route: ActivatedRoute,
                private menuService: MenuService,
                private toastService: ToastService,
                private checkoutService: CheckoutService,
                private shoppingCartService: ShoppingCartService,
                private readonly sessionService: SessionService,
                private readonly storageService: StorageService,
                private socialSharing: SocialSharing,
                private readonly webSocketService: WebSocketService) {
        const tableId = route.snapshot.paramMap.get('tableId');
        if (tableId === AppConstants.SECONDARY_CLIENT) {
            this.isPrincipalClient = false;
        } else {
            MenuService.tableId = +tableId;
        }
    }

    /***
     * Get menu items and subscribe to notifications from other clients
     */
    ngOnInit(): void {
        this.menuService.getMenuItems().subscribe(menuSection => {
            menuSection.length > 0 ? this.currentMenu = menuSection[0] : noop();
            this.menuSection = menuSection;
            this.checkoutService.setMenuList(this.menuSection);
        });
        if (this.isPrincipalClient) {
            this.sessionService.getNewSessionId()
                .subscribe(sessionId => {
                    this.storageService.set(AppConstants.SESSION_ID, sessionId).then();
                    this.webSocketService.open();
                    this.linkShare = this.linkShare + sessionId;
                });
        }

        this.webSocketService.messagesSubject.subscribe(res => {
            const nickname = res["nickname"];
            const menuItemIds = res["menuItemIds"];
            //Customize background-color(TO-DO)
            this.toastService.toast({
                header: nickname + " a facut o schimbare",
                duration: 2000,
                position: 'top',
            });

            if (menuItemIds) {
                this.checkoutService.addNewCustomer(nickname, menuItemIds);
                this.checkoutService.getCustomerList();
            }
            console.log(res);
        });
    }

    shareLink(): void {
        //Replace alert with share-link(TO-DO)
        console.log(this.linkShare);
        this.socialSharing.share("", "", null, this.linkShare)
            .then(res => {
                console.log("a mers")
            }).catch(res => {
            console.log(res);
        })
    }

    setCurrentMenu(menu: MenuSection): void {
        this.currentMenu = menu;
    }
}
