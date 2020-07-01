import { checkStatus } from './apiHelpers';
import fetch from 'unfetch';

// MUST PREFIX WITH '/' TO BYPASS RELATIVE PATH

// GET the total number of courses
export const fetchCourseCount = () => fetch('/api/courses/count', {
}).then(checkStatus);

// GET the paginated list of courses of a given page
export const fetchCoursePage = (page, sort, direction) => fetch(
    `/api/courses/pages/${page}?sort=${sort}&direction=${direction}`,
    {}).then(checkStatus);

// GET the course by id
export const fetchCourse = (courseId) => fetch(`/api/courses/${courseId}`, {
}).then(checkStatus);

// GET the course by keywords
export const fetchCourseSearch = (keywords, page, sort, direction) => fetch(
    `/api/courses/search?keywords=${keywords}&page=${page}&sort=${sort}&direction=${direction}`,
    {}).then(checkStatus);

//GET top 5 courses
export const fetchTopFiveCourses = () => fetch('/api/courses/pages/1', {
}).then(checkStatus);
