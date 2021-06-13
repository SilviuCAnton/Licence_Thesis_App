import {Injectable, OnDestroy} from '@angular/core';
import {noop, Observable, Subject} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {StorageService} from '../storage/storage.service';
import {AppConstants} from '../../domain/app-constants';
import {isDefined} from '@angular/compiler/src/util';
import {environment} from '../../../environments/environment';
import {filter} from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
})
export class WebSocketService implements OnDestroy {

    private ws: WebSocket;
    private readonly messagesSubject = new Subject<any>();

    constructor(private readonly httpClient: HttpClient,
                private readonly storageService: StorageService) {
        this.storageService.get<number>(AppConstants.USER_ID).then(id => isDefined(id) ? this.open() : noop());
    }

    getMessages(): Observable<any> {
        return this.messagesSubject
            .asObservable()
            .pipe(
                filter(value => isDefined(value))
            );
    }
    /***
     * Removes the observers but do not mark subject as complete in order to add subscriptions later
     * Closes the web socket connection
     */
    close(): void {
        this.ws.close();
        this.messagesSubject.observers = [];
    }

    private sendUserId(): void {
        this.storageService.get<number>(AppConstants.USER_ID).then(id => {
            if (!isDefined(id)) {
                console.log('User id is undefined');
                return;
            }
            this.ws.send(JSON.stringify(id));
        });
    }

    /***
     * Opens the web socket connection and identifies the waiter connected by sending the userId
     */
    open() {
        this.ws = new WebSocket(environment.wsApi);
        this.ws.onopen = () => {
            console.log('web socket onopen');
            this.sendUserId();
        };
        this.ws.onclose = () => {
            console.log('web socket onclose');
        };
        this.ws.onerror = error => {
            console.log('web socket onerror', error);
        };
        this.ws.onmessage = messageEvent => {
            console.log('web socket onmessage');
            console.log(messageEvent);
            const message = JSON.parse(messageEvent.data).payload.value;
            this.messagesSubject.next(message);
        };
    }

    /***
     * Marks subject as complete to avoid memory leaks
     * Closes the web socket connection
     */
    ngOnDestroy(): void {
        this.ws.close();
        this.messagesSubject.complete();
    }
}
