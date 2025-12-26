import { useQuery } from '@tanstack/react-query';
import { donationService } from '../../services/donationService';
import { statementService } from '../../services/statementService';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import toast from 'react-hot-toast';
import { FiHeart, FiMessageSquare, FiDollarSign } from 'react-icons/fi';

const Donations = () => {
  const { data: donations, isLoading } = useQuery({
    queryKey: ['my-donations'],
    queryFn: donationService.getMyDonations,
  });

  const queryClient = useQueryClient();
  const [selectedDonation, setSelectedDonation] = useState<number | null>(null);
  const { register, handleSubmit, reset } = useForm<{ message: string }>();

  const statementMutation = useMutation({
    mutationFn: statementService.create,
    onSuccess: () => {
      toast.success('Message added successfully!');
      reset();
      setSelectedDonation(null);
      queryClient.invalidateQueries({ queryKey: ['statements'] });
    },
    onError: (error: any) => {
      toast.error(error.response?.data?.message || 'Failed to add message');
    },
  });

  const onSubmit = (data: { message: string }) => {
    if (!selectedDonation) return;
    statementMutation.mutate({
      donationId: selectedDonation,
      message: data.message,
    });
  };

  if (isLoading) {
    return <div className="text-center py-8">Loading donations...</div>;
  }

  return (
    <div className="space-y-6">
      <div className="card">
        <div className="card-header">
          <h2 className="card-title">My Donations</h2>
        </div>
        <div className="card-content">
          {donations && donations.length > 0 ? (
            <div className="space-y-4">
              {donations.map((donation) => (
                <div key={donation.id} className="border rounded-lg p-4">
                  <div className="flex justify-between items-start mb-2">
                    <div>
                      <p className="font-semibold text-lg flex items-center space-x-2">
                        <FiDollarSign className="text-primary" />
                        <span>
                          ${donation.amount.toLocaleString()} {donation.currency}
                        </span>
                      </p>
                      <p className="text-gray-600 mt-1">
                        {donation.institutionName || donation.causeName}
                      </p>
                      <p className="text-sm text-gray-500 mt-1">
                        {new Date(donation.createdAt).toLocaleString()}
                      </p>
                    </div>
                  </div>
                  
                  {selectedDonation === donation.id ? (
                    <form onSubmit={handleSubmit(onSubmit)} className="mt-4 space-y-3">
                      <textarea
                        {...register('message')}
                        className="input w-full min-h-[100px]"
                        placeholder="Add a message to this donation..."
                      />
                      <div className="flex space-x-2">
                        <button type="submit" className="btn btn-primary">
                          Save Message
                        </button>
                        <button
                          type="button"
                          onClick={() => setSelectedDonation(null)}
                          className="btn btn-secondary"
                        >
                          Cancel
                        </button>
                      </div>
                    </form>
                  ) : (
                    <button
                      onClick={() => setSelectedDonation(donation.id)}
                      className="btn btn-secondary mt-2 flex items-center space-x-2"
                    >
                      <FiMessageSquare />
                      <span>Add Message</span>
                    </button>
                  )}
                </div>
              ))}
            </div>
          ) : (
            <div className="text-center py-8 text-gray-500">
              <FiHeart className="text-4xl mx-auto mb-4 opacity-50" />
              <p>No donations yet.</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Donations;

