import { Routes, Route, Link, useLocation } from 'react-router-dom'
import ProductsPage from './pages/ProductsPage'
import RawMaterialsPage from './pages/RawMaterialsPage'
import ProductionSuggestionPage from './pages/ProductionSuggestionPage'
import HomePage from './pages/HomePage'

function App() {
  const location = useLocation()

  const isActive = (path: string) => {
    return location.pathname === path
      ? 'bg-primary-700 text-white'
      : 'text-gray-300 hover:bg-primary-600 hover:text-white'
  }

  return (
    <div className="min-h-screen bg-gray-100">
      {/* Navigation */}
      <nav className="bg-primary-800 shadow-lg">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            <div className="flex items-center">
              <Link to="/" className="text-white font-bold text-xl">
                Controle de Estoque
              </Link>
              <div className="hidden md:block ml-10">
                <div className="flex items-baseline space-x-4">
                  <Link
                    to="/"
                    className={`px-3 py-2 rounded-md text-sm font-medium ${isActive('/')}`}
                  >
                    Início
                  </Link>
                  <Link
                    to="/products"
                    className={`px-3 py-2 rounded-md text-sm font-medium ${isActive('/products')}`}
                  >
                    Produtos
                  </Link>
                  <Link
                    to="/raw-materials"
                    className={`px-3 py-2 rounded-md text-sm font-medium ${isActive('/raw-materials')}`}
                  >
                    Matérias-Primas
                  </Link>
                  <Link
                    to="/production-suggestion"
                    className={`px-3 py-2 rounded-md text-sm font-medium ${isActive('/production-suggestion')}`}
                  >
                    Sugestão de Produção
                  </Link>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Mobile menu */}
        <div className="md:hidden">
          <div className="px-2 pt-2 pb-3 space-y-1 sm:px-3">
            <Link
              to="/"
              className={`block px-3 py-2 rounded-md text-base font-medium ${isActive('/')}`}
            >
              Início
            </Link>
            <Link
              to="/products"
              className={`block px-3 py-2 rounded-md text-base font-medium ${isActive('/products')}`}
            >
              Produtos
            </Link>
            <Link
              to="/raw-materials"
              className={`block px-3 py-2 rounded-md text-base font-medium ${isActive('/raw-materials')}`}
            >
              Matérias-Primas
            </Link>
            <Link
              to="/production-suggestion"
              className={`block px-3 py-2 rounded-md text-base font-medium ${isActive('/production-suggestion')}`}
            >
              Sugestão de Produção
            </Link>
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/products" element={<ProductsPage />} />
          <Route path="/raw-materials" element={<RawMaterialsPage />} />
          <Route path="/production-suggestion" element={<ProductionSuggestionPage />} />
        </Routes>
      </main>
    </div>
  )
}

export default App
