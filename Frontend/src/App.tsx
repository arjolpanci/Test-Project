import Login from './components/Login';
import User from './user/User';
import Supervisor from './supervisor/Supervisor';

import {
	BrowserRouter as Router,
  Routes,
	Route,
} from 'react-router-dom';

function App() {
  return (
    <div className="flex items-center justify-center w-screen h-screen shadow-xl bg-gradient-to-r from-gray-100 to-gray-200">
      <Router>
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/user/:userId/*" element={<User />} />
          <Route path="/supervisor/:userId/*" element={<Supervisor />} />
        </Routes>
      </Router>
		</div>
  );
}

export default App;
