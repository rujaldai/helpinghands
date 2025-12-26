import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { transferService } from '../../services/transferService';
import { institutionService } from '../../services/institutionService';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import toast from 'react-hot-toast';
import { FiPlus, FiArrowRight, FiDollarSign } from 'react-icons/fi';

const ManageTransfers = () => {
  const queryClient = useQueryClient();
  const [isCreating, setIsCreating] = useState(false);

  const { data: transfers, isLoading } = useQuery({
    queryKey: ['transfers'],
    queryFn: transferService.getAll,
  });

  const { data: institutions } = useQuery({
    queryKey: ['institutions'],
    queryFn: institutionService.getAll,
  });

  const { register, handleSubmit, reset, formState: { errors } } = useForm<{
    toInstitutionId: number;
    amount: number;
    currency: string;
    description: string;
  }>();

  const createMutation = useMutation({
    mutationFn: transferService.createFromHost,
    onSuccess: () => {
      toast.success('Transfer created successfully');
      setIsCreating(false);
      reset();
      queryClient.invalidateQueries({ queryKey: ['transfers'] });
      queryClient.invalidateQueries({ queryKey: ['balance'] });
    },
    onError: (error: any) => {
      toast.error(error.response?.data?.message || 'Failed to create transfer');
    },
  });

  const onSubmit = (data: any) => {
    createMutation.mutate(data);
  };

  if (isLoading) {
    return <div className="text-center py-8">Loading transfers...</div>;
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold">Manage Transfers</h2>
        <button
          onClick={() => {
            setIsCreating(true);
            reset();
          }}
          className="btn btn-primary flex items-center space-x-2"
        >
          <FiPlus />
          <span>Transfer Money</span>
        </button>
      </div>

      {isCreating && (
        <div className="card">
          <div className="card-header">
            <h3 className="card-title">Transfer Money from Host Company</h3>
          </div>
          <div className="card-content">
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
              <div>
                <label className="block text-sm font-medium mb-2">To Institution</label>
                <select
                  {...register('toInstitutionId', {
                    required: 'Please select an institution',
                    valueAsNumber: true,
                  })}
                  className="input w-full"
                >
                  <option value="">Choose an institution...</option>
                  {institutions?.filter(inst => !inst.name.includes('Host Company')).map((inst) => (
                    <option key={inst.id} value={inst.id}>
                      {inst.name}
                    </option>
                  ))}
                </select>
                {errors.toInstitutionId && (
                  <p className="text-red-500 text-sm mt-1">{errors.toInstitutionId.message}</p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium mb-2">Amount</label>
                <input
                  type="number"
                  step="0.01"
                  min="0.01"
                  {...register('amount', {
                    required: 'Amount is required',
                    min: { value: 0.01, message: 'Amount must be greater than 0' },
                    valueAsNumber: true,
                  })}
                  className="input w-full"
                />
                {errors.amount && (
                  <p className="text-red-500 text-sm mt-1">{errors.amount.message}</p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium mb-2">Currency</label>
                <select {...register('currency')} className="input w-full">
                  <option value="USD">USD</option>
                  <option value="EUR">EUR</option>
                  <option value="GBP">GBP</option>
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium mb-2">Description</label>
                <textarea
                  {...register('description')}
                  className="input w-full min-h-[100px]"
                  placeholder="Describe the transfer..."
                />
              </div>

              <div className="flex space-x-2">
                <button type="submit" className="btn btn-primary" disabled={createMutation.isPending}>
                  {createMutation.isPending ? 'Transferring...' : 'Transfer Money'}
                </button>
                <button
                  type="button"
                  onClick={() => {
                    setIsCreating(false);
                    reset();
                  }}
                  className="btn btn-secondary"
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      <div className="card">
        <div className="card-content">
          <div className="space-y-4">
            {transfers && transfers.length > 0 ? (
              transfers.map((transfer) => (
                <div key={transfer.id} className="border rounded-lg p-4">
                  <div className="flex justify-between items-start">
                    <div className="flex-1">
                      <div className="flex items-center space-x-2 mb-2">
                        <span className="font-semibold">{transfer.fromInstitutionName}</span>
                        <FiArrowRight className="text-gray-400" />
                        <span className="font-semibold">{transfer.toInstitutionName}</span>
                        <span className="text-2xl font-bold text-primary ml-auto">
                          ${transfer.amount.toLocaleString()}
                        </span>
                      </div>
                      {transfer.description && (
                        <p className="text-gray-600 mb-2">{transfer.description}</p>
                      )}
                      <p className="text-xs text-gray-400">
                        {new Date(transfer.createdAt).toLocaleString()}
                      </p>
                    </div>
                  </div>
                </div>
              ))
            ) : (
              <div className="text-center py-8 text-gray-500">
                <p>No transfers yet.</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ManageTransfers;

