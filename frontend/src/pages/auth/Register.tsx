import { useForm } from 'react-hook-form';
import { useAuth } from '../../context/AuthContext';
import { useNavigate, Link } from 'react-router-dom';
import { FiUserPlus, FiMail, FiLock, FiUser } from 'react-icons/fi';
import { useEffect } from 'react';

const Register = () => {
  const { register: registerUser } = useAuth();
  const navigate = useNavigate();
  const { register, handleSubmit, formState: { errors } } = useForm<{
    email: string;
    password: string;
    name: string;
    guestId?: string;
  }>();

  useEffect(() => {
    const guestId = localStorage.getItem('guestId');
    if (guestId) {
      // Pre-fill guest ID if available
    }
  }, []);

  const onSubmit = async (data: {
    email: string;
    password: string;
    name: string;
    guestId?: string;
  }) => {
    const guestId = localStorage.getItem('guestId');
    try {
      await registerUser(data.email, data.password, data.name, guestId || undefined);
      localStorage.removeItem('guestId');
      navigate('/dashboard');
    } catch (error) {
      // Error is handled in AuthContext
    }
  };

  return (
    <div className="container mx-auto px-4 py-16 flex items-center justify-center min-h-[60vh]">
      <div className="card w-full max-w-md">
        <div className="card-header text-center">
          <div className="flex justify-center mb-4">
            <FiUserPlus className="text-4xl text-primary" />
          </div>
          <h1 className="card-title">Create Account</h1>
          <p className="card-description">Join Helping Hands and start making a difference.</p>
        </div>
        <div className="card-content">
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div>
              <label className="block text-sm font-medium mb-2 flex items-center space-x-2">
                <FiUser />
                <span>Full Name</span>
              </label>
              <input
                type="text"
                {...register('name', { required: 'Name is required' })}
                className="input w-full"
                placeholder="John Doe"
              />
              {errors.name && (
                <p className="text-red-500 text-sm mt-1">{errors.name.message}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium mb-2 flex items-center space-x-2">
                <FiMail />
                <span>Email</span>
              </label>
              <input
                type="email"
                {...register('email', { required: 'Email is required' })}
                className="input w-full"
                placeholder="your@email.com"
              />
              {errors.email && (
                <p className="text-red-500 text-sm mt-1">{errors.email.message}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium mb-2 flex items-center space-x-2">
                <FiLock />
                <span>Password</span>
              </label>
              <input
                type="password"
                {...register('password', {
                  required: 'Password is required',
                  minLength: { value: 6, message: 'Password must be at least 6 characters' },
                })}
                className="input w-full"
                placeholder="••••••••"
              />
              {errors.password && (
                <p className="text-red-500 text-sm mt-1">{errors.password.message}</p>
              )}
            </div>

            {localStorage.getItem('guestId') && (
              <div className="p-3 bg-blue-50 rounded-lg text-sm text-blue-800">
                <p>Your guest donations will be linked to this account.</p>
              </div>
            )}

            <button type="submit" className="btn btn-primary w-full">
              Register
            </button>
          </form>

          <div className="mt-6 text-center text-sm">
            <p className="text-gray-600">
              Already have an account?{' '}
              <Link to="/login" className="text-primary hover:underline">
                Login here
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Register;

