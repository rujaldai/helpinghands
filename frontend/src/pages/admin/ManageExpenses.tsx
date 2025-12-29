import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { expenseService } from '../../services/expenseService';
import {EXPENSE_CATEGORIES} from "../../services/expenseCategory.ts";
import type {ExpenseCategory} from "../../services/expenseCategory.ts";
import { institutionService } from '../../services/institutionService';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import toast from 'react-hot-toast';
import { FiPlus, FiDollarSign, FiShoppingBag, FiUsers } from 'react-icons/fi';
import { FaBuilding } from "react-icons/fa";

const ManageExpenses = () => {
  const queryClient = useQueryClient();
  const [isCreating, setIsCreating] = useState(false);

  const { data: expenses, isLoading } = useQuery({
    queryKey: ['expenses'],
    queryFn: expenseService.getAll,
  });

  const { data: institutions } = useQuery({
    queryKey: ['institutions'],
    queryFn: institutionService.getAll,
  });

  const { register, handleSubmit, reset, watch, formState: { errors } } = useForm<{
    amount: number;
    currency: string;
    category: ExpenseCategory;
    description: string;
    recipient: string;
    recipientInstitutionId: number;
  }>();

  const selectedCategory = watch('category');

  const createMutation = useMutation({
    mutationFn: expenseService.create,
    onSuccess: () => {
      toast.success('Expense created successfully');
      setIsCreating(false);
      reset();
      queryClient.invalidateQueries({ queryKey: ['expenses'] });
      queryClient.invalidateQueries({ queryKey: ['balance'] });
    },
    onError: (error: any) => {
      toast.error(error.response?.data?.message || 'Failed to create expense');
    },
  });

  const onSubmit = (data: any) => {
    const expenseData: any = {
      amount: data.amount,
      currency: data.currency || 'USD',
      category: data.category,
      description: data.description,
    };

    if (data.category === EXPENSE_CATEGORIES.DONATION_TO_PERSON && data.recipient) {
      expenseData.recipient = data.recipient;
    }

    if (data.category === EXPENSE_CATEGORIES.DONATION_TO_INSTITUTION && data.recipientInstitutionId) {
      expenseData.recipientInstitutionId = data.recipientInstitutionId;
    }

    createMutation.mutate(expenseData);
  };

  const getCategoryIcon = (category: ExpenseCategory) => {
    switch (category) {
      case EXPENSE_CATEGORIES.CLOTHES:
        return <FiShoppingBag />;
      case EXPENSE_CATEGORIES.FOOD:
        return '🍔';
      case EXPENSE_CATEGORIES.DONATION_TO_PERSON:
        return <FiUsers />;
      case EXPENSE_CATEGORIES.DONATION_TO_INSTITUTION:
        return <FaBuilding />;
      default:
        return <FiDollarSign />;
    }
  };

  if (isLoading) {
    return <div className="text-center py-8">Loading expenses...</div>;
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold">Manage Expenses</h2>
        <button
          onClick={() => {
            setIsCreating(true);
            reset();
          }}
          className="btn btn-primary flex items-center space-x-2"
        >
          <FiPlus />
          <span>Add Expense</span>
        </button>
      </div>

      {isCreating && (
        <div className="card">
          <div className="card-header">
            <h3 className="card-title">Create Expense</h3>
          </div>
          <div className="card-content">
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
              <div>
                <label className="block text-sm font-medium mb-2">Category</label>
                <select
                  {...register('category', { required: 'Category is required' })}
                  className="input w-full"
                >
                  <option value="">Select category...</option>
                  <option value={EXPENSE_CATEGORIES.CLOTHES}>Clothes</option>
                  <option value={EXPENSE_CATEGORIES.FOOD}>Food</option>
                  <option value={EXPENSE_CATEGORIES.DONATION_TO_PERSON}>Donation to Person</option>
                  <option value={EXPENSE_CATEGORIES.DONATION_TO_INSTITUTION}>Donation to Institution</option>
                  <option value={EXPENSE_CATEGORIES.OTHER}>Other</option>
                </select>
                {errors.category && (
                  <p className="text-red-500 text-sm mt-1">{errors.category.message}</p>
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
                  placeholder="Describe the expense..."
                />
              </div>

              {selectedCategory === EXPENSE_CATEGORIES.DONATION_TO_PERSON && (
                <div>
                  <label className="block text-sm font-medium mb-2">Recipient Name</label>
                  <input
                    type="text"
                    {...register('recipient')}
                    className="input w-full"
                    placeholder="Person's name"
                  />
                </div>
              )}

              {selectedCategory === EXPENSE_CATEGORIES.DONATION_TO_INSTITUTION && (
                <div>
                  <label className="block text-sm font-medium mb-2">Recipient Institution</label>
                  <select
                    {...register('recipientInstitutionId', {
                      required: 'Please select an institution',
                    })}
                    className="input w-full"
                  >
                    <option value="">Choose an institution...</option>
                    {institutions?.map((inst) => (
                      <option key={inst.id} value={inst.id}>
                        {inst.name}
                      </option>
                    ))}
                  </select>
                  {errors.recipientInstitutionId && (
                    <p className="text-red-500 text-sm mt-1">{errors.recipientInstitutionId.message}</p>
                  )}
                </div>
              )}

              <div className="flex space-x-2">
                <button type="submit" className="btn btn-primary" disabled={createMutation.isPending}>
                  {createMutation.isPending ? 'Creating...' : 'Create Expense'}
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
            {expenses && expenses.length > 0 ? (
              expenses.map((expense) => (
                <div key={expense.id} className="border rounded-lg p-4">
                  <div className="flex justify-between items-start">
                    <div className="flex-1">
                      <div className="flex items-center space-x-2 mb-2">
                        <span className="text-xl">{getCategoryIcon(expense.category)}</span>
                        <h3 className="font-semibold text-lg capitalize">
                          {expense.category.toLowerCase().replace(/_/g, ' ')}
                        </h3>
                        <span className="text-2xl font-bold text-primary">
                          ${expense.amount.toLocaleString()}
                        </span>
                      </div>
                      {expense.description && (
                        <p className="text-gray-600 mb-2">{expense.description}</p>
                      )}
                      {expense.recipient && (
                        <p className="text-sm text-gray-500">Recipient: {expense.recipient}</p>
                      )}
                      {expense.recipientInstitutionName && (
                        <p className="text-sm text-gray-500">
                          Recipient Institution: {expense.recipientInstitutionName}
                        </p>
                      )}
                      <p className="text-xs text-gray-400 mt-2">
                        {new Date(expense.createdAt).toLocaleString()}
                      </p>
                    </div>
                  </div>
                </div>
              ))
            ) : (
              <div className="text-center py-8 text-gray-500">
                <p>No expenses yet.</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ManageExpenses;

