import FlightRow from "./FlightRow";
import { useState, useEffect } from "react";
import makeRequest from "../RequestHelper";
import { useNavigate } from "react-router";
import DatePicker from "react-date-picker";
import Popup from "./Popup";
import NewFlight from "./NewFlight";
import Swal from "sweetalert2";

export default function FlightsTable(props: any) {
    const navigate = useNavigate();
    const [flightPopup, setFlightPopup] = useState(false);
    const [pageNumber, setPageNumber] = useState(0);
    const [destination, setDestination] = useState('');
    const [departure, setDeparture] = useState('');
    const [beforeDate, changeBefore] = useState(new Date());
    const [afterDate, changeAfter] = useState(new Date());
    const [flights, setFlights] = useState([]);

    const toggleFlightPopup = () => {
        setFlightPopup(!flightPopup);
    }

    const updateFlights = () => {
        makeRequest(`/flights`, 'POST', {
            destinationSearchString: destination,
            departureSearchString: departure,
            beforeDate: beforeDate,
            afterDate: afterDate,
            incomingPageRequest: {      
                pageSize: 5,
                pageNumber: pageNumber,
            }
        }, true).then(res => {
            if(res.status == 401) {
                sessionStorage.clear();
                navigate('/');
            }
            if(res.statusCode === "OK") {
                console.log(res);
                setFlights(res.data);
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

    useEffect(() => {
        updateFlights();
    }, [pageNumber]);

    return (
        <div className="w-full">
            <Popup shouldopen={flightPopup ? true : false} toggle={toggleFlightPopup} title="New Flight"> 
                <NewFlight />
            </Popup>
            <div className="flex items-center justify-between px-4 py-2 mb-4 bg-white">
                <div className="flex items-center space-x-4">
                    <div className="flex items-center space-x-1">
                        <p> Departure: </p>
                        <input value={departure} onInput={e => setDeparture((e.target as HTMLInputElement).value)} type="text" placeholder="departure" className="w-full px-2 py-1 border border-gray-700" />
                    </div>
                    <div className="flex items-center space-x-1">
                        <p> Destination: </p>
                        <input value={destination} onInput={e => setDestination((e.target as HTMLInputElement).value)} type="text" placeholder="destination" className="w-full px-2 py-1 border border-gray-700" />
                    </div>
                    <div className="flex items-center space-x-1">
                        <p> Between: </p>
                        <DatePicker
                            onChange={changeBefore}
                            value={beforeDate}
                        />
                        <span> - </span>
                        <DatePicker
                            onChange={changeAfter}
                            value={afterDate}
                        />
                    </div>
                    <p onClick={updateFlights} className="px-2 py-1 text-white bg-green-700 rounded-lg cursor-pointer select-none hover:bg-green-600"> Apply Filters </p>
                </div>
                {!props.isUser && <p onClick={toggleFlightPopup} className="px-2 py-1 text-white bg-blue-600 cursor-pointer select-none hover:bg-blue-500"> Add New </p>}
            </div>
            {flights == null || flights.length < 1 && <p className="w-full px-2 py-1 mb-2 bg-white"> No flights were retrieved for the specified paramters! </p>}
            {flights != null && flights.length > 0 && flights.map(f => <FlightRow flight={f} isUser={props.isUser} userId={props.userId} />)}
            <div className="flex items-center justify-between px-4 py-1 bg-white">
                <svg onClick={() => {if(pageNumber > 0){setPageNumber(pageNumber-1)}}} className="cursor-pointer" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="26" height="26"><path fill="none" d="M0 0h24v24H0z"/><path d="M12 2c5.52 0 10 4.48 10 10s-4.48 10-10 10S2 17.52 2 12 6.48 2 12 2zm0 9V8l-4 4 4 4v-3h4v-2h-4z"/></svg>
                <p> Current Page - {pageNumber + 1} </p>
                <svg onClick={() => {setPageNumber(pageNumber+1)}} className="cursor-pointer" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="26" height="26"><path fill="none" d="M0 0h24v24H0z"/><path d="M12 2c5.52 0 10 4.48 10 10s-4.48 10-10 10S2 17.52 2 12 6.48 2 12 2zm0 9H8v2h4v3l4-4-4-4v3z"/></svg>
            </div>
        </div>
    );
}