import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {StorageService} from '../storage/storage.service';
import {AppConstants} from '../../domain/app-constants';
import {isDefined} from '@angular/compiler/src/util';
import {environment} from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class WebSocketService {

    private ws: WebSocket;
    messagesSubject = new Subject<any>();

    constructor(private readonly httpClient: HttpClient,
                private readonly storageService: StorageService) {
    }

    /***
     * Closes the web socket connection and completes the subject to avoid memory leaks
     */
    close(): void {
        this.ws.close();
        this.messagesSubject.complete();
    }

    /***
     * Sends the current session id via the web socket in order to identify the current order
     */
    private sendSessionId(): void {
        this.storageService.get<string>(AppConstants.SESSION_ID).then(id => {
            if (!isDefined(id)) {
                console.log('Session id is undefined');
                return;
            }
            this.ws.send(JSON.stringify(id));
        });
    }

    /***
     * Opens the web socket connection and sends the current sessionId
     */
    open() {
        this.ws = new WebSocket(environment.wsApi);
        this.ws.onopen = () => {
            console.log('web socket onopen');
            this.sendSessionId();
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
            this.messagesSubject.next(JSON.parse(messageEvent.data).payload.value);
        };
    }
}
