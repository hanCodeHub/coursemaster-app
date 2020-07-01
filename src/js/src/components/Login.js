import React from 'react';
import { Row, Col, Divider, Button } from 'antd';
import { Card } from 'antd';
import { Typography } from 'antd';
import { GithubOutlined, GoogleOutlined } from '@ant-design/icons';

import CourseMasterLogo from '../images/CourseMaster-logo-new.png';

const { Title } = Typography;

export default function Login() {
    return (
        <section id='login-section'>

            <Divider id='login-title' orientation="center">
                <Title level={3}>Sign in with your existing accounts</Title>
            </Divider>

            {/* Login area */}
            <Row justify='center'>
                <Col span={24}>
                    <Card id='login-card' bordered={false}>

                        <div id='logo-container'>
                            <img
                                id='logo-image'
                                src={CourseMasterLogo}
                                alt='application logo'
                            />
                        </div>

                        <Button
                            id='github-btn'
                            className='login-btn'
                            shape='round'
                            block size={'large'}
                            href='http://localhost:8080/oauth2/authorization/github'
                        >
                            <GithubOutlined className='btn-icon' />
                            <span className='btn-text'>Sign in with GitHub</span>
                        </Button>

                        <Button
                            id='google-btn'
                            className='login-btn'
                            shape='round'
                            type='primary'
                            block size={'large'}
                            href='http://localhost:8080/oauth2/authorization/google'
                        >
                            <GoogleOutlined className='btn-icon' />
                            <span className='btn-text'>Sign in with Google</span>
                        </Button>

                    </Card>
                </Col>
            </Row>
            
        </section>
    )
}
