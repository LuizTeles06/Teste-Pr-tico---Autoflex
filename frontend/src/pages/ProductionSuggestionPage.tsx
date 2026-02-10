import { useEffect } from 'react';
import { useAppDispatch, useAppSelector } from '../store/hooks';
import { fetchProductionSuggestion } from '../store/slices/productionSlice';
import LoadingSpinner from '../components/LoadingSpinner';

function ProductionSuggestionPage() {
  const dispatch = useAppDispatch();
  const { suggestion, loading, error } = useAppSelector((state) => state.production);

  useEffect(() => {
    dispatch(fetchProductionSuggestion());
  }, [dispatch]);

  const handleRefresh = () => {
    dispatch(fetchProductionSuggestion());
  };

  const formatCurrency = (value: number) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    }).format(value);
  };

  if (loading) {
    return <LoadingSpinner />;
  }

  if (error) {
    return (
      <div className="px-4 py-6 sm:px-0">
        <div className="card text-center">
          <p className="text-red-600 mb-4">{error}</p>
          <button onClick={handleRefresh} className="btn btn-primary">
            Tentar Novamente
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="px-4 py-6 sm:px-0">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-gray-900">Sugestão de Produção</h1>
        <button onClick={handleRefresh} className="btn btn-primary">
          Atualizar
        </button>
      </div>

      {/* Summary Card */}
      <div className="card mb-6">
        <div className="flex items-center justify-between">
          <div>
            <h2 className="text-lg font-semibold text-gray-900 mb-1">
              Valor Potencial Total
            </h2>
            <p className="text-sm text-gray-600">
              Baseado no estoque atual de matérias-primas
            </p>
          </div>
          <div className="text-3xl font-bold text-green-600">
            {formatCurrency(suggestion?.totalValue || 0)}
          </div>
        </div>
      </div>

      {/* Production Items Table */}
      <div className="card">
        <h2 className="text-lg font-semibold text-gray-900 mb-4">
          Produção Sugerida
        </h2>
        <p className="text-sm text-gray-600 mb-4">
          Os produtos são priorizados por valor (maior valor primeiro). O sistema calcula
          a quantidade máxima de cada produto que pode ser produzida com as matérias-primas
          disponíveis.
        </p>

        <div className="table-container">
          <table className="table">
            <thead>
              <tr>
                <th>Produto</th>
                <th className="text-right">Valor Unitário</th>
                <th className="text-right">Quantidade</th>
                <th className="text-right">Subtotal</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {!suggestion || suggestion.items.length === 0 ? (
                <tr>
                  <td colSpan={4} className="text-center py-8 text-gray-500">
                    <div className="flex flex-col items-center">
                      <svg
                        className="w-12 h-12 text-gray-400 mb-3"
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                      >
                        <path
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          strokeWidth={2}
                          d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4"
                        />
                      </svg>
                      <p className="font-medium">Nenhuma produção possível</p>
                      <p className="text-sm mt-1">
                        Não há produtos configurados, matérias-primas em estoque,
                        ou os produtos não possuem requisitos de matérias-primas.
                      </p>
                    </div>
                  </td>
                </tr>
              ) : (
                suggestion.items.map((item) => (
                  <tr key={item.productId}>
                    <td className="font-medium">{item.productName}</td>
                    <td className="text-right">{formatCurrency(item.productValue)}</td>
                    <td className="text-right">
                      <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
                        {item.quantity} unidade(s)
                      </span>
                    </td>
                    <td className="text-right font-semibold text-green-600">
                      {formatCurrency(item.subtotal)}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
            {suggestion && suggestion.items.length > 0 && (
              <tfoot>
                <tr className="bg-gray-50">
                  <td colSpan={3} className="px-6 py-4 text-right font-bold text-gray-900">
                    Valor Total:
                  </td>
                  <td className="px-6 py-4 text-right font-bold text-green-600 text-lg">
                    {formatCurrency(suggestion.totalValue)}
                  </td>
                </tr>
              </tfoot>
            )}
          </table>
        </div>
      </div>

      {/* Info Card */}
      <div className="card mt-6 bg-blue-50 border border-blue-200">
        <div className="flex">
          <div className="flex-shrink-0">
            <svg
              className="h-5 w-5 text-blue-400"
              fill="currentColor"
              viewBox="0 0 20 20"
            >
              <path
                fillRule="evenodd"
                d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z"
                clipRule="evenodd"
              />
            </svg>
          </div>
          <div className="ml-3">
            <h3 className="text-sm font-medium text-blue-800">
              Como a sugestão funciona
            </h3>
            <div className="mt-2 text-sm text-blue-700">
              <ul className="list-disc list-inside space-y-1">
                <li>
                  Os produtos são ordenados por valor (maior primeiro) para maximizar a receita
                </li>
                <li>
                  Para cada produto, o sistema calcula quantas unidades podem ser produzidas
                  com base nas matérias-primas disponíveis
                </li>
                <li>
                  As matérias-primas são "reservadas" conforme os produtos são alocados,
                  garantindo cálculos precisos
                </li>
                <li>
                  O processo se repete até que não seja mais possível produzir
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ProductionSuggestionPage;
