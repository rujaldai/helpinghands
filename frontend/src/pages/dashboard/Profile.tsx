import { useAuth } from '../../context/AuthContext';
import { FiUser, FiMail, FiCalendar } from 'react-icons/fi';

const Profile = () => {
  const { user } = useAuth();

  if (!user) {
    return <div>Loading...</div>;
  }

  return (
    <div className="max-w-2xl">
      <div className="card">
        <div className="card-header">
          <h2 className="card-title">Profile Information</h2>
        </div>
        <div className="card-content space-y-6">
          <div className="flex items-center space-x-4">
            <div className="w-20 h-20 rounded-full bg-primary flex items-center justify-center text-white text-2xl font-bold">
              {user.name.charAt(0).toUpperCase()}
            </div>
            <div>
              <h3 className="text-2xl font-bold">{user.name}</h3>
              <p className="text-gray-600 capitalize">{user.role.toLowerCase()}</p>
            </div>
          </div>

          <div className="space-y-4">
            <div className="flex items-center space-x-3">
              <FiMail className="text-gray-400" />
              <div>
                <p className="text-sm text-gray-600">Email</p>
                <p className="font-medium">{user.email}</p>
              </div>
            </div>

            <div className="flex items-center space-x-3">
              <FiUser className="text-gray-400" />
              <div>
                <p className="text-sm text-gray-600">Role</p>
                <p className="font-medium capitalize">{user.role.toLowerCase()}</p>
              </div>
            </div>

            {user.guestId && (
              <div className="flex items-center space-x-3">
                <FiUser className="text-gray-400" />
                <div>
                  <p className="text-sm text-gray-600">Guest ID</p>
                  <p className="font-medium font-mono">{user.guestId}</p>
                </div>
              </div>
            )}

            <div className="flex items-center space-x-3">
              <FiCalendar className="text-gray-400" />
              <div>
                <p className="text-sm text-gray-600">Member Since</p>
                <p className="font-medium">
                  {new Date(user.createdAt).toLocaleDateString()}
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;

