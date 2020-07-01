// generic response handler must return Promise to caller
export const checkStatus = response => {
    // return response to caller if status ok
    if (response.ok) {
        return response;
    // redirect to login page if authentication fails
    } else if (response.status === 401 || response.status === 403) {
        window.location.href='/';
    } 
    // custom object for other errors
    const error = new Error(response.statusText);
    error.response = response;
    response.json().then(err => {
        error.error = err;
    });
    return Promise.reject(error);
}
