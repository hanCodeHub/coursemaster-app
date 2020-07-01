import React, { useEffect, useState } from 'react';
import Logo from '../images/CourseMaster-logo-new.png';
import { fetchTopFiveCourses } from '../api/apiCourse';
import CourseList from './courses/CourseList';
import Cookies from './Cookies';

export default function Home() {
        /*
        const [favoriteCourses, setFavoriteCourses] = useState([])
        useEffect(() => {
                fetchTopFiveCourses()
                        .then(result => result.json())
                        .then(setFavoriteCourses)
        }, [])
        */
        return (
                <div>
                        <Cookies/>
                        <div className="jumbotron">
                                <div className="logo">

                                </div>

                        </div>

                </div>

        )
}