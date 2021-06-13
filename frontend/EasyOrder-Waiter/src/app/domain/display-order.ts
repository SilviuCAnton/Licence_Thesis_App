import {DisplayMenuItem} from './display-menu-item';

export class DisplayOrder {
    id: number;
    comments: string;
    tableId: number;
    orderDate: Date;
    displayMenuItems: DisplayMenuItem[];
}
