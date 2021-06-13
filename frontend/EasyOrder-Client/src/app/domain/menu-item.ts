export class MenuItem {
    id: number;
    name: string;
    description: string;
    price: number;
    photoPath: string;
    available: boolean;
    category: string;
}

export interface MenuItemWrapper {
    nickname?: string;
    menuItem: MenuItem;
    frequency: number;
}
