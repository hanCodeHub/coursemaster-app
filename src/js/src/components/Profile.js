import React from 'react';
import { Typography, Row, Col } from 'antd';
import { fetchFavoriteCourses } from '../api/apiUser';
import { ErrorNotification } from './Notification';

import Container from './Container';
import CourseList from './courses/CourseList';

export default function Profile() {
    let favoriteCourseHeader = 'Here are your favorite courses!';
    const { Title } = Typography;
    const [favoriteCourses, setFavoriteCourses] = React.useState([])

    function getFavoriteCoursesForUser() {
        fetchFavoriteCourses()
        .then(res => res.json())
        .then(favoriteCourses => setFavoriteCourses(favoriteCourses))
        .catch(e => {
            ErrorNotification(
                `${e.error.status} ${e.error.message}`,
                e.error.reason
            );
        })
    }

    React.useEffect(() => {
        getFavoriteCoursesForUser()
    }, [])

    return (
        <>
            <Row justify="center">
                <Col span={24}>
                    <Title className='page-title'>{favoriteCourseHeader}</Title>
                </Col>
            </Row>

            <Container>
                <CourseList data={favoriteCourses}/>
            </Container>
        </>
    )
}