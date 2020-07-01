import React from 'react';
import { Card, Row, Col, Typography, Button, Divider, Empty, Modal, Popconfirm } from 'antd'
import { PlusOutlined, DeleteTwoTone, EditTwoTone } from '@ant-design/icons';

import { SuccessNotification, ErrorNotification } from '../Notification';
import AddReviewForm from './../forms/addReviewForm';
import { deleteReview } from '../../api/apiReview';

const { Title } = Typography;

export default function CourseReviews({ reviews, reviewParams }) {
    const [modalVisible, setModalVisible] = React.useState(false);
    const [prefillData, setPrefillData] = React.useState(null);
    const [hasReview, setHasReview] = React.useState(false);
    const { courseData, getCourseData, currentUser, getCurrentUser } = reviewParams;

    // when there are no reviews
    let reviewHeader = 'Here is what students think about this course';
    let emptyComponent = null;
    if (reviews.length === 0) {
        reviewHeader = 'No student reviews found. Click on Add Review to add yours.';
        emptyComponent = <Empty id='empty-reviews' description='' />
    }

    React.useEffect(() => {
        if (Object.keys(currentUser).length === 0)
            return () => { setHasReview(false) };

        const matchedReview = currentUser.reviews.some(userReview => {
            return reviews.some(review => review.id === userReview.id)
        })
        if (matchedReview)
            setHasReview(true);
        else setHasReview(false);
        
    }, [currentUser.reviews])

    return (
        <>
            <Modal
                title='Please answer the following questions and click submit.'
                visible={modalVisible}
                onCancel={() => setModalVisible(false)}
                footer={null}
                width={800}
            >
                <AddReviewForm
                    courseId={courseData.id}
                    currentUser={currentUser}
                    instructors={courseData.instructors}
                    prefillData={prefillData}
                    onSuccess={(action) => {
                        setModalVisible(false);
                        SuccessNotification(`Review ${action}`,
                            "You have successfully shared your review of this course!");
                        getCourseData(courseData.id);
                        getCurrentUser();
                    }}
                    onCancel={() => {
                        setModalVisible(false);
                    }}
                />
            </Modal>
            <Row>
                <Col flex={1}>
                    <Title level={3}  >
                        {reviewHeader}
                    </Title>
                </Col>
                <Col>
                    <Button
                        id='btn-add-review'
                        type='primary'
                        size='large'
                        disabled={hasReview}
                        onClick={() => {
                            setModalVisible(true); // show modal
                            setPrefillData(null); // don't prefill the form
                        }}
                    >
                        <PlusOutlined />
                        Add Review
                    </Button>
                </Col>
            </Row>
            {emptyComponent}
            {reviews.map(review =>
                <ReviewItem
                    key={review.id}
                    courseId={courseData.id}
                    review={review}
                    getCourseData={getCourseData}
                    currentUser={currentUser}
                    getCurrentUser={getCurrentUser}
                    setModalVisible={setModalVisible}
                    setPrefillData={setPrefillData}
                />
            )}
        </>
    );
}


function ReviewItem({ courseId, review, getCourseData, currentUser, getCurrentUser,
    setPrefillData, setModalVisible }) {
    // user.current maintains reference to current user
    const [interactions, setInteractions] = React.useState(null);

    // extract and construct review properties for render
    const userReviews = currentUser.reviews;
    const reviewId = review.id;
    const { comment, courseDate, instructor, rating, userName } = review
    const date = new Date(courseDate);
    const datePart1 = date.toDateString().substring(4, 10); // month day
    const datePart2 = date.toDateString().substring(10); // year
    const dateText = `${datePart1}, ${datePart2}`;
    const roundRating = rating.toFixed(1);

    const handleDelete = () => {
        deleteReview(courseId, reviewId)
            .then(res => {
                getCourseData(courseId);
                getCurrentUser();
                SuccessNotification('Review Deleted',
                    'Your review has been removed from this course.');
            })
            .catch(e => {
                ErrorNotification(
                    `${e.error.status} ${e.error.message}`,
                    e.error.reason
                );
            });
    }

    React.useEffect(() => {
        // prevents memory leaks by escaping early with cleanup
        if (Object.keys(currentUser).length === 0)
            return () => { setInteractions(null) };
        // only reviews by the current user is editable/deletable
        if (currentUser.reviews.some(review => review.id === reviewId)) {
            setInteractions(
                <>
                    <EditTwoTone
                        onClick={() => {
                            setModalVisible(true);
                            setPrefillData(review); // pass review data to edit form
                        }}
                        className='review-card-icon icon-edit'
                    />
                    <Popconfirm
                        title={<h3>Are you sure you want to delete this review?</h3>}
                        onConfirm={handleDelete}
                        okText="Yes"
                        cancelText="No"
                        placement='topRight'
                    >
                        <DeleteTwoTone
                            className='review-card-icon icon-delete'
                        />
                    </Popconfirm>
                </>
            )
        }
    }, [currentUser.reviews]) // only rerender when user reviews updates

    return (
        <>
            <Card
                className='review-card course-text' size='small'
                title={userName}
                extra={`Rating: ${roundRating}`}
            >
                {/* body of review card */}
                <p>{comment}</p>
                <p className='review-instructor'>
                    <strong>Instructor: </strong>{instructor.name}
                </p>
                <Divider className='card-divider' />

                <Row className='review-bottom-row'>
                    <Col flex={1}>
                        <em>{dateText}</em>
                    </Col>
                    <Col>
                        {interactions} {/* not available for other user's reviews */}
                    </Col>
                </Row>
            </Card>
        </>
    )
}
