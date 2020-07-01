import React from 'react';
import { Tabs, Typography } from 'antd';
import { MessageTwoTone, CopyTwoTone } from '@ant-design/icons';

import CourseReviews from './CourseReviews';

const { Title } = Typography;
const { TabPane } = Tabs;

export default function CourseTabs({ reviews, reviewParams }) {
    return (
        <Tabs id='course-tabs-section' defaultActiveKey="1">
            <TabPane
                className='course-tab'
                tab={<span><MessageTwoTone /> Reviews</span>}
                key="1"
            >
                <CourseReviews reviews={reviews} reviewParams={reviewParams} />
            </TabPane>
            <TabPane
                className='course-tab'
                tab={<span><CopyTwoTone /> Resources</span>}
                key="2"
            >
                <Title level={3}>Resources live here.</Title>
            </TabPane>
        </Tabs>
    )
}