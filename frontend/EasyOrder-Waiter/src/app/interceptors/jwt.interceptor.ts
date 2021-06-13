import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {from, Observable, throwError} from 'rxjs';
import {environment} from '../../environments/environment';
import {StorageService} from '../services/storage/storage.service';
import {AppConstants} from '../domain/app-constants';
import {isDefined} from '@angular/compiler/src/util';
import {catchError, switchMap} from 'rxjs/operators';
import {AuthenticationService} from '../services/authentication/authentication.service';
import {NavController} from '@ionic/angular';
import {AppPaths} from '../domain/app-paths';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

    constructor(private readonly storageService: StorageService,
                private readonly authenticationService: AuthenticationService,
                private readonly navController: NavController) {
    }

    /***
     * adds the jwtToken to the HttpRequest
     */
    private static addToken(jwtToken, request: HttpRequest<any>): HttpRequest<any> {
        const isLoggedIn = isDefined(jwtToken);
        const isApiUrl = request.url.startsWith(environment.urlApi);
        if (isLoggedIn && isApiUrl) {
            request = request.clone({
                setHeaders: {Authorization: jwtToken}
            });
        }
        return request;
    }

    /***
     * Each http request will go trough this method which adds the accessToken found in the localStorage to the request.
     * If the request fails with status code 403 the tokens are updated using refreshToken method and the initial
     * request is remade, this time with the new tokens.
     * However, if the request fails and it requests the login endpoint, this means that the refresh token has expired
     * and the user is redirected to the login page.
     */
    // DO NOT TOUCH WARNING: VERY FRAGILE (se topeste laptopul - Istvan)
    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return from(this.storageService.get<string>(AppConstants.ACCESS_TOKEN))
            .pipe(
                switchMap(jwtToken => {
                    request = JwtInterceptor.addToken(jwtToken, request);
                    if (request.url.endsWith(AppPaths.LOGIN)) {
                        return next.handle(request).pipe(
                            catchError(err => {
                                    if (err.status === 403) {
                                        this.navController.navigateRoot(AppPaths.LOGIN).then();
                                    }
                                    return throwError(err);
                                }
                            ));
                    }
                    return next.handle(request)
                        .pipe(catchError(err => {
                                if (err.status === 403 && !request.url.endsWith(AppPaths.LOGIN)) {
                                    return this.authenticationService.refreshToken()
                                        .pipe(
                                            switchMap(token => {
                                                console.log('Token refreshed');
                                                const remakeRequest = JwtInterceptor.addToken(token, request);
                                                return next.handle(remakeRequest);
                                            })
                                        );
                                } else {
                                    return throwError(err);
                                }
                            })
                        );
                })
            );
    }
}
