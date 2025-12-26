import { useQuery } from '@tanstack/react-query';
import { balanceService } from '../../services/balanceService';
import { FiDollarSign, FiTrendingUp, FiTrendingDown, FiArrowRight } from 'react-icons/fi';

const Balance = () => {
  const { data: hostBalance, isLoading: hostLoading } = useQuery({
    queryKey: ['balance', 'host-company'],
    queryFn: balanceService.getHostCompanyBalance,
  });

  const { data: allBalances, isLoading: allLoading } = useQuery({
    queryKey: ['balance', 'all'],
    queryFn: balanceService.getAllBalances,
  });

  if (hostLoading || allLoading) {
    return <div className="text-center py-8">Loading balances...</div>;
  }

  return (
    <div className="space-y-8">
      <h2 className="text-2xl font-bold">Balance Overview</h2>

      {/* Host Company Balance */}
      {hostBalance && (
        <div className="card">
          <div className="card-header">
            <h3 className="card-title flex items-center space-x-2">
              <FiDollarSign />
              <span>Host Company Balance</span>
            </h3>
          </div>
          <div className="card-content">
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-4">
              <div>
                <p className="text-sm text-gray-600 mb-1">Total Donations</p>
                <p className="text-2xl font-bold text-green-600">
                  ${hostBalance.totalDonations.toLocaleString()}
                </p>
              </div>
              <div>
                <p className="text-sm text-gray-600 mb-1">Total Expenses</p>
                <p className="text-2xl font-bold text-red-600">
                  ${hostBalance.totalExpenses.toLocaleString()}
                </p>
              </div>
              <div>
                <p className="text-sm text-gray-600 mb-1">Outgoing Transfers</p>
                <p className="text-2xl font-bold text-orange-600">
                  ${hostBalance.totalOutgoingTransfers.toLocaleString()}
                </p>
              </div>
              <div>
                <p className="text-sm text-gray-600 mb-1">Incoming Transfers</p>
                <p className="text-2xl font-bold text-blue-600">
                  ${hostBalance.totalIncomingTransfers.toLocaleString()}
                </p>
              </div>
              <div>
                <p className="text-sm text-gray-600 mb-1">Available Balance</p>
                <p className={`text-2xl font-bold ${
                  hostBalance.availableBalance >= 0 ? 'text-green-600' : 'text-red-600'
                }`}>
                  ${hostBalance.availableBalance.toLocaleString()}
                </p>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* All Institution Balances */}
      <div className="card">
        <div className="card-header">
          <h3 className="card-title">All Institution Balances</h3>
        </div>
        <div className="card-content">
          <div className="space-y-4">
            {allBalances && allBalances.length > 0 ? (
              allBalances.map((balance) => (
                <div key={balance.institutionId} className="border rounded-lg p-4">
                  <div className="flex justify-between items-start mb-4">
                    <h4 className="font-semibold text-lg">{balance.institutionName}</h4>
                    <span className={`text-xl font-bold ${
                      balance.availableBalance >= 0 ? 'text-green-600' : 'text-red-600'
                    }`}>
                      ${balance.availableBalance.toLocaleString()}
                    </span>
                  </div>
                  <div className="grid grid-cols-4 gap-4 text-sm">
                    <div>
                      <p className="text-gray-600">Donations</p>
                      <p className="font-semibold text-green-600">
                        ${balance.totalDonations.toLocaleString()}
                      </p>
                    </div>
                    <div>
                      <p className="text-gray-600">Expenses</p>
                      <p className="font-semibold text-red-600">
                        ${balance.totalExpenses.toLocaleString()}
                      </p>
                    </div>
                    <div>
                      <p className="text-gray-600">Outgoing</p>
                      <p className="font-semibold text-orange-600">
                        ${balance.totalOutgoingTransfers.toLocaleString()}
                      </p>
                    </div>
                    <div>
                      <p className="text-gray-600">Incoming</p>
                      <p className="font-semibold text-blue-600">
                        ${balance.totalIncomingTransfers.toLocaleString()}
                      </p>
                    </div>
                  </div>
                </div>
              ))
            ) : (
              <div className="text-center py-8 text-gray-500">
                <p>No balances available.</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Balance;

