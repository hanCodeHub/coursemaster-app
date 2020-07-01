import React from 'react';
import { Row, Col } from 'antd';

const Container = props => (
    <Row justify="center">
        <Col span={22}>
            {props.children}
        </Col>
    </Row>
)

export default Container;