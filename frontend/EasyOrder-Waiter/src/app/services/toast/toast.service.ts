import {Injectable} from '@angular/core';
import {ToastController} from '@ionic/angular';

@Injectable({
    providedIn: 'root'
})
export class ToastService {

    constructor(private readonly toastController: ToastController) {

    }

    async presentError(message: string) {
        const toast = await this.toastController.create({
            message,
            position: 'top',
            duration: 2000,
            color: 'danger'
        });
        toast.present().then();
    }
}
