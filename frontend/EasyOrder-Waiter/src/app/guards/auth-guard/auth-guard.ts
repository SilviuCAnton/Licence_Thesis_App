import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {StorageService} from '../../services/storage/storage.service';
import {AppConstants} from '../../domain/app-constants';
import {isDefined} from '@angular/compiler/src/util';
import {from, Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {AppPaths} from '../../domain/app-paths';

@Injectable({providedIn: 'root'})
export class AuthGuard implements CanActivate {

    constructor(private router: Router,
                private readonly storageService: StorageService) {
    }
    /***
     * Returns if the user can access this route.
     * The user is logged in if it has an access token stored in the local storage.
     * If the user does not have an access token redirect to login page.
     */
    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
        return from(this.storageService.get<string>(AppConstants.ACCESS_TOKEN))
            .pipe(
                map(token => {
                    const isLoggedIn = isDefined(token);
                    if (isLoggedIn) {
                        return true;
                    } else {
                        this.router.navigate([AppPaths.LOGIN]).then();
                        return false;
                    }
                })
            );
    }
}
