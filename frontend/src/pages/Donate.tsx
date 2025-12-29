import { useState, useEffect } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useForm } from 'react-hook-form';
import { useAuth } from '../context/AuthContext';
import { donationService } from '../services/donationService';
import type { DonationRequest } from '../services/donationService';
import { institutionService } from '../services/institutionService';
import { causeService } from '../services/causeService';
import { authService } from '../services/authService';
import toast from 'react-hot-toast';
import { useNavigate } from 'react-router-dom';
import { FiHeart, FiDollarSign } from 'react-icons/fi';

const Donate = () => {
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [donationType, setDonationType] = useState<'institution' | 'cause' | 'host'>('cause');
  const [guestId, setGuestId] = useState<string>('');

  const { data: institutions } = useQuery({
    queryKey: ['institutions'],
    queryFn: institutionService.getAll,
  });

  const { data: causes } = useQuery({
    queryKey: ['causes'],
    queryFn: causeService.getAll,
  });

  const { register, handleSubmit, formState: { errors }, reset } = useForm<DonationRequest & { message?: string }>();

  // Create guest user if not authenticated
  useEffect(() => {
    if (!isAuthenticated) {
      const storedGuestId = localStorage.getItem('guestId');
      if (storedGuestId) {
        setGuestId(storedGuestId);
      } else {
        authService.createGuest().then((guest) => {
          localStorage.setItem('guestId', guest.guestId || '');
          setGuestId(guest.guestId || '');
        });
      }
    }
  }, [isAuthenticated]);

  const donationMutation = useMutation({
    mutationFn: donationService.createDonation,
    onSuccess: () => {
      toast.success('Donation successful! Thank you for your contribution.');
      reset();
      queryClient.invalidateQueries({ queryKey: ['dashboard-stats'] });
      queryClient.invalidateQueries({ queryKey: ['donations'] });
    },
    onError: (error: any) => {
      toast.error(error.response?.data?.message || 'Donation failed');
    },
  });

  const onSubmit = (data: DonationRequest & { message?: string }) => {
    const donationData: DonationRequest = {
      amount: data.amount,
      currency: data.currency || 'USD',
      ...(donationType === 'host' 
        ? { toHostCompany: true } 
        : donationType === 'institution'
        ? { institutionId: data.institutionId } 
        : { causeId: data.causeId }),
      ...(!isAuthenticated && { guestId }),
    };

    donationMutation.mutate(donationData);
  };

  return (
    <div className="container mx-auto px-4 py-16 max-w-2xl">
      <div className="card">
        <div className="card-header text-center">
          <div className="flex justify-center mb-4">
            <FiHeart className="text-5xl text-red-500" />
          </div>
          <h1 className="card-title">Make a Donation</h1>
          <p className="card-description">
            Support a cause or institution that matters to you
          </p>
        </div>
        <div className="card-content">
          {!isAuthenticated && guestId && (
            <div className="mb-6 p-4 bg-blue-50 rounded-lg">
              <p className="text-sm text-blue-800">
                <strong>Guest ID:</strong> {guestId}
              </p>
              <p className="text-xs text-blue-600 mt-1">
                Save this ID to link your donations when you create an account later.
              </p>
            </div>
          )}

          <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
            {/* Donation Type */}
            <div>
              <label className="block text-sm font-medium mb-2">Donation Type</label>
              <div className="grid grid-cols-3 gap-2">
                <button
                  type="button"
                  onClick={() => setDonationType('cause')}
                  className={`py-2 px-4 rounded-lg border transition ${
                    donationType === 'cause'
                      ? 'bg-primary text-white border-primary'
                      : 'bg-white border-gray-300'
                  }`}
                >
                  Cause
                </button>
                <button
                  type="button"
                  onClick={() => setDonationType('institution')}
                  className={`py-2 px-4 rounded-lg border transition ${
                    donationType === 'institution'
                      ? 'bg-primary text-white border-primary'
                      : 'bg-white border-gray-300'
                  }`}
                >
                  Institution
                </button>
                <button
                  type="button"
                  onClick={() => setDonationType('host')}
                  className={`py-2 px-4 rounded-lg border transition ${
                    donationType === 'host'
                      ? 'bg-primary text-white border-primary'
                      : 'bg-white border-gray-300'
                  }`}
                >
                  Host Company
                </button>
              </div>
            </div>

            {/* Institution, Cause, or Host Company Selection */}
            {donationType === 'host' ? (
              <div className="p-4 bg-blue-50 rounded-lg">
                <p className="text-sm text-blue-800">
                  <strong>Donating to Host Company:</strong> Your donation will be managed by the host company
                  and used for various expenses like clothes, food, and donations to people/institutions.
                  You can track exactly where your money goes through the transparency system.
                </p>
              </div>
            ) : donationType === 'institution' ? (
              <div>
                <label className="block text-sm font-medium mb-2">Select Institution</label>
                <select
                  {...register('institutionId', { required: 'Please select an institution' })}
                  className="input w-full"
                >
                  <option value="">Choose an institution...</option>
                  {institutions?.map((inst) => (
                    <option key={inst.id} value={inst.id}>
                      {inst.name}
                    </option>
                  ))}
                </select>
                {errors.institutionId && (
                  <p className="text-red-500 text-sm mt-1">{errors.institutionId.message}</p>
                )}
              </div>
            ) : (
              <div>
                <label className="block text-sm font-medium mb-2">Select Cause</label>
                <select
                  {...register('causeId', { required: 'Please select a cause' })}
                  className="input w-full"
                >
                  <option value="">Choose a cause...</option>
                  {causes?.map((cause) => (
                    <option key={cause.id} value={cause.id}>
                      {cause.name}
                    </option>
                  ))}
                </select>
                {errors.causeId && (
                  <p className="text-red-500 text-sm mt-1">{errors.causeId.message}</p>
                )}
              </div>
            )}

            {/* Amount */}
            <div>
              <label className="block text-sm font-medium mb-2 flex items-center space-x-2">
                <FiDollarSign />
                <span>Amount</span>
              </label>
              <input
                type="number"
                step="0.01"
                min="0.01"
                {...register('amount', {
                  required: 'Amount is required',
                  min: { value: 0.01, message: 'Amount must be greater than 0' },
                })}
                className="input w-full"
                placeholder="0.00"
              />
              {errors.amount && (
                <p className="text-red-500 text-sm mt-1">{errors.amount.message}</p>
              )}
            </div>

            {/* Currency */}
            <div>
              <label className="block text-sm font-medium mb-2">Currency</label>
              <select {...register('currency')} className="input w-full">
                <option value="USD">USD</option>
                <option value="EUR">EUR</option>
                <option value="GBP">GBP</option>
              </select>
            </div>

            {/* Submit Button */}
            <button
              type="submit"
              disabled={donationMutation.isPending}
              className="btn btn-primary w-full"
            >
              {donationMutation.isPending ? 'Processing...' : 'Donate Now'}
            </button>

            {!isAuthenticated && (
              <p className="text-center text-sm text-gray-600">
                <button
                  type="button"
                  onClick={() => navigate('/register')}
                  className="text-primary hover:underline"
                >
                  Create an account
                </button>
                {' '}to track all your donations
              </p>
            )}
          </form>
        </div>
      </div>
    </div>
  );
};

export default Donate;

