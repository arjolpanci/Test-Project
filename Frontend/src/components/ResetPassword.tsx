import { useState } from "react";
import makeRequest from "../RequestHelper";
import Swal from "sweetalert2";

export default function ResetPassword(props: any) {
    const [email, setEmail] = useState('');

    const sendReset = () => {
        makeRequest(`/auth/password/reset`, "POST", {
            email: email
        }, false).then(res => {
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
    }

    return (
        <div className="p-2 mt-2 w-96">
            <div className="flex items-center space-x-2">
                <p> Email: </p>
                <input value={email} onInput={e => setEmail((e.target as HTMLInputElement).value)} type="text" placeholder="Email" className="w-full px-4 py-2 my-2 border rounded-full" />
            </div>
            <button onClick={sendReset} className="w-full py-2 my-8 font-semibold text-white uppercase bg-blue-600 rounded-full hover:bg-blue-500" type="submit"> Send Reset </button>
        </div>
    );
}