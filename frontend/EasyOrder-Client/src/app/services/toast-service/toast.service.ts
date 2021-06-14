import {Injectable} from '@angular/core';
import {ToastController} from '@ionic/angular';
import {ToastModel} from 'src/app/domain/toast';

@Injectable({
    providedIn: 'root'
})
export class ToastService {

    public sessionId: string;

    constructor(public toastController: ToastController) {
    }

    async toast(toastModel: ToastModel): Promise<any> {
        const toast = await this.toastController.create({
            header: toastModel.header,
            message: toastModel.message,
            position: toastModel.position,
            duration: toastModel.duration,
            buttons: toastModel.buttons,
        });
        return toast.present();
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
