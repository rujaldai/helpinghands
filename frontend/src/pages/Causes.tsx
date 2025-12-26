import { useQuery } from '@tanstack/react-query';
import { causeService } from '../services/causeService';
import { Link } from 'react-router-dom';
import { FiTarget, FiArrowRight } from 'react-icons/fi';

const Causes = () => {
  const { data: causes, isLoading } = useQuery({
    queryKey: ['causes'],
    queryFn: causeService.getAll,
  });

  if (isLoading) {
    return (
      <div className="container mx-auto px-4 py-16 text-center">
        <div className="text-xl">Loading causes...</div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-16">
      <div className="mb-12 text-center">
        <h1 className="text-4xl font-bold mb-4">Support a Cause</h1>
        <p className="text-gray-600 text-lg">
          Choose from various causes and make a difference
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {causes?.map((cause) => (
          <div key={cause.id} className="card hover:shadow-lg transition">
            <div className="card-header">
              <div className="flex items-center space-x-2 mb-2">
                <FiTarget className="text-primary" />
                <h3 className="card-title">{cause.name}</h3>
              </div>
              {cause.description && (
                <p className="card-description">{cause.description}</p>
              )}
            </div>
            <div className="card-content">
              {cause.totalDonations && (
                <p className="text-2xl font-bold text-primary mb-2">
                  ${cause.totalDonations.toLocaleString()}
                </p>
              )}
              <p className="text-sm text-gray-600">Total Donations</p>
            </div>
            <div className="card-footer">
              <Link
                to="/donate"
                className="btn btn-primary flex items-center space-x-2"
              >
                <span>Donate Now</span>
                <FiArrowRight />
              </Link>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Causes;

