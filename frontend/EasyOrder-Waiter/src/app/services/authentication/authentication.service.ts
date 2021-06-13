import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {User} from '../../domain/user';
import {from, Observable} from 'rxjs';
import {HttpUtils} from '../../domain/http-utils';
import {map, switchMap, tap} from 'rxjs/operators';
import {AppConstants} from '../../domain/app-constants';
import {StorageService} from '../storage/storage.service';
import {WebSocketService} from '../web-socket/web-socket.service';

@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {

    readonly baseUrl = `${environment.urlApi}/login`;

    constructor(private readonly httpClient: HttpClient,
                private readonly storageService: StorageService,
                private readonly webSocketService: WebSocketService) {
    }

    /***
     * Logs the user in and opens the web socket connection
     */
    login(user: User): Observable<void> {
        return this.httpClient.post<number>(this.baseUrl, user, {
            headers: HttpUtils.getDefaultHeaders().headers,
            observe: 'response'
        })
            .pipe(
                tap(async () => await this.storageService.set(AppConstants.USERNAME, user.username)),
                tap(async res =>
                    await this.storageService.set(AppConstants.ACCESS_TOKEN, res.headers.get(AppConstants.ACCESS_TOKEN))
                ),
                tap(async res =>
                    await this.storageService
                        .set(AppConstants.REFRESH_TOKEN, res.headers.get(AppConstants.REFRESH_TOKEN))
                ),
                tap(async res => await this.storageService.set(AppConstants.USER_ID, res.body)),
                map(() => this.webSocketService.open())
            );
    }

    refreshToken(): Observable<string> {
        return from(this.storageService.get<string>(AppConstants.REFRESH_TOKEN))
            .pipe(
                switchMap(jwtToken => {
                        const headers = HttpUtils.getDefaultHeaders().headers;
                        return this.httpClient.post<string>(this.baseUrl, {}, {
                            headers: {
                                ...headers,
                                refreshToken: jwtToken
                            },
                            observe: 'response'
                        })
                            .pipe(
                                tap(async res => await this.storageService.set(
                                    AppConstants.ACCESS_TOKEN,
                                    res.headers.get(AppConstants.ACCESS_TOKEN)
                                )),
                                tap(async res => await this.storageService.set(
                                    AppConstants.REFRESH_TOKEN,
                                    res.headers.get(AppConstants.REFRESH_TOKEN)
                                )),
                                map(res => res.headers.get(AppConstants.ACCESS_TOKEN))
                            );
                    }
                ));
    }

    /***
     * Clear all cached data and close the web socket
     */
    logout(): void {
        this.storageService.clear();
        this.webSocketService.close();
    }
}
