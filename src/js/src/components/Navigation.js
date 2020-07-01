import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Menu } from 'antd';
import { HomeTwoTone, CopyTwoTone, ContactsTwoTone, BankTwoTone, CloseCircleTwoTone } from '@ant-design/icons'

import { logout } from './../api/apiAuth';


export default function Navigation() {
    const [menuOpened, setMenuOpened] = useState(["menu"]);

    const toggleMenu = () => {
        if (menuOpened.length > 0) {
            setMenuOpened([])
        }
        else {
            setMenuOpened(["menu"]);
        }
    }

    return (
        <>
            <Menu mode='inline' style={{ flex: 1 }} theme="light" openKeys={menuOpened} onOpenChange={toggleMenu}>

                    <Menu.Item >
                        <Link to='/home'> <HomeTwoTone size="large"/>Home</Link>
                    </Menu.Item>
                    <Menu.Item >
                        <Link to='/courses'><BankTwoTone />Courses</Link>
                    </Menu.Item>
                    <Menu.Item >
                        <Link to='/resources'><CopyTwoTone />Resources</Link>
                    </Menu.Item>
                    <Menu.Item >
                        <Link to='/profile'><ContactsTwoTone />Profile</Link>
                    </Menu.Item>
                    <Menu.Item >
                        <a href='/' onClick={logout}><CloseCircleTwoTone />Logout</a>
                    </Menu.Item>

            </Menu>
        </>
    );
}