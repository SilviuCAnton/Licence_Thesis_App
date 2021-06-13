import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import {HttpUtils} from '../../domain/http-utils';
import {DisplayOrder} from '../../domain/display-order';
import {Injectable} from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class OrderService {

    constructor(private readonly httpClient: HttpClient) {
    }

    getOrdersForWaiterWithStatus(userId: number, isDone: boolean): Observable<DisplayOrder[]> {
        const url = `${environment.urlApi}/orders?id=${userId}&isDone=${isDone}`;
        return this.httpClient.get<DisplayOrder[]>(url, HttpUtils.getDefaultHeaders());
    }

    markOrderAsDone(orderId: number): Observable<void> {
        const url = `${environment.urlApi}/orders/${orderId}/markAsDone`;
        return this.httpClient.put<void>(url, {}, HttpUtils.getDefaultHeaders());
    }
}
