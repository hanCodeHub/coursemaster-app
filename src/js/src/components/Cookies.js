import React, { useEffect, useState } from 'react';
import { Modal, Button } from 'antd';
import { Link } from 'react-router-dom';

export default function Cookies() {
    const [showModal, setShowModal] = useState(false);
    useEffect(() => {
        let acknowledged = localStorage.getItem("acknowledged");
        console.log(acknowledged);
        if (acknowledged === null) {
            setShowModal(true)
        }
    }, [])
    const accept = () => {
        setShowModal(false);
        localStorage.setItem("acknowledged", "yes");
    }

    return (
        <section>
            <Modal visible={showModal} onOk={accept}>
                <p>By using this website you consent to the use of cookies.
                To learn more about how CourseMaster uses your information
                    please visit our privacy policy below.</p>
                <Link to="/legal">Privacy Policy</Link>
            </Modal>
        </section>
    )
}