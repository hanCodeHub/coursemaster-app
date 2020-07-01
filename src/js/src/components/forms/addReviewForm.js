import React from 'react';
import { Button, Input, Rate, Tag, Typography, Select } from 'antd';
import { Formik } from 'formik';

import { createReview, updateReview } from '../../api/apiReview';
import { ErrorNotification } from '../Notification';


const { TextArea } = Input;
const { Text } = Typography;
const { Option } = Select;


export default function addReviewForm({ courseId, currentUser, instructors,
    onSuccess, onCancel, prefillData }) { // prefill data = review when editing

    // payload variables
    const today = new Date();
    const reviewDate = today.toJSON();
    const reviewId = prefillData ? prefillData.id : null;

    return (
        <Formik
            // defines initial values of form controls
            enableReinitialize
            initialValues={{ 
                rating: prefillData ? prefillData.rating : 0,
                instructor: prefillData ? prefillData.instructor.id : 'Please select',
                comment: prefillData ? prefillData.comment : ''
            }}

            // validates inputs - errors are added to errors object
            validate={values => {
                const errors = {};

                // comment validation
                if (!values.comment) {
                    errors.comment = 'Please provide a comment';
                } else if (values.comment.length > 10000) {
                    errors.comment = 'Max characters reached for comment';
                }
                // rating validation
                if (!values.rating) {
                    errors.rating = 'Please choose a rating';
                } else if (values.rating < 1 || values.rating > 5) {
                    errors.rating = 'Rating must be between 1.0 and 5.0 stars';
                }
                // instructor validation
                if (!values.instructor || values.instructor === 'Please select') {
                    errors.instructor = 'Please select the instructor that taught your class';
                }

                return errors;
            }}

            // SUBMIT FORM
            // autoformats form data into JSON object upon submission
            onSubmit={(values, { setSubmitting }) => {

                setSubmitting(false);
                // construct review payload
                const payload = {
                    courseDate: reviewDate,
                    comment: values.comment,
                    rating: values.rating.toFixed(1), 
                    user: {
                        id: currentUser.id
                    },
                    instructor: {
                        id: values.instructor // id is stored on selection
                    }
                }
                try { // send payload to endpoint
                    // reviewId is only available when editing
                    if (reviewId) {
                        updateReview(payload, courseId, reviewId)
                            .then((res) => { onSuccess('Updated') })
                            .catch(e => {
                                ErrorNotification(
                                    `${e.error.status} ${e.error.message}`, 
                                    e.error.reason
                                );
                            });
                    } else {
                        createReview(payload, courseId) // fetch request
                        .then((res) => { onSuccess('Created') })
                        .catch(e => {
                            ErrorNotification(
                                `${e.error.status} ${e.error.message}`, 
                                e.error.reason
                            );
                        });
                    }                    
                } catch (err) {
                    ErrorNotification(err.name, err.message)
                }
            }}
        >
            {({
                values, // contains input values
                setFieldValue,
                errors, // contains input errors
                touched, // checks if user touched inputs
                handleChange,
                handleBlur,
                handleSubmit,
                handleReset,
                isSubmitting, // checks if in submission process
                submitForm,
                isValid // checks if form valid
            }) => (
                    <form id='review-form' onSubmit={handleSubmit}>
                        {/* errors are tagged on top of invalid inputs */}

                        {/* RATING INPUT */}
                        <Text className='input-label'>
                            How satisfied were you with the course?
                        </Text>
                        {errors.rating && touched.rating &&
                            <Tag className='input-tag'>{errors.rating}</Tag>}
                        <Rate
                            name='rating'
                            className='input-control'
                            allowHalf
                            onChange={value => setFieldValue('rating', value)}
                            value={values.rating}
                        />

                        {/* INSTRUCTOR INPUT */}
                        <Text className='input-label'>
                            Which instructor taught your class?
                        </Text>
                        {errors.instructor && touched.instructor &&
                            <Tag className='input-tag'>{errors.instructor}</Tag>}
                        <Select 
                            id='instructor'
                            name='instructor'
                            className='input-control'
                            onChange={value => setFieldValue('instructor', value)}
                            value={values.instructor}
                        >
                            {instructors.map(instructor => {
                                return ( // selection returns instructor id
                                    <Option key={instructor.id} value={instructor.id}>
                                        {instructor.name}
                                    </Option>
                                )
                            })}
                        </Select>

                        {/* COMMENT INPUT */}
                        <Text 
                            className='input-label' 
                            style={{ display: 'block', marginBottom: '.8em' }}
                        >
                            What do you like or don't like about the course?
                        </Text>
                        {errors.comment && touched.comment &&
                            <Tag className='input-tag'>{errors.comment}</Tag>}
                        <TextArea
                            name="comment"
                            size='large'
                            rows={5}
                            className='input-control input-text'
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.comment}
                            placeholder={'I would like to learn more about...'}
                        />

                        {/* BUTTONS */}
                        <div className='form-btn-group'>
                            <Button
                                size='large'
                                className='form-btn'
                                onClick={submitForm}
                                type="primary"
                                // disable submit button until inputs are valid
                                disabled={isSubmitting || (touched && !isValid)}
                            >
                                Submit
                            </Button>

                            <Button
                                size='large'
                                className='form-btn'
                                key='Cancel'
                                type='secondary'
                                onClick={() => {
                                    handleReset();
                                    onCancel();
                                }}
                            >
                                Cancel
                            </Button>
                        </div>
                       
                    </form>
                )}
        </Formik>
    )
}