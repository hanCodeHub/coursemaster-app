import fetch from 'unfetch';
// import Cookies from 'js-cookie';  USE FOR POST, PUT, DELETE

import { checkStatus } from './apiHelpers';

// GET the current signed in user
export const fetchCurrentUser = () => fetch('/api/users/me').then(checkStatus);

//GET Favorite Courses per user
export const fetchFavoriteCourses = () => fetch(`/api/favoritecourse/me`, {
}).then(checkStatus);
