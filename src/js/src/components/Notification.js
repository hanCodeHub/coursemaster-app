import { notification } from 'antd';

const openNotification = (type, message, description) => {
    notification[type]({
        message,
        description
    });
}

export const SuccessNotification = (message, description) => {
    openNotification('success', message, description);
}

export const InfoNotification = (message, description) => {
    openNotification('info', message, description);
}

export const WarningNotification = (message, description) => {
    openNotification('warning', message, description);
}

export const ErrorNotification = (message, description) => {
    openNotification('error', message, description);
}