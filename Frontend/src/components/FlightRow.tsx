import makeRequest from "../RequestHelper";
import Swal from "sweetalert2";
import { useState } from "react";
import Popup from "./Popup";
import LimitedUserList from "../supervisor/LimitedUserList";

export default function FlightRow(props: any) {
    const [userPopup, setUserPopup] = useState(false);

    const role = sessionStorage.getItem("app-16acfb35-role");

    const registerRequest = (fid: number) => {
        if(props.isUser) {
            makeRequest(`/flights/requests/${props.userId}/create`, 'POST', {flightId: fid}, true)
            .then(res => {
                Swal.fire({
                    icon: res.statusCode === "OK" ? 'success' : 'error',
                    title: "Flight Booker",
                    text: res.message,
                    timer: 3000
                });
            }).catch(error => {
                Swal.fire({
                    icon:'error',
                    title: "Flight Booker",
                    text: error.message,
                    timer: 3000
                });
            });
        } else {
            toggleUserPopup();
        }
    }

    const toggleUserPopup = () => {
        setUserPopup(!userPopup);
    }

    return (
        <div className="flex items-center justify-between w-full px-4 py-2 my-1 space-x-2 bg-white rounded-md">
            {role === 'supervisor' && 
                <Popup shouldopen={userPopup ? true : false} toggle={toggleUserPopup} title="New Flight"> 
                    <LimitedUserList flightId={props.flight.id} />
                </Popup>
            }
            <div className="flex flex-col items-center w-full px-4 py-2 space-y-4 bg-indigo-400">
                <p className="pb-2 text-lg font-semibold text-center text-white border-b border-b-white"> Flight ID </p>
                <p className="py-2 text-gray-800"> {props.flight.id} </p>
            </div>
            <div className="flex flex-col items-center w-full px-4 py-2 space-y-4 bg-indigo-400">
                <p className="pb-2 text-lg font-semibold text-center text-white border-b border-b-white"> Departs From </p>
                <p className="py-2 text-gray-800"> {props.flight.departure} </p>
            </div>
            <div className="flex flex-col items-center w-full px-4 py-2 space-y-4 bg-indigo-400">
                <p className="pb-2 text-lg font-semibold text-center text-white border-b border-b-white"> Destination </p>
                <p className="py-2 text-gray-800"> {props.flight.destination} </p>
            </div>
            <div className="flex flex-col items-center w-full px-4 py-2 space-y-4 bg-indigo-400">
                <p className="pb-2 text-lg font-semibold text-center text-white border-b border-b-white"> Departure Time </p>
                <p className="py-2 text-gray-800"> {props.flight.departure_time} </p>
            </div>
            <div className="flex flex-col items-center w-full px-4 py-2 space-y-4 bg-indigo-400">
                <p className="pb-2 text-lg font-semibold text-center text-white border-b border-b-white"> Flight Class </p>
                <p className="py-2 text-gray-800"> {props.flight.flight_class} </p>
            </div>
            <p onClick={() => registerRequest(props.flight.id)} className="px-2 py-1 text-white bg-blue-600 rounded-md cursor-pointer select-none hover:bg-blue-500"> Reserve </p>
        </div>
    );
}