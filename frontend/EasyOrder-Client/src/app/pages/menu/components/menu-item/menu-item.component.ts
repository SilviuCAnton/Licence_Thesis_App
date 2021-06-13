import { Component, Input, OnInit } from '@angular/core';
import { ModalController } from '@ionic/angular';
import { MenuItem } from 'src/app/domain/menu-item';
import { MenuSection } from 'src/app/domain/menu-section';
import { ToastService } from 'src/app/services/toast-service/toast.service';
import { MenuItemDescriptionComponent } from '../menu-item-description/menu-item-description.component';
import {DisplayMenuItem} from '../../../../domain/display-menu-item';

@Component({
  selector: 'app-menu-item',
  templateUrl: './menu-item.component.html',
  styleUrls: ['./menu-item.component.scss'],
})
export class MenuItemComponent implements OnInit {

  @Input() menuSection: MenuSection;
  constructor(private toastService: ToastService,
              public modalController: ModalController) { }

  ngOnInit() {}

  showItem(menuItem: DisplayMenuItem){
    if (menuItem.available === false){
        this.toastService.toast({
          header: 'Acest preparat nu este valabil momentan!',
          duration: 2000,
          position: 'bottom',
        });
    }
    else{
      this.presentModal(menuItem);
    }
  }

  async presentModal(menuItem: DisplayMenuItem) {
    const modal = await this.modalController.create({
      component: MenuItemDescriptionComponent,
      componentProps: {
        menuItem,
      }
    });
    return await modal.present();
  }
}
