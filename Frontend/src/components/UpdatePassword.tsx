import { useState } from "react";
import makeRequest from "../RequestHelper";
import Swal from "sweetalert2";

export default function UpdatePassword(props: any) {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [token, setToken] = useState('');

    const update = () => {
        makeRequest(`/auth/password/update`, "POST", {
            email: email,
            token: token,
            password: password,
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
                <p className="w-full"> Token: </p>
                <input value={token} onInput={e => setToken((e.target as HTMLInputElement).value)} type="text" placeholder="Token" className="w-full px-4 py-2 my-2 border rounded-full" />
            </div>
            <div className="flex items-center space-x-2">
                <p className="w-full"> Email: </p>
                <input value={email} onInput={e => setEmail((e.target as HTMLInputElement).value)} type="text" placeholder="Email" className="w-full px-4 py-2 my-2 border rounded-full" />
            </div>
            <div className="flex items-center space-x-2">
                <p className="w-full"> New Password: </p>
                <input value={password} onInput={e => setPassword((e.target as HTMLInputElement).value)} type="password" placeholder="Password" className="w-full px-4 py-2 my-2 border rounded-full" />
            </div>
            <button onClick={update} className="w-full py-2 my-8 font-semibold text-white uppercase bg-blue-600 rounded-full hover:bg-blue-500" type="submit"> Update Password </button>
        </div>
    );
}