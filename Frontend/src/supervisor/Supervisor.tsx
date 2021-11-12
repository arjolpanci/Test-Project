import SupervisorNav from "./SupervisorNav";
import { useEffect } from "react";
import {
    Routes,
	Route,
    useParams,
    useNavigate,
    useLocation,
} from 'react-router-dom';
import FlightReservation from "./FlightReservations";
import FlightsTable from "../components/FlightsTable";
import UserList from "./UserList";

export default function Supervisor(props: any) {
    const location = useLocation();
    const navigate = useNavigate();
    const {userId} = useParams();
    let path = location.pathname;

    useEffect(() => {
        if(!sessionStorage.getItem("app-16acfb35-token")) {
            navigate('/');
        }
    },[]);

    return (
        <div className="flex items-center w-full pr-4 space-x-4">
            <SupervisorNav baseLocation={path} />
            <Routes>
                <Route path="reservations" element={<FlightReservation />} />
                <Route path="flights" element={<FlightsTable isUser={false} />} />
                <Route path="users" element={<UserList />} />
            </Routes>
        </div>
    );
}