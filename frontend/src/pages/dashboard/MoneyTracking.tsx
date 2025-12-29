import { useQuery } from '@tanstack/react-query';
import { useAuth } from '../../context/AuthContext';
import { moneyFlowService } from '../../services/moneyFlowService';
import { FiDollarSign, FiShoppingBag, FiArrowRight} from 'react-icons/fi';

const MoneyTracking = () => {
  const { user } = useAuth();

  const { data: moneyFlows, isLoading } = useQuery({
    queryKey: ['money-flows', user?.id],
    queryFn: () => moneyFlowService.getByUser(user!.id),
    enabled: !!user,
  });

  if (isLoading) {
    return <div className="text-center py-8">Loading money tracking...</div>;
  }

  if (!moneyFlows || moneyFlows.length === 0) {
    return (
      <div className="text-center py-8">
        <p className="text-gray-500">No money flows tracked yet.</p>
        <p className="text-sm text-gray-400 mt-2">
          Your donations will appear here once they are used for expenses or transfers.
        </p>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-2xl font-bold mb-2">Money Tracking</h2>
        <p className="text-gray-600">
          See exactly where your donations are being used. Full transparency.
        </p>
      </div>

      <div className="card">
        <div className="card-content">
          <div className="space-y-4">
            {moneyFlows.map((flow) => (
              <div key={flow.id} className="border rounded-lg p-4">
                <div className="flex justify-between items-start mb-2">
                  <div className="flex-1">
                    <div className="flex items-center space-x-2 mb-2">
                      <FiDollarSign className="text-primary" />
                      <span className="font-semibold">
                        ${flow.flowAmount.toLocaleString()} from your donation of ${flow.donationAmount.toLocaleString()}
                      </span>
                    </div>
                    
                    {flow.expenseId && (
                      <div className="ml-6 mt-2 p-3 bg-blue-50 rounded-lg">
                        <div className="flex items-center space-x-2 mb-1">
                          <FiShoppingBag className="text-blue-600" />
                          <span className="font-medium text-blue-900">Used for Expense</span>
                        </div>
                        {flow.expenseDescription && (
                          <p className="text-sm text-blue-800">{flow.expenseDescription}</p>
                        )}
                        <p className="text-xs text-blue-600 mt-1">
                          Expense ID: {flow.expenseId}
                        </p>
                      </div>
                    )}

                    {flow.transferId && (
                      <div className="ml-6 mt-2 p-3 bg-green-50 rounded-lg">
                        <div className="flex items-center space-x-2 mb-1">
                          <FiArrowRight className="text-green-600" />
                          <span className="font-medium text-green-900">Transferred</span>
                        </div>
                        {flow.transferDescription && (
                          <p className="text-sm text-green-800">{flow.transferDescription}</p>
                        )}
                        <p className="text-xs text-green-600 mt-1">
                          Transfer ID: {flow.transferId}
                        </p>
                      </div>
                    )}
                  </div>
                  <span className="text-xs text-gray-500">
                    {new Date(flow.createdAt).toLocaleDateString()}
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

export default MoneyTracking;

