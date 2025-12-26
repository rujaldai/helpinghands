import { useQuery } from '@tanstack/react-query';
import { dashboardService } from '../../services/dashboardService';
import { balanceService } from '../../services/balanceService';
import { FiTrendingUp, FiUsers, FiTarget, FiDollarSign, FiHeart } from 'react-icons/fi';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, PieChart, Pie, Cell } from 'recharts';
import { FaBuilding } from "react-icons/fa";
import { Link } from 'react-router-dom';

const AdminDashboard = () => {
  const { data: stats, isLoading } = useQuery({
    queryKey: ['dashboard-stats'],
    queryFn: dashboardService.getStats,
  });

  const { data: hostBalance } = useQuery({
    queryKey: ['balance', 'host-company'],
    queryFn: balanceService.getHostCompanyBalance,
  });

  const COLORS = ['#3b82f6', '#ef4444', '#10b981', '#f59e0b', '#8b5cf6'];

  if (isLoading) {
    return <div className="text-center py-8">Loading dashboard...</div>;
  }

  return (
    <div className="space-y-8">
      {/* Host Company Balance Card */}
      {hostBalance && (
        <div className="card bg-gradient-to-r from-blue-50 to-purple-50">
          <div className="card-content">
            <div className="flex justify-between items-center">
              <div>
                <h3 className="text-lg font-semibold mb-2">Host Company Balance</h3>
                <p className="text-3xl font-bold text-primary">
                  ${hostBalance.availableBalance.toLocaleString()}
                </p>
                <p className="text-sm text-gray-600 mt-1">Available to use</p>
              </div>
              <Link to="/admin/balance" className="btn btn-primary">
                View Details
              </Link>
            </div>
          </div>
        </div>
      )}

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <div className="card">
          <div className="card-content">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600 mb-1">Total Donations</p>
                <p className="text-2xl font-bold text-primary">
                  ${stats?.totalDonations.toLocaleString() || 0}
                </p>
              </div>
              <FiDollarSign className="text-3xl text-primary opacity-50" />
            </div>
          </div>
        </div>

        <div className="card">
          <div className="card-content">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600 mb-1">Total Donators</p>
                <p className="text-2xl font-bold text-primary">
                  {stats?.totalDonators.toLocaleString() || 0}
                </p>
              </div>
              <FiUsers className="text-3xl text-primary opacity-50" />
            </div>
          </div>
        </div>

        <div className="card">
          <div className="card-content">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600 mb-1">Institutions</p>
                <p className="text-2xl font-bold text-primary">
                  {stats?.totalInstitutions || 0}
                </p>
              </div>
              <FaBuilding className="text-3xl text-primary opacity-50" />
            </div>
          </div>
        </div>

        <div className="card">
          <div className="card-content">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600 mb-1">Causes</p>
                <p className="text-2xl font-bold text-primary">
                  {stats?.totalCauses || 0}
                </p>
              </div>
              <FiTarget className="text-3xl text-primary opacity-50" />
            </div>
          </div>
        </div>
      </div>

      {/* Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Top Donators */}
        <div className="card">
          <div className="card-header">
            <h2 className="card-title flex items-center space-x-2">
              <FiUsers />
              <span>Top Donators</span>
            </h2>
          </div>
          <div className="card-content">
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={stats?.topDonators.slice(0, 10) || []}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="userName" />
                <YAxis />
                <Tooltip formatter={(value) => `$${value.toLocaleString()}`} />
                <Bar dataKey="totalAmount" fill="#3b82f6" />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Top Causes */}
        <div className="card">
          <div className="card-header">
            <h2 className="card-title flex items-center space-x-2">
              <FiTarget />
              <span>Top Causes</span>
            </h2>
          </div>
          <div className="card-content">
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie
                  data={stats?.topCauses.slice(0, 5) || []}
                  dataKey="totalAmount"
                  nameKey="causeName"
                  cx="50%"
                  cy="50%"
                  outerRadius={100}
                  label
                >
                  {(stats?.topCauses.slice(0, 5) || []).map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip formatter={(value) => `$${value.toLocaleString()}`} />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>

      {/* Recent Statements */}
      <div className="card">
        <div className="card-header">
          <h2 className="card-title flex items-center space-x-2">
            <FiHeart />
            <span>Recent Donation Messages</span>
          </h2>
        </div>
        <div className="card-content">
          <div className="space-y-4">
            {stats?.recentStatements.slice(0, 10).map((statement) => (
              <div key={statement.id} className="border-b pb-4 last:border-0">
                <div className="flex justify-between items-start">
                  <div>
                    <p className="font-semibold">{statement.userName}</p>
                    <p className="text-gray-600 text-sm">
                      Donated ${statement.donationAmount.toLocaleString()}
                    </p>
                    {statement.message && (
                      <p className="mt-2 text-gray-700">{statement.message}</p>
                    )}
                  </div>
                  <span className="text-sm text-gray-500">
                    {new Date(statement.createdAt).toLocaleDateString()}
                  </span>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;

