import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {HttpUtils} from '../../domain/http-utils';
import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {environment} from '../../../environments/environment';
import {TempOrder} from 'src/app/domain/temp-order';
import {StorageService} from '../storage/storage.service';
import {AppConstants} from 'src/app/domain/app-constants';

@Injectable({
    providedIn: 'root'
})
export class TempOrderService {

    private readonly baseUrl = `${environment.urlApi}/order-service/tempOrders`;

    constructor(private readonly httpClient: HttpClient,
                private readonly storageService: StorageService) {
    }

    save(tempOrder: TempOrder): Observable<TempOrder> {
        return this.httpClient.post<TempOrder>(
            this.baseUrl,
            tempOrder,
            HttpUtils.getDefaultHeaders()
        )
            .pipe(
                tap(() => {
                    this.storageService.set(AppConstants.NICKNAME, tempOrder.nickname).then();
                    this.storageService.set(AppConstants.SESSION_ID, tempOrder.sessionId).then();
                }),
            );
    }

    update(tempOrder: TempOrder): Observable<TempOrder> {
        return this.httpClient.put<TempOrder>(this.baseUrl, tempOrder, HttpUtils.getDefaultHeaders());
    }
}
