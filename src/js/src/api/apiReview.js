import fetch from 'unfetch';
import Cookies from 'js-cookie';

import { checkStatus } from './apiHelpers';

export const createReview = (payload, courseId) => 
    fetch(`/api/courses/${courseId}/reviews`, {
    method: 'POST',
    headers: {
        'X-XSRF-TOKEN': Cookies.get('XSRF-TOKEN'),
        'Content-Type': 'application/json'
    },
    body: JSON.stringify(payload)
}).then(checkStatus);

export const updateReview = (payload, courseId, reviewId) =>
    fetch(`/api/courses/${courseId}/reviews/${reviewId}`, {
        method: 'PUT',
        headers: {
            'X-XSRF-TOKEN': Cookies.get('XSRF-TOKEN'),
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    }).then(checkStatus);


// courseId not used currently. review is deleted directly by id.
export const deleteReview = (courseId, reviewId) => 
    fetch(`/api/courses/${courseId}/reviews/${reviewId}`, {
    method: 'DELETE',
    headers: {
        'X-XSRF-TOKEN': Cookies.get('XSRF-TOKEN'),
        'Content-Type': 'application/json'
    }
}).then(checkStatus);