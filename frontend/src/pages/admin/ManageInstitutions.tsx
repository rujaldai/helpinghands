import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { institutionService } from '../../services/institutionService';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import toast from 'react-hot-toast';
import { FiPlus, FiEdit} from 'react-icons/fi';
import { FaBuilding } from "react-icons/fa";

const ManageInstitutions = () => {
  const queryClient = useQueryClient();
  const [isCreating, setIsCreating] = useState(false);
  const [editingId, setEditingId] = useState<number | null>(null);

  const { data: institutions, isLoading } = useQuery({
    queryKey: ['institutions'],
    queryFn: institutionService.getAll,
  });

  const { register: registerForm, handleSubmit, reset, formState: { errors } } = useForm<{
    name: string;
    description: string;
    active: boolean;
  }>();

  const createMutation = useMutation({
    mutationFn: institutionService.create,
    onSuccess: () => {
      toast.success('Institution created successfully');
      setIsCreating(false);
      reset();
      queryClient.invalidateQueries({ queryKey: ['institutions'] });
    },
    onError: (error: any) => {
      toast.error(error.response?.data?.message || 'Failed to create institution');
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: any }) => institutionService.update(id, data),
    onSuccess: () => {
      toast.success('Institution updated successfully');
      setEditingId(null);
      queryClient.invalidateQueries({ queryKey: ['institutions'] });
    },
    onError: (error: any) => {
      toast.error(error.response?.data?.message || 'Failed to update institution');
    },
  });

  const onSubmit = (data: { name: string; description: string; active: boolean }) => {
    if (editingId) {
      updateMutation.mutate({ id: editingId, data });
    } else {
      createMutation.mutate(data);
    }
  };

  if (isLoading) {
    return <div className="text-center py-8">Loading institutions...</div>;
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold">Manage Institutions</h2>
        <button
          onClick={() => {
            setIsCreating(true);
            setEditingId(null);
            reset();
          }}
          className="btn btn-primary flex items-center space-x-2"
        >
          <FiPlus />
          <span>Add Institution</span>
        </button>
      </div>

      {(isCreating || editingId) && (
        <div className="card">
          <div className="card-header">
            <h3 className="card-title">
              {editingId ? 'Edit Institution' : 'Create Institution'}
            </h3>
          </div>
          <div className="card-content">
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
              <div>
                <label className="block text-sm font-medium mb-2">Name</label>
                <input
                  {...registerForm('name', { required: 'Name is required' })}
                  className="input w-full"
                />
                {errors.name && (
                  <p className="text-red-500 text-sm mt-1">{errors.name.message}</p>
                )}
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">Description</label>
                <textarea
                  {...registerForm('description')}
                  className="input w-full min-h-[100px]"
                />
              </div>
              <div className="flex items-center space-x-2">
                <input
                  type="checkbox"
                  {...registerForm('active')}
                  defaultChecked
                  className="w-4 h-4"
                />
                <label className="text-sm">Active</label>
              </div>
              <div className="flex space-x-2">
                <button type="submit" className="btn btn-primary">
                  {editingId ? 'Update' : 'Create'}
                </button>
                <button
                  type="button"
                  onClick={() => {
                    setIsCreating(false);
                    setEditingId(null);
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
            {institutions?.map((institution) => (
              <div key={institution.id} className="border rounded-lg p-4">
                <div className="flex justify-between items-start">
                  <div className="flex-1">
                    <div className="flex items-center space-x-2 mb-2">
                      <FaBuilding className="text-primary" />
                      <h3 className="font-semibold text-lg">{institution.name}</h3>
                      <span
                        className={`px-2 py-1 rounded text-xs ${
                          institution.active
                            ? 'bg-green-100 text-green-800'
                            : 'bg-red-100 text-red-800'
                        }`}
                      >
                        {institution.active ? 'Active' : 'Inactive'}
                      </span>
                    </div>
                    {institution.description && (
                      <p className="text-gray-600 mb-2">{institution.description}</p>
                    )}
                    {institution.totalDonations && (
                      <p className="text-sm text-gray-500">
                        Total Donations: ${institution.totalDonations.toLocaleString()}
                      </p>
                    )}
                  </div>
                  <button
                    onClick={() => {
                      setEditingId(institution.id);
                      setIsCreating(false);
                      reset({
                        name: institution.name,
                        description: institution.description || '',
                        active: institution.active,
                      });
                    }}
                    className="btn btn-secondary ml-4"
                  >
                    <FiEdit />
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ManageInstitutions;

