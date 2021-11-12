import { useEffect, useState } from "react";
import makeRequest from "../RequestHelper";
import { useNavigate } from "react-router";
import Swal from 'sweetalert2';

type flightRequest = {
    created_at: Date,
    flight: any,
    user: any,
    id: Number,
    rejection_reason: String,
    status: String
}

export default function RequestHistory(props: any) {
    const navigate = useNavigate();
    const [flightRequests, setFlightRequests] = useState([]);
    const [pageNumber, setPageNumber] = useState(0);

    useEffect(() => {
        makeRequest(`/flights/requests/users/${props.userId}/history`, 'POST', {
            pageSize: 5,
            pageNumber: pageNumber,
        }, true).then(res => {
            if(res.status == 401) {
                sessionStorage.clear();
                navigate('/');
            }
            setFlightRequests(res.data);
        }).catch(error => {
            Swal.fire({
                icon:'error',
                title: "Flight Booker",
                text: error.message,
                timer: 3000
            });
        });
    }, []);

    useEffect(() => {
        makeRequest(`/flights/requests/users/${props.userId}/history`, 'POST', {
            pageSize: 5,
            pageNumber: pageNumber,
        }, true).then(res => {
            if(res.status == 401) {
                sessionStorage.clear();
                navigate('/');
            }
            if(res.statusCode === "OK") {
                setFlightRequests(res.data);
            }
        }).catch(error => {
            Swal.fire({
                icon:'error',
                title: "Flight Booker",
                text: error.message,
                timer: 3000
            });
        });
    }, [pageNumber]);

    return (
        <div className="flex flex-col w-full p-4 border border-gray-400 rounded-lg">
            <div className="flex items-center justify-between p-4 font-semibold text-gray-600 bg-gray-300 rounded-lg">
                <p> Flight </p>
                <p> Departure </p>
                <p> Destination </p>
                <p> Departure Time </p>
                <p> Status </p>
            </div>
            <div className="my-2 border border-gray-400"></div>
            <div className="flex flex-col p-4 space-y-2 bg-white rounded-lg">
                {flightRequests.length < 1 && <p className="text-gray-400 italics"> No flight requests were found! </p>}
                {flightRequests.map((request: flightRequest) => 
                    <div className="flex items-center justify-between p-4 bg-gray-100 rounded-lg select-none">
                        <p> {request.flight.id} </p>
                        <p> {request.flight.departure} </p>
                        <p> {request.flight.destination} </p>
                        <p> {request.flight.departure_time} </p>
                        <p> {request.status} </p>
                    </div>
                )}
            </div>
            <div className="my-2 border border-gray-400"></div>
            <div className="flex items-center justify-between px-4 py-1 bg-gray-200">
                <svg onClick={() => {if(pageNumber > 0){setPageNumber(pageNumber-1)}}} className="cursor-pointer" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="26" height="26"><path fill="none" d="M0 0h24v24H0z"/><path d="M12 2c5.52 0 10 4.48 10 10s-4.48 10-10 10S2 17.52 2 12 6.48 2 12 2zm0 9V8l-4 4 4 4v-3h4v-2h-4z"/></svg>
                <p> Current Page - {pageNumber + 1} </p>
                <svg onClick={() => {setPageNumber(pageNumber+1)}} className="cursor-pointer" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="26" height="26"><path fill="none" d="M0 0h24v24H0z"/><path d="M12 2c5.52 0 10 4.48 10 10s-4.48 10-10 10S2 17.52 2 12 6.48 2 12 2zm0 9H8v2h4v3l4-4-4-4v3z"/></svg>
            </div>
        </div>
    );
}