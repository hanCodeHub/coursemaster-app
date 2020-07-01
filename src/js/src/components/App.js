import React from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import { Row, Col } from 'antd';
import Login from './Login';
import Navigation from './Navigation';
import CourseCatalog from './courses/CourseCatalog';
import Resources from './Resources';
import Home from './Home';
import Profile from './Profile';
import Footer from './Footer';
import Legal from './Legal';

import Course from './courses/Course';

export default function App() {

    // don't render nav column at login
    const atLogin = window.location.pathname === '/' || window.location.pathname === '/login';
    const navColumn = atLogin ? null :
        <Col className='nav-column'>
            <Navigation />
        </Col>;
    const footer = atLogin ? null : <Footer />;

    return (
        <Router>
            {/* login component is outside of app body */}
            <Route exact path={['/', '/login']} component={Login} />

            <Row style={{ flex: 1 }}>
                {navColumn} {/* does not render at login */}

                <Col id='app-body' span={24}>
                    <Switch>
                        <Route exact path='/home' component={Home} />
                        <Route exact path='/courses' component={CourseCatalog} />
                        <Route path='/courses/:courseId' component={Course} />
                        <Route exact path='/resources' component={Resources} />
                        <Route exact path='/profile' component={Profile} />
                        <Route exact path='/legal' component={Legal} />
                    </Switch>
                    {footer} {/* does not render at login */}
                </Col>
            </Row >
        </Router>
    );
}
