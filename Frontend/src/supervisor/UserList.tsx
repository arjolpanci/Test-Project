import React, { useEffect, useState } from "react";
import Popup from "../components/Popup";
import makeRequest from "../RequestHelper";
import Swal from 'sweetalert2';

type User = {
    id: number;
    username: string;
    email: string;
    role: "user" | "supervisor";
};

export default function UserList(props: any) {
    const [pageNumber, setPageNumber] = useState(0);
    const [users, setUsers] = useState<User[]>([]);
    const [edit, setEdit] = useState<{ show: boolean; user: User | null }>({
        show: false,
        user: null
    });
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
            <AddUser />
            <div className="flex items-center justify-between p-4 font-semibold text-gray-600 bg-gray-300 rounded-lg">
                <p className="w-full text-center"> Username </p>
                <p className="w-full text-center"> Email </p>
                <p className="w-full text-center"> Role </p>
                <p className="w-full text-center"></p>
            </div>
            <div className="my-2 border border-gray-400"></div>
            <div className="flex flex-col p-4 space-y-2 bg-white rounded-lg">
                {users != null && users.length < 1 && <p className="text-gray-400 italics"> No users were found! </p>}
                {users.map(user => (
                    <div className="flex items-center justify-between p-4 bg-gray-100 rounded-lg select-none">
                        <p className="w-full text-center"> {user.username} </p>
                        <p className="w-full text-center"> {user.email} </p>
                        <p className="w-full text-center"> {user.role} </p>
                        <div className="flex items-center justify-center w-full">
                            <p onClick={() => setEdit({ show: true, user: user })} className="w-24 px-4 py-1 text-center text-white bg-blue-600 rounded-lg cursor-pointer hover:bg-blue-400"> Edit </p>
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
            {edit.show ?
                <Popup shouldopen={edit.show} toggle={() => setEdit({ ...edit, show: false })} title="Edit user">
                    {edit.user ? <EditUserForm user={edit.user} /> : null}
                </Popup> : null
            }
        </div>
    );
}

function AddUser(props: any) {
    const [popup, setPopup] = useState(false);
    return (
        <>
            <button onClick={() => setPopup(true)} className="px-4 py-1 m-5 text-center text-white bg-blue-600 rounded-lg cursor-pointer hover:bg-blue-400">
                <svg xmlns="http://www.w3.org/2000/svg" x="0px" y="0px"
                    width="25" height="25"
                    viewBox="0 0 50 50"
                    style={{ fill: "white" }}><path d="M 25 2 C 12.309295 2 2 12.309295 2 25 C 2 37.690705 12.309295 48 25 48 C 37.690705 48 48 37.690705 48 25 C 48 12.309295 37.690705 2 25 2 z M 25 4 C 36.609824 4 46 13.390176 46 25 C 46 36.609824 36.609824 46 25 46 C 13.390176 46 4 36.609824 4 25 C 4 13.390176 13.390176 4 25 4 z M 24 13 L 24 24 L 13 24 L 13 26 L 24 26 L 24 37 L 26 37 L 26 26 L 37 26 L 37 24 L 26 24 L 26 13 L 24 13 z"></path></svg>
            </button>
            <Popup shouldopen={popup} toggle={() => setPopup(false)} title="Add user">
                <AddUserForm />
            </Popup>
        </>
    );
}
function AddUserForm(props: any) {
    const [user, setUser] = useState({
        id: 0,
        username: "",
        password: "",
        email: "",
        role: "user"
    });
    const onChange = (event: React.ChangeEvent) => {
        event.preventDefault();
        const { name, value } = (event.target as any);
        setUser({ ...user, [name]: value });
    }
    const onSubmit = (event: React.MouseEvent) => {
        event.preventDefault();
        makeRequest("/auth/register", "POST", user, true)
            .then(res => {
                Swal.fire({
                    icon: res.statusCode === "OK" ? 'success' : 'error',
                    title: "Flight Booker",
                    text: res.message,
                    timer: 3000
                });
            })
            .catch(error => {
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
                <p className="w-1/5"> Username: </p>
                <input value={user.username} name="username" onChange={onChange} className="w-full px-4 py-1 border rounded-full" placeholder="username" type="text" />
            </div>
            <div className="flex items-center justify-between w-full space-x-4">
                <p className="w-1/5"> Password: </p>
                <input value={user.password} name="password" onChange={onChange} className="w-full px-4 py-1 border rounded-full" placeholder="password" type="password" />
            </div>
            <div className="flex items-center justify-between w-full space-x-4">
                <p className="w-1/5"> Email: </p>
                <input value={user.email} name="email" onChange={onChange} className="w-full px-4 py-1 border rounded-full" placeholder="email" type="text" />
            </div>
            <div className="flex items-center justify-between w-full space-x-4">
                <p className="w-1/5"> Role: </p>
                <select value={user.role} name="role" onChange={onChange} className="w-full px-4 py-1 border rounded-full" placeholder="role">
                    <option value="user">User</option>
                    <option value="supervisor">Supervisor</option>
                </select>
            </div>
            <button onClick={onSubmit} className="w-full py-2 my-8 font-semibold text-white uppercase bg-blue-600 rounded-full hover:bg-blue-500" type="submit"> Register </button>
        </div>
    );
}

function EditUserForm(props: { user: User }) {
    const [user, setUser] = useState(props.user);
    const onChange = (event: React.ChangeEvent) => {
        event.preventDefault();
        const { name, value } = (event.target as any);
        setUser({ ...user, [name]: value });
    }
    const onSubmit = (event: React.MouseEvent) => {
        event.preventDefault();
        makeRequest(`/auth/update/${user.id}`, "PUT", {
            email: user.email,
            role: user.role
        }, true).then(res => {
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
        <div className="flex flex-col items-center space-y-4">
            <div className="flex items-center justify-between w-full space-x-4">
                <p className="w-1/5"> Username: </p>
                <input readOnly value={user.username} name="username" onChange={onChange} className="w-full px-4 py-1 border rounded-full" placeholder="username" type="text" />
            </div>
            <div className="flex items-center justify-between w-full space-x-4">
                <p className="w-1/5"> Email: </p>
                <input value={user.email} name="email" onChange={onChange} className="w-full px-4 py-1 border rounded-full" placeholder="email" type="text" />
            </div>
            <div className="flex items-center justify-between w-full space-x-4">
                <p className="w-1/5"> Role: </p>
                <select value={user.role} name="role" onChange={onChange} className="w-full px-4 py-1 border rounded-full" placeholder="role">
                    <option value="user">User</option>
                    <option value="supervisor">Supervisor</option>
                </select>
            </div>
            <button onClick={onSubmit} className="w-full py-2 my-8 font-semibold text-white uppercase bg-blue-600 rounded-full hover:bg-blue-500" type="submit"> Update </button>
        </div>
    );
}