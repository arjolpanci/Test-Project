import UserNav from "./UserNav";
import { useEffect } from "react";
import {
    Routes,
	Route,
    useParams,
    useNavigate,
    useLocation,
} from 'react-router-dom';
import ReservedFlights from "../components/ReservedFlights";
import FlightsTable from "../components/FlightsTable";
import RequestHistory from "./FlightRequestHistory";

export default function User(props: any) {
    const location = useLocation();
    const navigate = useNavigate();
    const {userId} = useParams();
    let path = location.pathname;

    useEffect(() => {
        if(!sessionStorage.getItem("app-16acfb35-token")) {
            navigate('/');
        }
    }, []);

    return (
        <div className="flex items-center w-full pr-4 space-x-4">
            <UserNav baseLocation={path} />
            <Routes>
                <Route path="reservations" element={<ReservedFlights userId={userId}/>} />
                <Route path="book" element={<FlightsTable isUser={true} userId={userId} />} />
                <Route path="history" element={<RequestHistory userId={userId} />} />
            </Routes>
        </div>
    );
}