import { useQuery } from '@tanstack/react-query';
import { institutionService } from '../services/institutionService';
import { Link } from 'react-router-dom';
import { FiArrowRight } from 'react-icons/fi';
import { FaBuilding } from "react-icons/fa";

const Institutions = () => {
  const { data: institutions, isLoading } = useQuery({
    queryKey: ['institutions'],
    queryFn: institutionService.getAll,
  });

  if (isLoading) {
    return (
      <div className="container mx-auto px-4 py-16 text-center">
        <div className="text-xl">Loading institutions...</div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-16">
      <div className="mb-12 text-center">
        <h1 className="text-4xl font-bold mb-4">Support an Institution</h1>
        <p className="text-gray-600 text-lg">
          Donate directly to institutions making a difference
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {institutions?.map((institution) => (
          <div key={institution.id} className="card hover:shadow-lg transition">
            <div className="card-header">
              <div className="flex items-center space-x-2 mb-2">
                <FaBuilding className="text-primary" />
                <h3 className="card-title">{institution.name}</h3>
              </div>
              {institution.description && (
                <p className="card-description">{institution.description}</p>
              )}
            </div>
            <div className="card-content">
              {institution.totalDonations && (
                <p className="text-2xl font-bold text-primary mb-2">
                  ${institution.totalDonations.toLocaleString()}
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

export default Institutions;

