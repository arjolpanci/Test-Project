import { useState } from "react"
import makeRequest from "../RequestHelper";
import { useNavigate } from "react-router";
import Swal from "sweetalert2";

export default function SignupForm(props: any) {
    const navigate = useNavigate();
    const [user, setUser] = useState('');
    const [email, setEmail] = useState('');
    const [pass, setPass] = useState('');

    const submit = () => {
        makeRequest('/auth/register', 'POST', {
            email: email,
            username: user,
            password: pass
        }, false).then(res => {
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
        <div className="flex flex-col items-center space-y-4">
            <div className="flex items-center justify-between w-full space-x-4">
                <p className="w-1/5"> Email: </p>
                <input onInput={e => setEmail((e.target as HTMLInputElement).value)} className="w-full px-4 py-1 border rounded-full" placeholder="Email" type="text" />
            </div>
            <div className="flex items-center justify-between w-full space-x-4">
                <p className="w-1/5"> Username: </p>
                <input onInput={e => setUser((e.target as HTMLInputElement).value)} className="w-full px-4 py-1 border rounded-full" placeholder="username" type="text" />
            </div>
            <div className="flex items-center justify-between w-full space-x-4">
                <p className="w-1/5"> Password: </p>
                <input onInput={e => setPass((e.target as HTMLInputElement).value)} className="w-full px-4 py-1 border rounded-full" placeholder="password" type="password" />
            </div>
            <button onClick={submit} className="w-full py-2 my-8 font-semibold text-white uppercase bg-blue-600 rounded-full hover:bg-blue-500" type="submit"> Sign Up! </button>
        </div>
    )
} 