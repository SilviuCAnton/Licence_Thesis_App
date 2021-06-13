export interface ToastModel {
    header: string;
    message?: string;
    duration?: number;
    position: 'top' | 'bottom' | 'middle';
    buttons?: ToastButton[];
}

export interface ToastButton {
    text?: string;
    icon?: string;
    side?: 'start' | 'end';
    role?: 'cancel' | string;
    cssClass?: string | string[];
    handler?: () => boolean | void | Promise<boolean | void>;
}
