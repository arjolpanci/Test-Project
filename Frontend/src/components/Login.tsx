import { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";
import makeRequest from '../RequestHelper';
import Popup from './Popup';
import SignupForm from './SignupForm';
import ResetPassword from './ResetPassword';
import UpdatePassword from './UpdatePassword';
import Swal from 'sweetalert2';

export default function Login(props: any) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [signUpPopup, setSignUp] = useState(false);
    const [resetPopup, setReset] = useState(false);
    const [updatePopup, setUpdate] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        let uid = sessionStorage.getItem("app-16acfb35-uid");
        let role = sessionStorage.getItem("app-16acfb35-role");
        let token = sessionStorage.getItem("app-16acfb35-token");

        if(token && role && uid) {
            navigate(`/${role}/${uid}/flights`);
        }
    });

    const handleLogin = () => {
        makeRequest("/auth/login", "POST", {
            username: username,
            password: password,
        }, false).then(res => {
            if(res.statusCode === "OK") {
                sessionStorage.setItem("app-16acfb35-token", res.data.token);
                sessionStorage.setItem("app-16acfb35-role", res.data.role);
                sessionStorage.setItem("app-16acfb35-uid", res.data.userId);
                navigate(`/${res.data.role}/${res.data.userId}/reservations`);
            }else {
                Swal.fire({
                    icon: res.statusCode === "OK" ? 'success' : 'error',
                    title: "Flight Booker",
                    text: res.message,
                    timer: 3000
                });
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

    const toggleSignup = () => {
        setSignUp(!signUpPopup);
    }

    const toggleReset = () => {
        setReset(!resetPopup);
    }

    const toggleUpdate = () => {
        setUpdate(!updatePopup);
    }

    return (
        <div className="px-8 py-4 mx-auto shadow-xl bg-gradient-to-b from-gray-700 to-black rounded-xl">
            <Popup shouldopen={signUpPopup ? true : false} toggle={toggleSignup} title="Sign Up Form"> 
                <SignupForm />
            </Popup>
            <Popup shouldopen={resetPopup ? true : false} toggle={toggleReset} title="Reset Password"> 
                <ResetPassword />
            </Popup>
            <Popup shouldopen={updatePopup ? true : false} toggle={toggleUpdate} title="Update Password"> 
                <UpdatePassword />
            </Popup>
            <div className="flex flex-col items-center">
                <div className="flex flex-col items-center pt-8 space-y-4 font-semibold text-white">
                    <p className="text-3xl"> Login to continue! </p>
                    <div className="text-black">
                        <input value={username} onInput={e => setUsername((e.target as HTMLInputElement).value)} type="text" name="username" placeholder="username" className="w-full px-4 py-2 my-2 rounded-full" />
                        <input value={password} onInput={e => setPassword((e.target as HTMLInputElement).value)} type="password" name="password" placeholder="password" className="w-full px-4 py-2 my-2 rounded-full" />
                    </div>
                </div>

                <button onClick={handleLogin} className="w-full py-2 my-8 text-xl font-semibold text-white uppercase bg-blue-600 rounded-full hover:bg-blue-500"> LOGIN </button>

                <div className="flex items-center justify-between w-full mt-8 border-t-2 border-gray-400">
                    <div className="flex items-center space-x-1">
                        <p onClick={toggleReset} className="p-1 text-sm text-right text-white cursor-pointer select-none"> Reset Password </p>
                        <p className="text-white"> | </p>
                        <p onClick={toggleUpdate} className="p-1 text-sm text-right text-white cursor-pointer select-none"> Update Password </p>
                    </div>
                    <p onClick={toggleSignup} className="p-1 text-sm text-right text-white cursor-pointer select-none"> Sign Up! </p>
                </div>
            </div>
        </div>
    );
}