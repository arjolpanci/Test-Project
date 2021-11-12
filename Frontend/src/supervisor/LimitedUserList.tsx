
import { useState, useEffect } from "react";
import makeRequest from "../RequestHelper";
import Swal from 'sweetalert2';

type User = {
    id: number;
    username: string;
    email: string;
};

export default function LimitedUserList(props: any) {
    const [pageNumber, setPageNumber] = useState(0);
    const [users, setUsers] = useState<User[]>([]);

    const reserverForUser = (userId: number) => {
        makeRequest(`/flights/requests/${userId}/create`, 'POST', {flightId: props.flightId}, true)
            .then(res => {
                Swal.fire({
                    icon: res.statusCode === "OK" ? 'success' : 'error',
                    title: "Flight Booker",
                    text: res.statusCode === "OK" ? res.message : "Selected user already has a reservation for this flight!",
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
    }

    useEffect(() => {
        makeRequest("/auth/users", "POST", {pageNumber: pageNumber, pageSize: 5}, true)
            .then(res => {
                setUsers(res.data);
            })
            .catch(error => {
                Swal.fire({
                    icon:'error',
                    title: "Flight Booker",
                    text: error.message,
                    timer: 3000
                });
            });
    }, [pageNumber]);

    return (
        <div className="w-full">
            <div className="flex items-center justify-between p-4 font-semibold text-gray-600 bg-gray-300 rounded-lg">
                <p className="w-full text-center"> Username </p>
                <p className="w-full text-center"> Email </p>
                <p className="w-full text-center"></p>
            </div>
            <div className="my-2 border border-gray-400"></div>
            <div className="flex flex-col p-4 space-y-2 bg-white rounded-lg">
                {users != null && users.length < 1 && <p className="text-gray-400 italics"> No users were found! </p>}
                {users.map(user => (
                    <div className="flex items-center justify-between p-4 bg-gray-100 rounded-lg select-none">
                        <p className="w-full text-center"> {user.username} </p>
                        <p className="w-full text-center"> {user.email} </p>
                        <div className="flex items-center justify-center w-full">
                            <p onClick={() => reserverForUser(user.id)} className="w-24 px-4 py-1 text-center text-white bg-blue-600 rounded-lg cursor-pointer hover:bg-blue-400"> Reserve </p>
                        </div>
                    </div>
                ))}
            </div>
            <div className="my-2 border border-gray-400"></div>
            <div className="flex items-center justify-between px-4 py-1 bg-gray-200">
                <svg onClick={() => { if (pageNumber > 0) { setPageNumber(pageNumber - 1) } }} className="cursor-pointer" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="26" height="26"><path fill="none" d="M0 0h24v24H0z" /><path d="M12 2c5.52 0 10 4.48 10 10s-4.48 10-10 10S2 17.52 2 12 6.48 2 12 2zm0 9V8l-4 4 4 4v-3h4v-2h-4z" /></svg>
                <p> Current Page - {pageNumber + 1} </p>
                <svg onClick={() => { setPageNumber(pageNumber + 1) }} className="cursor-pointer" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="26" height="26"><path fill="none" d="M0 0h24v24H0z" /><path d="M12 2c5.52 0 10 4.48 10 10s-4.48 10-10 10S2 17.52 2 12 6.48 2 12 2zm0 9H8v2h4v3l4-4-4-4v3z" /></svg>
            </div>
        </div>
    )
}