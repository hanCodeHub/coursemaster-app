import fetch from 'unfetch';
import Cookies from 'js-cookie';

import { checkStatus } from './apiHelpers';


export const logout = () => {
    fetch('/logout', {
        method: 'POST',
        headers: {
            'X-XSRF-TOKEN': Cookies.get('XSRF-TOKEN')
        }
    }).then(checkStatus).then(res => window.location.href = "http://localhost:3000/");
}