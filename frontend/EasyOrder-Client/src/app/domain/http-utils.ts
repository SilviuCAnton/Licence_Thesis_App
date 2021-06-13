import {HttpHeaders} from '@angular/common/http';

export const httpOptions = {
    headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*'
    })
};


export const simpleHttpOptions = {
    headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*'
    }),
    responseType: 'text'
};

// tslint:disable-next-line:no-namespace
export namespace HttpUtils {

    /***
     * JSON text response
     */
    export function getDefaultHeaders(): { headers?: HttpHeaders } {
        return httpOptions;
    }

    /***
     * Sample plain text response
     */
    export function getSimpleResponseHeaders(): { headers?: HttpHeaders } {
        return simpleHttpOptions;
    }
}
