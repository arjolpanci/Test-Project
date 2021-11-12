import { useState } from "react";
import makeRequest from "../RequestHelper";
import { useEffect } from "react";
import { useNavigate } from "react-router";
import Swal from "sweetalert2";

export default function UpdateFlightRequest(props: any) {
    const [status, setStatus] = useState('');
    const [rejectionReason, setRejectionReason] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        if(props.flightRequest != null) {
            setStatus(props.flightRequest.status);
            setRejectionReason(props.flightRequest.rejection_reason);
        }
    }, [props.flightRequest])

    const updateRequest = () => {
        if(props.flightRequest != null) {
            makeRequest(`/flights/requests/${props.flightRequest.id}/update`, 'PATCH', {
                status: status,
                rejection_reason: rejectionReason
            }, true).then(res => {
                Swal.fire({
                    icon: res.statusCode === "OK" ? 'success' : 'error',
                    title: "Flight Booker",
                    text: res.message,
                    timer: 3000
                });
                if(res.status == 401) {
                    sessionStorage.clear();
                    navigate('/');
                }
            }).catch(error => {
                Swal.fire({
                    icon:'error',
                    title: "Flight Booker",
                    text: error.message,
                    timer: 3000
                });
            });
        }
    }

    return (
        <div className="flex flex-col items-center space-y-4 w-96">
            <div className="flex items-center space-x-4">
                <p> Status: </p>
                {
                    props.flightRequest != null && 
                    <select value={status ? status : props.flightRequest.status} className="px-4 py-1 border rounded-md" onInput={e => setStatus((e.target as HTMLInputElement).value)}>
                        <option value="">Change Status</option>
                        <option value="Pending">Pending</option>
                        <option value="Rejected">Rejected</option>
                        <option value="Accepted">Accepted</option>
                    </select>
                }
            </div>
            {
            status == "Rejected" && 
            <div className="flex items-center space-x-4">
                <p> Rejection Reason: </p>
                <input value={rejectionReason} onInput={e => setRejectionReason((e.target as HTMLInputElement).value)} 
                    type="text" name="rejection_reason" placeholder="Rejection Reason" className="w-full px-4 py-2 my-2 border border-black rounded-full" />
            </div>
            }
            <div className="flex items-center justify-end w-full">
                <p onClick={updateRequest} className="px-4 py-1 text-white bg-blue-600 rounded-lg cursor-pointer select-none hover:bg-blue-500"> Update </p>
            </div>
        </div>
    );
}