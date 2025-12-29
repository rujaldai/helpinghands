import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { userService } from '../../services/userService';
import { useState } from 'react';
import toast from 'react-hot-toast';
import { FiEdit, FiCheck, FiX } from 'react-icons/fi';

const ManageUsers = () => {
  const queryClient = useQueryClient();
  const [editingId, setEditingId] = useState<number | null>(null);
  const [editName, setEditName] = useState('');
  const [editActive, setEditActive] = useState(true);

  const { data: users, isLoading } = useQuery({
    queryKey: ['users'],
    queryFn: userService.getAll,
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: any }) => userService.update(id, data),
    onSuccess: () => {
      toast.success('User updated successfully');
      setEditingId(null);
      queryClient.invalidateQueries({ queryKey: ['users'] });
    },
    onError: (error: any) => {
      toast.error(error.response?.data?.message || 'Failed to update user');
    },
  });

  const handleEdit = (user: any) => {
    setEditingId(user.id);
    setEditName(user.name);
    setEditActive(user.active);
  };

  const handleSave = (id: number) => {
    updateMutation.mutate({
      id,
      data: { name: editName, active: editActive },
    });
  };

  const handleCancel = () => {
    setEditingId(null);
  };

  if (isLoading) {
    return <div className="text-center py-8">Loading users...</div>;
  }

  return (
    <div className="card">
      <div className="card-header">
        <h2 className="card-title">Manage Users</h2>
        <p className="card-description">View and manage all users in the system</p>
      </div>
      <div className="card-content">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b">
                <th className="text-left py-3 px-4">ID</th>
                <th className="text-left py-3 px-4">Name</th>
                <th className="text-left py-3 px-4">Email</th>
                <th className="text-left py-3 px-4">Role</th>
                <th className="text-left py-3 px-4">Status</th>
                <th className="text-left py-3 px-4">Actions</th>
              </tr>
            </thead>
            <tbody>
              {users?.map((user) => (
                <tr key={user.id} className="border-b">
                  <td className="py-3 px-4">{user.id}</td>
                  <td className="py-3 px-4">
                    {editingId === user.id ? (
                      <input
                        type="text"
                        value={editName}
                        onChange={(e) => setEditName(e.target.value)}
                        className="input"
                      />
                    ) : (
                      user.name
                    )}
                  </td>
                  <td className="py-3 px-4">{user.email}</td>
                  <td className="py-3 px-4 capitalize">{user.role.toLowerCase()}</td>
                  <td className="py-3 px-4">
                    {editingId === user.id ? (
                      <select
                        value={editActive ? 'true' : 'false'}
                        onChange={(e) => setEditActive(e.target.value === 'true')}
                        className="input"
                      >
                        <option value="true">Active</option>
                        <option value="false">Inactive</option>
                      </select>
                    ) : (
                      <span
                        className={`px-2 py-1 rounded text-sm ${
                          user.active ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                        }`}
                      >
                        {user.active ? 'Active' : 'Inactive'}
                      </span>
                    )}
                  </td>
                  <td className="py-3 px-4">
                    {editingId === user.id ? (
                      <div className="flex space-x-2">
                        <button
                          onClick={() => handleSave(user.id)}
                          className="text-green-600 hover:text-green-800"
                        >
                          <FiCheck />
                        </button>
                        <button
                          onClick={handleCancel}
                          className="text-red-600 hover:text-red-800"
                        >
                          <FiX />
                        </button>
                      </div>
                    ) : (
                      <button
                        onClick={() => handleEdit(user)}
                        className="text-blue-600 hover:text-blue-800"
                      >
                        <FiEdit />
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default ManageUsers;

