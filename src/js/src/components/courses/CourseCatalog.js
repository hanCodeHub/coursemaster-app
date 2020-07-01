import React from 'react';
import { Pagination, Row, Col, Typography, Input, Divider, Button, Space, Anchor, Tooltip } from 'antd';
import { SortAscendingOutlined, SortDescendingOutlined, CloseCircleTwoTone } from '@ant-design/icons';

import Container from './../Container';
import CourseList from './CourseList';
import { ErrorNotification, InfoNotification } from './../Notification';
import { fetchCourseCount, fetchCoursePage, fetchCourseSearch } from './../../api/apiCourse';

const { Title } = Typography;
const { Search } = Input;

export default function CourseCatalog() {
    const [courseCount, setCourseCount] = React.useState(0); // total count of courses
    const [coursePageData, setCoursePageData] = React.useState([]); // one page of courses
    const [currentPage, setCurrentPage] = React.useState(1);
    const [searchMode, setSearchMode] = React.useState(false); // whether user triggered search-box
    const [searchKeywords, setSearchKeywords] = React.useState('');
    const [sortMode, setSortMode] = React.useState('DEFAULT');
    const [sortDirection, setSortDirection] = React.useState('ASC');

    // event handler for fetching course count
    const getCourseCount = () => {
        fetchCourseCount()
            .then(res => res.json())
            .then(count => {
                setCourseCount(count);
            })
            .catch(e => {
                ErrorNotification(
                    `${e.error.status} ${e.error.message}`, 
                    e.error.reason
                );
            });
    }

    // event handler for fetching courses of a given page
    const getCoursePageData = (page, sort, direction) => {
        fetchCoursePage(page, sort, direction)
            .then(res => res.json())
            .then(courses => {
                setCoursePageData(courses);
            })
            .catch(e => {
                ErrorNotification(
                    `${e.error.status} ${e.error.message}`, 
                    e.error.reason
                );
            });
    }

    // event handler for fetching courses based on search keywords for a given page
    const getCourseSearchPageData = (keywords, page, sort, direction) => {
        fetchCourseSearch(keywords, page, sort, direction)
            .then(res => res.json())
            .then(resultsMap => {
                setCourseCount(resultsMap['courseCount'])
                setCoursePageData(resultsMap['searchResults']);
                if (resultsMap['courseCount'] === 0) {
                    InfoNotification('No courses found',
                        'The specified keywords did not produce any search resutls; try modifying keywords.');
                }
                console.log(resultsMap);
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
        getCourseCount();  // get course count to determine number of pages
        getCoursePageData(1, sortMode, sortDirection); // get initial data for page 1
        setCurrentPage(1);  // set initial page marker
    }, []) // [] means only runs once after initial render

    // runs when user selects a page
    const pageSelectHandler = (page, pageSize) => {
        if (searchMode === true) {
            getCourseSearchPageData(searchKeywords, page, sortMode, sortDirection);
        } else {
            getCoursePageData(page, sortMode, sortDirection);
        }
        setCurrentPage(page);
    }

    // runs when user enters a search query
    const courseSearchHandler = (keywords) => {
        // do not allow keywords to include anything other than letters, numbers, and space
        keywords = keywords.replace(/[^a-zA-Z 0-9]/g, "");

        if (keywords.trim() != "") {
            getCourseSearchPageData(keywords, 1, sortMode, sortDirection);
            setCurrentPage(1);
            setSearchMode(true);
            setSearchKeywords(keywords);
        }
    }

    // runs when user sorts the course list by rating
    const sortByRating = () => {
        performSort('RATING', (sortDirection === 'ASC' ? 'DESC' : 'ASC'), searchMode);
    }

    // runs when user sorts the course list by review count
    const sortByReviews = () => {
        performSort('REVIEW_COUNT', (sortDirection === 'ASC' ? 'DESC' : 'ASC'), searchMode);
    }

    // runs when user resets the sort to default
    const resetSortAndSearch = () => {
        // reset course count
        getCourseCount();

        // perform default sort (resetting other applicable variables)
        performSort('DEFAULT', 'ASC', false);
    }

    // common logic for all sort options
    const performSort = (sort, direction, search) => {
        setSortMode(sort)
        setSortDirection(direction)
        setSearchMode(search)

        if (search === true) {
            getCourseSearchPageData(searchKeywords, currentPage, sort, direction);
        } else {
            getCoursePageData(currentPage, sort, direction);
        }
    }


    return (
        <>
            <Row justify="center">
                <Col span={24}>
                    <Title className='page-title'>Course Catalogue</Title>
                </Col>
            </Row>

            <Container>
                <Space className='search-box'>
                    <Search
                        justify="center"
                        placeholder="Search by course number or title ..."
                        size="large"
                        onSearch={courseSearchHandler}
                    />
                </Space>

                <Space className='sort-grid'>
                    <Tooltip title={'Sort by Rating '
                                    + (sortDirection === 'ASC' ? '(descending)' : '(ascending)')}>
                        <Button
                            type='link'
                            size='large'
                            className='sort-option'
                            icon={sortMode !== 'RATING' ? null :
                                sortDirection === 'ASC' ? <SortAscendingOutlined /> : <SortDescendingOutlined />}
                            onClick={sortByRating}>
                                Sort by Rating
                        </Button>
                    </Tooltip>
                    <Divider type='vertical' />

                    <Tooltip title={'Sort by Reviews '
                                    + (sortDirection === 'ASC' ? '(descending)' : '(ascending)')}>
                        <Button
                            type='link'
                            size='large'
                            className='sort-option'
                            icon={sortMode !== 'REVIEW_COUNT' ? null :
                                sortDirection === 'ASC' ? <SortAscendingOutlined /> : <SortDescendingOutlined />}
                            onClick={sortByReviews}>
                                Sort by Reviews
                        </Button>
                    </Tooltip>
                    <Divider type='vertical' />

                    <Tooltip title='Reset Sort & Search'>
                        <Button
                            type='link'
                            size='large'
                            className='reset-sort-option'
                            icon={<CloseCircleTwoTone />}
                            onClick={resetSortAndSearch}>
                                Reset
                        </Button>
                    </Tooltip>
                </Space>

                <Divider className='course-sort-and-search-divider' />

                <CourseList data={coursePageData}/>
            </Container>

            <Row justify="center">
                <Col span={24}>
                    <Pagination
                        current={currentPage}
                        defaultCurrent={1}
                        defaultPageSize={10}
                        showSizeChanger={false}
                        total={courseCount}
                        responsive={true}
                        onChange={pageSelectHandler}
                    />
                </Col>
            </Row>
        </>
    )
}
