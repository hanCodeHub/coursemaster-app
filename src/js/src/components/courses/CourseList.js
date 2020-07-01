import React from 'react';
import { List, Typography } from 'antd';
import { Link } from 'react-router-dom';

const { Text } = Typography;


export default function courseList({ data }) {
    return (
        <List
            itemLayout="vertical"
            dataSource={data}
            size="large"
            renderItem={course => (
                <CourseItem course={course} />
            )}
        />
    );
}


function CourseItem({ course }) {

    const title = course.fullTitle;

    // course is unrated if averageRating is 0 or null
    const rating = course.averageRating ? 
        course.averageRating.toFixed(1) + ' / 5' : 'unrated';

    // subtitle is first sentence of description
    let regex, subtitle;
    try {
        regex = /.*?(\.)(?=\s[A-Z])/;
        subtitle = course.description.match(regex)[0];
    } catch (err) {  // if no period
        subtitle = course.description;
    }

    // concatenates instructor names into string if any
    const instructorList = course.instructors.map(instructor => instructor.name).join(', ');
    const instructors = instructorList ? `Instructors: ${instructorList}` : 'No instructors found'

    return (
        <>
            <List.Item
                key={course.id}
                extra={rating}
            >
                <List.Item.Meta
                    title={
                        <Link to={`/courses/${course.id}`}>
                            {title}
                        </Link>
                    }
                    description={[subtitle]}
                />
                <Text className="course-item-content">
                    {instructors}
                </Text>
            </List.Item>
        </>
    )
}
