import React from 'react';
import { Row, Col } from 'antd';
import { Link } from 'react-router-dom';
import Legal from './Legal';

export default function Footer() {
    return (
        <section id='footer-section'>
            <Row justify='center'>
                <Col span={4}>
                    About us
            </Col>
                <Col span={4}>
                    <Link to="/legal">Legal</Link>
                </Col>
                <Col span={4}>
                    Contact us
            </Col>
            </Row>
        </section>
    )
}