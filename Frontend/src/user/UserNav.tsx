import Navbar from '../components/Navbar';
import { Link } from 'react-router-dom';

export default function UserNav(props: any) {
    let path = props.baseLocation;

    return (
        <Navbar>
            <Link className="w-full" to="reservations"> 
                <div className={`flex items-center space-x-6 select-none cursor-pointer text-sm w-full ${path.includes('reservations') ? 'border-b-2 border-white' : ''}`}>
                    <svg className="fill-current" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24"><path fill="none" d="M0 0h24v24H0z"/><path d="M10.478 11.632L5.968 4.56l1.931-.518 6.951 6.42 5.262-1.41a1.5 1.5 0 0 1 .776 2.898L5.916 15.96l-.776-2.898.241-.065 2.467 2.445-2.626.704a1 1 0 0 1-1.133-.48L1.466 10.94l1.449-.388 2.466 2.445 5.097-1.366zM4 19h16v2H4v-2z"/></svg>
                    <p className="w-full"> Reserved flights </p>
                </div>
            </Link>
            <Link className="w-full" to="book"> 
                <div className={`flex items-center space-x-6 select-none cursor-pointer text-sm w-full ${path.includes('book') ? 'border-b-2 border-white' : ''}`}>
                    <svg className="fill-current" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24"><path fill="none" d="M0 0h24v24H0z"/><path d="M19 22H5a3 3 0 0 1-3-3V3a1 1 0 0 1 1-1h14a1 1 0 0 1 1 1v12h4v4a3 3 0 0 1-3 3zm-1-5v2a1 1 0 0 0 2 0v-2h-2zM6 7v2h8V7H6zm0 4v2h8v-2H6zm0 4v2h5v-2H6z"/></svg>
                    <p className="w-full"> Book flight </p>
                </div>
            </Link>
            <Link className="w-full" to="history"> 
                <div className={`flex items-center space-x-6 select-none cursor-pointer text-sm w-full ${path.includes('history') ? 'border-b-2 border-white' : ''}`}>
                    <svg className="fill-current" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24"><path fill="none" d="M0 0H24V24H0z"/><path d="M12 2c5.523 0 10 4.477 10 10s-4.477 10-10 10S2 17.523 2 12h2c0 4.418 3.582 8 8 8s8-3.582 8-8-3.582-8-8-8C9.25 4 6.824 5.387 5.385 7.5H8v2H2v-6h2V6c1.824-2.43 4.729-4 8-4zm1 5v4.585l3.243 3.243-1.415 1.415L11 12.413V7h2z"/></svg>
                    <p className="w-full"> Flight history </p>
                </div>
            </Link>
        </Navbar>  
    );
}