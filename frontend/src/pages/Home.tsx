import { Link } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { dashboardService } from '../services/dashboardService';
import { FiHeart, FiUsers, FiTarget } from 'react-icons/fi';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, PieChart, Pie, Cell } from 'recharts';

const Home = () => {
  const { data: stats, isLoading } = useQuery({
    queryKey: ['dashboard-stats'],
    queryFn: dashboardService.getStats,
  });

  const COLORS = ['#3b82f6', '#ef4444', '#10b981', '#f59e0b', '#8b5cf6'];

  return (
    <div>
      {/* Hero Section */}
      <section className="bg-gradient-to-r from-blue-600 to-purple-600 text-white py-20">
        <div className="container mx-auto px-4 text-center">
          <h1 className="text-5xl font-bold mb-6">Make a Difference Today</h1>
          <p className="text-xl mb-8 max-w-2xl mx-auto">
            Join thousands of donors supporting causes and institutions that matter.
            Every donation counts, and every contribution is transparent.
          </p>
          <Link
            to="/donate"
            className="inline-block bg-white text-blue-600 px-8 py-3 rounded-lg font-semibold hover:bg-gray-100 transition"
          >
            Start Donating
          </Link>
        </div>
      </section>

      {/* Stats Section */}
      {!isLoading && stats && (
        <section className="py-16 bg-white">
          <div className="container mx-auto px-4">
            <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-12">
              <div className="card text-center">
                <div className="card-content">
                  <div className="text-4xl font-bold text-primary mb-2">
                    ${stats.totalDonations.toLocaleString()}
                  </div>
                  <div className="text-gray-600">Total Donations</div>
                </div>
              </div>
              <div className="card text-center">
                <div className="card-content">
                  <div className="text-4xl font-bold text-primary mb-2">
                    {stats.totalDonators.toLocaleString()}
                  </div>
                  <div className="text-gray-600">Total Donators</div>
                </div>
              </div>
              <div className="card text-center">
                <div className="card-content">
                  <div className="text-4xl font-bold text-primary mb-2">
                    {stats.totalInstitutions}
                  </div>
                  <div className="text-gray-600">Institutions</div>
                </div>
              </div>
              <div className="card text-center">
                <div className="card-content">
                  <div className="text-4xl font-bold text-primary mb-2">
                    {stats.totalCauses}
                  </div>
                  <div className="text-gray-600">Causes</div>
                </div>
              </div>
            </div>

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
                    <BarChart data={stats.topDonators.slice(0, 5)}>
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
                        data={stats.topCauses.slice(0, 5)}
                        dataKey="totalAmount"
                        nameKey="causeName"
                        cx="50%"
                        cy="50%"
                        outerRadius={100}
                        label
                      >
                        {stats.topCauses.slice(0, 5).map((_entry, index) => (
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
            <div className="card mt-8">
              <div className="card-header">
                <h2 className="card-title flex items-center space-x-2">
                  <FiHeart />
                  <span>Recent Donation Messages</span>
                </h2>
              </div>
              <div className="card-content">
                <div className="space-y-4">
                  {stats.recentStatements.slice(0, 5).map((statement) => (
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
        </section>
      )}

      {/* Features Section */}
      <section className="py-16 bg-gray-50">
        <div className="container mx-auto px-4">
          <h2 className="text-3xl font-bold text-center mb-12">Why Choose Helping Hands?</h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div className="text-center">
              <div className="text-4xl mb-4">🔒</div>
              <h3 className="text-xl font-semibold mb-2">Fully Transparent</h3>
              <p className="text-gray-600">
                Every donation is publicly visible. See exactly where your money goes.
              </p>
            </div>
            <div className="text-center">
              <div className="text-4xl mb-4">🎯</div>
              <h3 className="text-xl font-semibold mb-2">Choose Your Cause</h3>
              <p className="text-gray-600">
                Donate to specific institutions or causes that matter to you.
              </p>
            </div>
            <div className="text-center">
              <div className="text-4xl mb-4">💬</div>
              <h3 className="text-xl font-semibold mb-2">Share Your Message</h3>
              <p className="text-gray-600">
                Add a personal message to your donations and inspire others.
              </p>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;

