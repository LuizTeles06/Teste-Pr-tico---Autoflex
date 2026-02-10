import { Link } from 'react-router-dom';

function HomePage() {
  return (
    <div className="px-4 py-6 sm:px-0">
      <div className="text-center mb-12">
        <h1 className="text-4xl font-bold text-gray-900 mb-4">
          Sistema de Controle de Estoque
        </h1>
        <p className="text-lg text-gray-600 max-w-2xl mx-auto">
          Gerencie seus produtos, matérias-primas e obtenha sugestões de produção
          baseadas no estoque disponível.
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 max-w-5xl mx-auto">
        {/* Products Card */}
        <Link
          to="/products"
          className="card hover:shadow-lg transition-shadow duration-200 cursor-pointer"
        >
          <div className="text-center">
            <div className="w-16 h-16 bg-primary-100 rounded-full flex items-center justify-center mx-auto mb-4">
              <svg
                className="w-8 h-8 text-primary-600"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"
                />
              </svg>
            </div>
            <h2 className="text-xl font-semibold text-gray-900 mb-2">Produtos</h2>
            <p className="text-gray-600">
              Gerencie seus produtos e suas necessidades de matérias-primas
            </p>
          </div>
        </Link>

        {/* Raw Materials Card */}
        <Link
          to="/raw-materials"
          className="card hover:shadow-lg transition-shadow duration-200 cursor-pointer"
        >
          <div className="text-center">
            <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
              <svg
                className="w-8 h-8 text-green-600"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"
                />
              </svg>
            </div>
            <h2 className="text-xl font-semibold text-gray-900 mb-2">
              Matérias-Primas
            </h2>
            <p className="text-gray-600">
              Controle o estoque de matérias-primas e níveis de inventário
            </p>
          </div>
        </Link>

        {/* Production Suggestion Card */}
        <Link
          to="/production-suggestion"
          className="card hover:shadow-lg transition-shadow duration-200 cursor-pointer"
        >
          <div className="text-center">
            <div className="w-16 h-16 bg-yellow-100 rounded-full flex items-center justify-center mx-auto mb-4">
              <svg
                className="w-8 h-8 text-yellow-600"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"
                />
              </svg>
            </div>
            <h2 className="text-xl font-semibold text-gray-900 mb-2">
              Sugestão de Produção
            </h2>
            <p className="text-gray-600">
              Veja quais produtos podem ser produzidos com os materiais disponíveis
            </p>
          </div>
        </Link>
      </div>

      <div className="mt-12 text-center">
        <div className="card max-w-2xl mx-auto">
          <h3 className="text-lg font-semibold text-gray-900 mb-3">Como funciona</h3>
          <ol className="text-left text-gray-600 space-y-2">
            <li className="flex items-start">
              <span className="font-semibold text-primary-600 mr-2">1.</span>
              Cadastre matérias-primas com suas quantidades em estoque
            </li>
            <li className="flex items-start">
              <span className="font-semibold text-primary-600 mr-2">2.</span>
              Crie produtos e associe-os às matérias-primas necessárias
            </li>
            <li className="flex items-start">
              <span className="font-semibold text-primary-600 mr-2">3.</span>
              Obtenha sugestões de produção priorizadas pelo valor do produto
            </li>
          </ol>
        </div>
      </div>
    </div>
  );
}

export default HomePage;
