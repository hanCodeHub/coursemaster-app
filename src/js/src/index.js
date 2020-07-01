import React from 'react';
import ReactDOM from 'react-dom';
import App from './components/App';
import * as serviceWorker from './config/serviceWorker';

// styles
import 'antd/dist/antd.css';
import './styles/index.css';
import './styles/navigation.css';
import './styles/courses.css';
import './styles/courseReviews.css';
import './styles/login.css';
import './styles/footer.css';
import './styles/forms.css';
import './styles/app.css';

ReactDOM.render(
    <App />
    ,
    document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
