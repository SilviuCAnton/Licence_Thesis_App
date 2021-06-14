import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {HttpUtils} from '../../domain/http-utils';
import {Observable} from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class SessionService {

    constructor(private readonly httpClient: HttpClient) {
    }

    getNewSessionId(): Observable<string> {
        const url = `${environment.urlApi}/order-service/session/newSessionId`;
        return this.httpClient.get<string>(url, HttpUtils.getSimpleResponseHeaders());
    }
}
