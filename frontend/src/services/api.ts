import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor
api.interceptors.request.use(
  (config) => {
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor
api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response) {
      // Server responded with error status
      const message = error.response.data?.message || 'An error occurred';
      console.error('API Error:', message);
    } else if (error.request) {
      // Request was made but no response
      console.error('Network Error: No response received');
    } else {
      // Error setting up request
      console.error('Error:', error.message);
    }
    return Promise.reject(error);
  }
);

export default api;
