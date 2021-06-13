import {HttpHeaders} from '@angular/common/http';

export const httpOptions = {
    headers: new HttpHeaders()
        .set('Content-Type', 'application/json')
        .set('Access-Control-Allow-Origin', '*')
        .set('Access-Control-Expose-Headers', 'accessToken')
        .append('Access-Control-Expose-Headers', 'refreshToken')
};


export const simpleHttpOptions = {
    headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*'
    }),
    responseType : 'text'
};

// tslint:disable-next-line:no-namespace
export namespace HttpUtils {
    export function getDefaultHeaders(): { headers?: HttpHeaders } {
        return httpOptions;
    }

    export function getSimpleHeaders(): { headers?: HttpHeaders } {
        return simpleHttpOptions;
    }
}
