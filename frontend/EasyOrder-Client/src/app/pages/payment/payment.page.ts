import { Component, OnInit } from '@angular/core';
import {PaymentService} from '../../services/payment-service/payment-service.service';
import {CreditCard} from '../../domain/credit-card';
import {OrderService} from '../../services/order-service/order.service';
import {ShoppingCartService} from '../../services/shopping-cart-service/shopping-cart.service';
import {NavController} from '@ionic/angular';
import {ToastService} from "../../services/toast-service/toast.service";

@Component({
  selector: 'app-payment',
  templateUrl: './payment.page.html',
  styleUrls: ['./payment.page.scss'],
})
export class PaymentPage implements OnInit {
  owner: string;
  cardNumber: string;
  cvv: number;
  expireDate: string;

  constructor(
      private readonly paymentService: PaymentService,
      private readonly orderService: OrderService,
      private readonly shoppingCartService: ShoppingCartService,
      private readonly navController: NavController,
      private readonly toastService: ToastService
  ) { }

  ngOnInit(): void {
  }

  handlePay(): void {
    console.log('Method handlePay');
    const creditCard = new CreditCard();
    creditCard.owner = this.owner;
    creditCard.cardNumber = this.cardNumber;
    creditCard.expirationDate = this.expireDate;
    creditCard.cvv = this.cvv;
    this.paymentService.pay(creditCard)
        .subscribe(
            success => {
              console.log('POST call in success', success);
              this.orderService.saveOrderSpread('', this.shoppingCartService.getMenuItems(), this.toastService.sessionId)
                  .subscribe(
                      () => {
                          this.navController.navigateBack('').then();
                          this.shoppingCartService.removeAllItems();
                      },
                      response => {
                          this.toastService.presentError(response.error.message).then();
                          console.log('POST call in error', response);
                      }
                  );
            },
            response => {
                this.toastService.presentError(response.error.message).then();
              console.log('POST call in error', response);
            },
            () => {
              console.log('The POST observable is now completed.');
            });
  }
}
