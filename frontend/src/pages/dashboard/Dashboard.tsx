import { useQuery } from '@tanstack/react-query';
import { dashboardService } from '../../services/dashboardService';
import { donationService } from '../../services/donationService';
import { useAuth } from '../../context/AuthContext';
import { FiTrendingUp, FiUsers, FiTarget, FiDollarSign, FiHeart } from 'react-icons/fi';
import { Link } from 'react-router-dom';

const Dashboard = () => {
  const { user } = useAuth();
  const { data: stats, isLoading: statsLoading } = useQuery({
    queryKey: ['dashboard-stats'],
    queryFn: dashboardService.getStats,
  });

  const { data: myDonations, isLoading: donationsLoading } = useQuery({
    queryKey: ['my-donations'],
    queryFn: donationService.getMyDonations,
    enabled: !!user,
  });

  if (statsLoading || donationsLoading) {
    return <div className="text-center py-8">Loading dashboard...</div>;
  }

  const myTotal = myDonations?.reduce((sum, d) => sum + d.amount, 0) || 0;

  return (
    <div className="space-y-8">
      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <div className="card">
          <div className="card-content">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600 mb-1">My Total Donations</p>
                <p className="text-2xl font-bold text-primary">${myTotal.toLocaleString()}</p>
              </div>
              <FiDollarSign className="text-3xl text-primary opacity-50" />
            </div>
          </div>
        </div>

        <div className="card">
          <div className="card-content">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600 mb-1">My Donations</p>
                <p className="text-2xl font-bold text-primary">{myDonations?.length || 0}</p>
              </div>
              <FiHeart className="text-3xl text-primary opacity-50" />
            </div>
          </div>
        </div>

        <div className="card">
          <div className="card-content">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600 mb-1">Platform Total</p>
                <p className="text-2xl font-bold text-primary">
                  ${stats?.totalDonations.toLocaleString() || 0}
                </p>
              </div>
              <FiTrendingUp className="text-3xl text-primary opacity-50" />
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
      </div>

      {/* Recent Donations */}
      <div className="card">
        <div className="card-header">
          <h2 className="card-title">My Recent Donations</h2>
        </div>
        <div className="card-content">
          {myDonations && myDonations.length > 0 ? (
            <div className="space-y-4">
              {myDonations.slice(0, 5).map((donation) => (
                <div key={donation.id} className="border-b pb-4 last:border-0">
                  <div className="flex justify-between items-start">
                    <div>
                      <p className="font-semibold">
                        ${donation.amount.toLocaleString()} {donation.currency}
                      </p>
                      <p className="text-sm text-gray-600">
                        {donation.institutionName || donation.causeName}
                      </p>
                      <p className="text-xs text-gray-500 mt-1">
                        {new Date(donation.createdAt).toLocaleString()}
                      </p>
                    </div>
                  </div>
                </div>
              ))}
              <Link to="/dashboard/donations" className="text-primary hover:underline text-sm">
                View all donations →
              </Link>
            </div>
          ) : (
            <div className="text-center py-8 text-gray-500">
              <p>No donations yet.</p>
              <Link to="/donate" className="text-primary hover:underline mt-2 inline-block">
                Make your first donation →
              </Link>
            </div>
          )}
        </div>
      </div>

      {/* Top Donators */}
      {stats && stats.topDonators.length > 0 && (
        <div className="card">
          <div className="card-header">
            <h2 className="card-title">Top Donators</h2>
          </div>
          <div className="card-content">
            <div className="space-y-3">
              {stats.topDonators.slice(0, 5).map((donator, index) => (
                <div key={donator.userId} className="flex items-center justify-between">
                  <div className="flex items-center space-x-3">
                    <span className="text-lg font-bold text-gray-400">#{index + 1}</span>
                    <span className="font-medium">{donator.userName}</span>
                  </div>
                  <span className="font-semibold text-primary">
                    ${donator.totalAmount.toLocaleString()}
                  </span>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Dashboard;

