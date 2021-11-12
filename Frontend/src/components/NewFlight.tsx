import { useState } from "react";
import makeRequest from "../RequestHelper";
import DatePicker from "react-date-picker";
import { useNavigate } from "react-router";
import Swal from "sweetalert2";

export default function NewFlight(props: any) {
    const navigate = useNavigate();
    const [departure, setDeparture] = useState('');
    const [departureTime, setDepartureTime] = useState(new Date());
    const [destination, setDestination] = useState('');
    const [flightClass, setFlightClass] = useState('');

    const submit = () => {
        if(departure == "" && destination == "" && flightClass == "") {
            Swal.fire({
                icon:'error',
                title: "Flight Booker",
                text: "Please fill in all the details!",
                timer: 3000
            });
            return;
        }
        makeRequest('/flights/create', 'POST', {
            departure: departure,
            destination: destination,
            departure_time: departureTime,
            flight_class: flightClass
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

    return (
        <div className="flex flex-col items-center space-y-4 w-120">
            <div className="flex items-center justify-between w-full space-x-4">
                <p className="w-1/5"> Departure: </p>
                <input onInput={e => setDeparture((e.target as HTMLInputElement).value)} className="w-full px-4 py-1 border rounded-full" placeholder="departure" type="text" />
            </div>
            <div className="flex items-center justify-between w-full space-x-4">
                <p className="w-1/5"> Departure Date: </p>
                <DatePicker
                    className="w-full"
                    onChange={setDepartureTime}
                    value={departureTime}
                />
            </div>
            <div className="flex items-center justify-between w-full space-x-4">
                <p className="w-1/5"> Destination: </p>
                <input onInput={e => setDestination((e.target as HTMLInputElement).value)} className="w-full px-4 py-1 border rounded-full" placeholder="destination" type="text" />
            </div>
            <div className="flex items-center justify-between w-full space-x-4">
                <p className="w-1/5"> Flight Class: </p>
                <select value={flightClass} className="w-full px-4 py-1 border rounded-md" onInput={e => setFlightClass((e.target as HTMLInputElement).value)}>
                    <option value="">Choose Class</option>
                    <option value="Economy">Economy</option>
                    <option value="Business">Business</option>
                    <option value="First">First</option>
                </select>
            </div>
            <button onClick={submit} className="w-full py-2 my-8 font-semibold text-white uppercase bg-blue-600 rounded-full hover:bg-blue-500" type="submit"> Add Flight </button>
        </div>
    );
}