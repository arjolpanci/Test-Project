import { useNavigate } from "react-router";

export default function Navbar(props: any) {
    const navigate = useNavigate();

    const signOut = () => {
        sessionStorage.clear();
        navigate('/');
    }

    return (
        <nav className="flex flex-col items-center justify-between flex-shrink-0 w-64 h-screen py-8 space-y-12 text-white bg-purple-700 rounded-tr-xl rounded-br-xl">
            <div className="flex flex-col items-center space-y-4">
                <div className="flex items-center mb-4 space-x-2 border-b border-gray-400 select-none">
                    <img className="w-12" src="/logo.svg" alt="image" />
                    <p className="py-6 text-xl"> Flight Manager </p>
                </div>
                <div {...props} className="flex flex-col items-center space-y-6">
                </div>
            </div>
            <div onClick={signOut} className="cursor-pointer select-none">
                <p> Sign Out </p>
            </div>
        </nav>
    );
}