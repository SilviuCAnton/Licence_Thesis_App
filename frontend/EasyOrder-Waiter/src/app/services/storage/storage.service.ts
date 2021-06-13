import {Injectable} from '@angular/core';
import {Plugins} from '@capacitor/core';
import {from} from 'rxjs';
import {map} from 'rxjs/operators';

const {Storage} = Plugins;

@Injectable({
    providedIn: 'root'
})
export class StorageService {

    async set(key: string, value: any): Promise<void> {
        await Storage.remove({key});
        await Storage.set({key, value: JSON.stringify(value)});
    }

    async get<T>(key: string): Promise<T> {
        const item = await Storage.get({key});
        return JSON.parse(item.value) as T;
    }

    async remove(key: string): Promise<void> {
        await Storage.remove({key});
    }

    clear(): void {
        from(Storage.keys())
            .pipe(
                map(keys => keys.keys),
            )
            .subscribe(keys => keys.forEach(key => Storage.remove({key}).then()));
    }
}
