import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {HttpUtils} from '../../domain/http-utils';
import {MenuSection} from '../../domain/menu-section';

@Injectable({
    providedIn: 'root'
})
export class MenuService {

    constructor(private readonly httpClient: HttpClient) {
    }
    static tableId: number;
    private readonly baseUrl = `${environment.urlApi}/menu`;

    getMenuItems(): Observable<MenuSection[]> {
        return this.httpClient.get<MenuSection[]>(this.baseUrl, HttpUtils.getDefaultHeaders());
    }
}
