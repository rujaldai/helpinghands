import { Outlet, Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { FiHeart, FiUser, FiLogOut, FiLogIn, FiUserPlus } from 'react-icons/fi';

const MainLayout = () => {
  const { isAuthenticated, user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <div className="min-h-screen flex flex-col">
      <header className="bg-white shadow-sm border-b">
        <nav className="container mx-auto px-4 py-4">
          <div className="flex items-center justify-between">
            <Link to="/" className="flex items-center space-x-2 text-2xl font-bold text-primary">
              <FiHeart className="text-red-500" />
              <span>Helping Hands</span>
            </Link>
            
            <div className="flex items-center space-x-6">
              <Link to="/" className="text-gray-700 hover:text-primary transition">Home</Link>
              <Link to="/causes" className="text-gray-700 hover:text-primary transition">Causes</Link>
              <Link to="/institutions" className="text-gray-700 hover:text-primary transition">Institutions</Link>
              <Link to="/donate" className="text-gray-700 hover:text-primary transition">Donate</Link>
              
              {isAuthenticated ? (
                <div className="flex items-center space-x-4">
                  {user?.role === 'ADMIN' && (
                    <Link
                      to="/admin"
                      className="btn btn-secondary flex items-center space-x-2"
                    >
                      <FiUser />
                      <span>Admin</span>
                    </Link>
                  )}
                  <Link
                    to="/dashboard"
                    className="btn btn-secondary flex items-center space-x-2"
                  >
                    <FiUser />
                    <span>Dashboard</span>
                  </Link>
                  <button
                    onClick={handleLogout}
                    className="btn btn-secondary flex items-center space-x-2"
                  >
                    <FiLogOut />
                    <span>Logout</span>
                  </button>
                </div>
              ) : (
                <div className="flex items-center space-x-2">
                  <Link
                    to="/login"
                    className="btn btn-secondary flex items-center space-x-2"
                  >
                    <FiLogIn />
                    <span>Login</span>
                  </Link>
                  <Link
                    to="/register"
                    className="btn btn-primary flex items-center space-x-2"
                  >
                    <FiUserPlus />
                    <span>Register</span>
                  </Link>
                </div>
              )}
            </div>
          </div>
        </nav>
      </header>

      <main className="flex-1">
        <Outlet />
      </main>

      <footer className="bg-gray-50 border-t mt-auto">
        <div className="container mx-auto px-4 py-8">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div>
              <h3 className="font-bold text-lg mb-4">Helping Hands</h3>
              <p className="text-gray-600">
                Making a difference, one donation at a time.
              </p>
            </div>
            <div>
              <h3 className="font-bold text-lg mb-4">Quick Links</h3>
              <ul className="space-y-2">
                <li><Link to="/causes" className="text-gray-600 hover:text-primary">Causes</Link></li>
                <li><Link to="/institutions" className="text-gray-600 hover:text-primary">Institutions</Link></li>
                <li><Link to="/donate" className="text-gray-600 hover:text-primary">Donate</Link></li>
              </ul>
            </div>
            <div>
              <h3 className="font-bold text-lg mb-4">Transparency</h3>
              <p className="text-gray-600">
                All donations are transparent and publicly viewable.
              </p>
            </div>
          </div>
          <div className="mt-8 pt-8 border-t text-center text-gray-600">
            <p>&copy; 2024 Helping Hands. All rights reserved.</p>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default MainLayout;

