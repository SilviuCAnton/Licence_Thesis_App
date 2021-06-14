import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {HttpUtils} from '../../domain/http-utils';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import {CreditCard} from '../../domain/credit-card';

@Injectable({
    providedIn: 'root'
})
export class PaymentService {

    private readonly baseUrl = `${environment.urlApi}/payment-service/pay`;

    constructor(private readonly httpClient: HttpClient) {
    }

  pay(cardDetails: CreditCard): Observable<string> {
    console.log('pay method');
    return this.httpClient.post<string>(this.baseUrl, cardDetails, HttpUtils.getDefaultHeaders());
  }
}
