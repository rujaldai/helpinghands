import { Outlet, Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { FiHome, FiUser, FiHeart, FiUsers, FiTarget, FiLogOut, FiDollarSign, FiTrendingUp } from 'react-icons/fi';
import { FaBuilding } from "react-icons/fa";

interface DashboardLayoutProps {
  admin?: boolean;
}

const DashboardLayout = ({ admin = false }: DashboardLayoutProps) => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const userMenuItems = [
    { path: '/dashboard', label: 'Dashboard', icon: FiHome },
    { path: '/dashboard/profile', label: 'Profile', icon: FiUser },
    { path: '/dashboard/donations', label: 'My Donations', icon: FiHeart },
    { path: '/dashboard/money-tracking', label: 'Money Tracking', icon: FiTrendingUp },
  ];

  const adminMenuItems = [
    { path: '/admin', label: 'Dashboard', icon: FiHome },
    { path: '/admin/users', label: 'Manage Users', icon: FiUsers },
    { path: '/admin/institutions', label: 'Manage Institutions', icon: FaBuilding },
    { path: '/admin/causes', label: 'Manage Causes', icon: FiTarget },
    { path: '/admin/expenses', label: 'Manage Expenses', icon: FiDollarSign },
    { path: '/admin/transfers', label: 'Manage Transfers', icon: FiTrendingUp },
    { path: '/admin/balance', label: 'Balance', icon: FiDollarSign },
  ];

  const menuItems = admin ? adminMenuItems : userMenuItems;
  const basePath = admin ? '/admin' : '/dashboard';

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="flex">
        {/* Sidebar */}
        <aside className="w-64 bg-white shadow-sm min-h-screen">
          <div className="p-6">
            <h2 className="text-2xl font-bold text-primary mb-8">
              {admin ? 'Admin Panel' : 'Dashboard'}
            </h2>
            <nav className="space-y-2">
              {menuItems.map((item) => {
                const Icon = item.icon;
                const isActive = location.pathname === item.path;
                return (
                  <Link
                    key={item.path}
                    to={item.path}
                    className={`flex items-center space-x-3 px-4 py-3 rounded-lg transition ${
                      isActive
                        ? 'bg-primary text-white'
                        : 'text-gray-700 hover:bg-gray-100'
                    }`}
                  >
                    <Icon />
                    <span>{item.label}</span>
                  </Link>
                );
              })}
            </nav>
          </div>
          <div className="p-6 border-t">
            <button
              onClick={handleLogout}
              className="flex items-center space-x-3 px-4 py-3 rounded-lg text-gray-700 hover:bg-gray-100 w-full transition"
            >
              <FiLogOut />
              <span>Logout</span>
            </button>
          </div>
        </aside>

        {/* Main Content */}
        <main className="flex-1 p-8">
          <div className="mb-6">
            <h1 className="text-3xl font-bold text-gray-900">
              Welcome, {user?.name}!
            </h1>
            {user?.role === 'ADMIN' && (
              <p className="text-gray-600 mt-2">Administrator Dashboard</p>
            )}
          </div>
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default DashboardLayout;

