import React from 'react';
import { useParams } from 'react-router-dom';
import { Empty, Typography, Card, Row, Col, Tooltip } from 'antd';
import { HeartOutlined, HeartFilled, StarFilled } from '@ant-design/icons';

import Container from '../Container';
import CourseTabs from './CourseTabs';
import { fetchCourse } from '../../api/apiCourse';
import { fetchCurrentuser, fetchCurrentUser } from '../../api/apiUser';
import { ErrorNotification } from '../Notification';

const { Title } = Typography;


export default function Course() {
    const { courseId } = useParams(); // parsed from Route
    const [courseData, setCourseData] = React.useState({});
    const [isFavorite, setFavorite] = React.useState(false);
    const [currentUser, setCurrentUser] = React.useState({});

    // on component render handler returns fetching course by id
    const getCourseData = (id) => {
        fetchCourse(id)
            .then(res => res.json())
            .then(data => {
                setCourseData(data);
                console.log(data);
            })
            .catch(e => {
                ErrorNotification(
                    `${e.error.status} ${e.error.message}`, 
                    e.error.reason
                );
            });
    }

    // on component render handler returns current signed in user
    const getCurrentUser = () => {
        fetchCurrentUser()
            .then(res => res.json())
            .then(user => {
                setCurrentUser(user);
                console.log(user);
            })
            .catch(e => {
                ErrorNotification(
                    `${e.error.status} ${e.error.message}`, 
                    e.error.reason
                );
            });
    }

    // runs when component mounts
    React.useEffect(() => {
        getCourseData(courseId);
        getCurrentUser();
    }, [])

    // handles user favoriting this course
    function handleFavorite() {
        isFavorite ? setFavorite(false) : setFavorite(true);
    }

    // extracts properties from courseData
    const { college, department, courseNumber, title, credits, type, description,
        instructors, reviews, averageRating } = courseData;
    const format = type === 'IN_PERSON' ? 'IN PERSON' : type;
    const favoriteText = isFavorite ? 'Unfavorite this course' : 'Favorite this course'
    // course is unrated if averageRating is 0 or null
    const rating = averageRating ? averageRating.toFixed(1) + ' / 5' : 'unrated';

    // object contains parameters for add/edit/delete reviews
    const reviewParams = {
        courseData: courseData,
        getCourseData: getCourseData,
        currentUser: currentUser,
        getCurrentUser: getCurrentUser,
    }

    if (Object.keys(courseData).length !== 0) {
        return (
            <Container>
                <Row className='course-header-row' justify='space-between'>
                    <Col className='course-header-col-1' flex={1}>
                        <Title className='page-title title-left course-title'>
                            {college} {department} {courseNumber} ({format})
                        </Title>
                        <Title level={2} className='course-subtitle' >
                            {title}
                        </Title>
                    </Col>

                    <Col className='course-header-col-2'>
                        <div className='course-fave'>
                            <Tooltip title={favoriteText} placement='left'>
                                {isFavorite ?
                                    <HeartFilled onClick={handleFavorite} className='fave-course-icon' /> :
                                    <HeartOutlined onClick={handleFavorite} className='fave-course-icon' />}
                            </Tooltip>
                        </div>

                        <Title className='course-avg' level={3}>
                            <Tooltip title='Average rating' placement='left'>
                                <span className='course-avg-text'>{rating}</span>
                                <StarFilled className='course-avg-icon' />
                            </Tooltip>
                        </Title>

                    </Col>
                </Row>

                <Card 
                    className='course-overview course-text' 
                    title="Course Overview" 
                    bordered={false}
                >
                    {description}
                    <p className='course-instructors'>
                        <strong>Instructors:</strong>
                        {' ' + instructors.map(instructor => instructor.name).join(', ')}
                    </p>
                    <p className='course-credits'>
                        <strong>Credits:</strong> {credits}
                    </p>
                </Card>

                <CourseTabs 
                    reviewParams={reviewParams}
                    reviews={reviews}
                    /* pass resources in future release */
                /> 

            </Container>
        )
    }
    return <Empty />

}
