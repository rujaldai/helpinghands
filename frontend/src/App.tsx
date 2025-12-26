import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';

// Layouts
import MainLayout from './layouts/MainLayout';
import DashboardLayout from './layouts/DashboardLayout';

// Pages
import Home from './pages/Home';
import About from './pages/About';
import Causes from './pages/Causes';
import Institutions from './pages/Institutions';
import Donate from './pages/Donate';
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import Dashboard from './pages/dashboard/Dashboard';
import Profile from './pages/dashboard/Profile';
import Donations from './pages/dashboard/Donations';
import AdminDashboard from './pages/admin/Dashboard';
import ManageUsers from './pages/admin/ManageUsers';
import ManageInstitutions from './pages/admin/ManageInstitutions';
import ManageCauses from './pages/admin/ManageCauses';
import ManageExpenses from './pages/admin/ManageExpenses';
import ManageTransfers from './pages/admin/ManageTransfers';
import Balance from './pages/admin/Balance';
import MoneyTracking from './pages/dashboard/MoneyTracking';
import NotFound from './pages/NotFound';

// Context
import { AuthProvider, useAuth } from './context/AuthContext';

// Initialize React Query Client
const queryClient = new QueryClient();

// Protected Route Component
const ProtectedRoute = ({ children, adminOnly = false }: { children: React.ReactNode, adminOnly?: boolean }) => {
  const { isAuthenticated, user } = useAuth();
  
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  
  if (adminOnly && user?.role !== 'ADMIN') {
    return <Navigate to="/dashboard" replace />;
  }
  
  return <>{children}</>;
};

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Router>
        <AuthProvider>
          <div className="min-h-screen bg-background">
            <Routes>
              {/* Public Routes */}
              <Route path="/" element={<MainLayout />}>
                <Route index element={<Home />} />
                <Route path="about" element={<About />} />
                <Route path="causes" element={<Causes />} />
                <Route path="institutions" element={<Institutions />} />
                <Route path="donate" element={<Donate />} />
                <Route path="login" element={<Login />} />
                <Route path="register" element={<Register />} />
                
                {/* Protected User Routes */}
                <Route 
                  path="dashboard" 
                  element={
                    <ProtectedRoute>
                      <DashboardLayout />
                    </ProtectedRoute>
                  }
                >
                  <Route index element={<Dashboard />} />
                  <Route path="profile" element={<Profile />} />
                  <Route path="donations" element={<Donations />} />
                  <Route path="money-tracking" element={<MoneyTracking />} />
                </Route>
                
                {/* Admin Routes */}
                <Route 
                  path="admin" 
                  element={
                    <ProtectedRoute adminOnly>
                      <DashboardLayout admin />
                    </ProtectedRoute>
                  }
                >
                  <Route index element={<AdminDashboard />} />
                  <Route path="users" element={<ManageUsers />} />
                  <Route path="institutions" element={<ManageInstitutions />} />
                  <Route path="causes" element={<ManageCauses />} />
                  <Route path="expenses" element={<ManageExpenses />} />
                  <Route path="transfers" element={<ManageTransfers />} />
                  <Route path="balance" element={<Balance />} />
                </Route>
                
                {/* 404 Route */}
                <Route path="*" element={<NotFound />} />
              </Route>
            </Routes>
            
            {/* Toast Notifications */}
            <Toaster 
              position="top-center"
              toastOptions={{
                duration: 5000,
                style: {
                  background: 'hsl(var(--background))',
                  color: 'hsl(var(--foreground))',
                  border: '1px solid hsl(var(--border))',
                },
              }}
            />
          </div>
        </AuthProvider>
      </Router>
      
      {/* React Query Devtools - Only in development */}
      {import.meta.env.DEV && <ReactQueryDevtools initialIsOpen={false} />}
    </QueryClientProvider>
  );
}

export default App;
