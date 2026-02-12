import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { useAppDispatch, useAppSelector } from '../store/hooks';
import {
  fetchProducts,
  createProduct,
  updateProduct,
  deleteProduct,
  fetchProductById,
  addRawMaterialToProduct,
  removeRawMaterialFromProduct,
  clearSelectedProduct,
} from '../store/slices/productSlice';
import { fetchRawMaterials } from '../store/slices/rawMaterialSlice';
import { Product, ProductRawMaterial } from '../types';
import Modal from '../components/Modal';
import ConfirmDialog from '../components/ConfirmDialog';
import LoadingSpinner from '../components/LoadingSpinner';

function ProductsPage() {
  const dispatch = useAppDispatch();
  const { items: products, loading, selectedProduct } = useAppSelector(
    (state) => state.products
  );
  const { items: rawMaterials } = useAppSelector((state) => state.rawMaterials);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);
  const [productToDelete, setProductToDelete] = useState<Product | null>(null);

  // Form state
  const [formData, setFormData] = useState<Product>({
    name: '',
    value: 0,
    rawMaterials: [],
  });

  // Raw material form
  const [selectedRawMaterialId, setSelectedRawMaterialId] = useState<number | ''>('');
  const [requiredQuantity, setRequiredQuantity] = useState<number>(0);

  useEffect(() => {
    dispatch(fetchProducts());
    dispatch(fetchRawMaterials());
  }, [dispatch]);

  const openCreateModal = () => {
    setEditingProduct(null);
    setFormData({ name: '', value: 0, rawMaterials: [] });
    setIsModalOpen(true);
  };

  const openEditModal = async (product: Product) => {
    await dispatch(fetchProductById(product.id!));
    setEditingProduct(product);
    setFormData({
      name: product.name,
      value: product.value,
      rawMaterials: product.rawMaterials || [],
    });
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    const wasEditing = editingProduct !== null;
    setIsModalOpen(false);
    setEditingProduct(null);
    dispatch(clearSelectedProduct());
    setFormData({ name: '', value: 0, rawMaterials: [] });
    if (wasEditing) {
      dispatch(fetchProducts());
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      if (editingProduct) {
        // When editing, don't send rawMaterials - they are managed separately via API
        const updateData = { name: formData.name, value: formData.value };
        await dispatch(updateProduct({ id: editingProduct.id!, product: updateData })).unwrap();
        toast.success('Produto atualizado com sucesso');
      } else {
        await dispatch(createProduct(formData)).unwrap();
        toast.success('Produto criado com sucesso');
      }
      handleCloseModal();
      dispatch(fetchProducts());
    } catch (error) {
      toast.error(error as string);
    }
  };

  const handleDelete = async () => {
    if (!productToDelete) return;

    try {
      await dispatch(deleteProduct(productToDelete.id!)).unwrap();
      toast.success('Produto excluído com sucesso');
      setIsDeleteDialogOpen(false);
      setProductToDelete(null);
    } catch (error) {
      toast.error(error as string);
    }
  };

  const handleAddRawMaterial = async () => {
    if (!selectedRawMaterialId || requiredQuantity <= 0) {
      toast.error('Selecione uma matéria-prima e insira uma quantidade válida');
      return;
    }

    if (editingProduct?.id) {
      // If editing existing product, add via API
      try {
        await dispatch(
          addRawMaterialToProduct({
            productId: editingProduct.id,
            rawMaterial: {
              rawMaterialId: Number(selectedRawMaterialId),
              requiredQuantity,
            },
          })
        ).unwrap();
        toast.success('Matéria-prima adicionada');
        setSelectedRawMaterialId('');
        setRequiredQuantity(0);
        dispatch(fetchProductById(editingProduct.id));
      } catch (error) {
        toast.error(error as string);
      }
    } else {
      // If creating new product, add to form state
      const rawMaterial = rawMaterials.find((rm) => rm.id === Number(selectedRawMaterialId));
      if (!rawMaterial) return;

      const newRawMaterial: ProductRawMaterial = {
        rawMaterialId: Number(selectedRawMaterialId),
        rawMaterialName: rawMaterial.name,
        requiredQuantity,
      };

      setFormData({
        ...formData,
        rawMaterials: [...(formData.rawMaterials || []), newRawMaterial],
      });
      setSelectedRawMaterialId('');
      setRequiredQuantity(0);
    }
  };

  const handleRemoveRawMaterial = async (rawMaterialId: number) => {
    if (editingProduct?.id) {
      try {
        await dispatch(
          removeRawMaterialFromProduct({
            productId: editingProduct.id,
            rawMaterialId,
          })
        ).unwrap();
        toast.success('Matéria-prima removida');
        dispatch(fetchProductById(editingProduct.id));
      } catch (error) {
        toast.error(error as string);
      }
    } else {
      setFormData({
        ...formData,
        rawMaterials: formData.rawMaterials?.filter(
          (rm) => rm.rawMaterialId !== rawMaterialId
        ),
      });
    }
  };

  const formatCurrency = (value: number) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    }).format(value);
  };

  const currentRawMaterials = editingProduct
    ? selectedProduct?.rawMaterials || []
    : formData.rawMaterials || [];

  const availableRawMaterials = rawMaterials.filter(
    (rm) => !currentRawMaterials.some((crm) => crm.rawMaterialId === rm.id)
  );

  if (loading && products.length === 0) {
    return <LoadingSpinner />;
  }

  return (
    <div className="px-4 py-6 sm:px-0">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-gray-900">Produtos</h1>
        <button onClick={openCreateModal} className="btn btn-primary">
          Adicionar Produto
        </button>
      </div>

      {/* Products Table */}
      <div className="card">
        <div className="table-container">
          <table className="table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Valor</th>
                <th>Matérias-Primas</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {products.length === 0 ? (
                <tr>
                  <td colSpan={5} className="text-center py-8 text-gray-500">
                    Nenhum produto encontrado. Clique em "Adicionar Produto" para criar.
                  </td>
                </tr>
              ) : (
                products.map((product) => (
                  <tr key={product.id}>
                    <td>{product.id}</td>
                    <td className="font-medium">{product.name}</td>
                    <td>{formatCurrency(product.value)}</td>
                    <td>
                      {product.rawMaterials && product.rawMaterials.length > 0 ? (
                        <span className="text-sm text-gray-600">
                          {product.rawMaterials.length} material(is)
                        </span>
                      ) : (
                        <span className="text-sm text-gray-400">Nenhum</span>
                      )}
                    </td>
                    <td>
                      <div className="flex space-x-2">
                        <button
                          onClick={() => openEditModal(product)}
                          className="text-primary-600 hover:text-primary-800"
                        >
                          Editar
                        </button>
                        <button
                          onClick={() => {
                            setProductToDelete(product);
                            setIsDeleteDialogOpen(true);
                          }}
                          className="text-red-600 hover:text-red-800"
                        >
                          Excluir
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Create/Edit Modal */}
      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title={editingProduct ? 'Editar Produto' : 'Criar Produto'}
      >
        <form onSubmit={handleSubmit}>
          <div className="space-y-4">
            <div>
              <label htmlFor="name" className="label">
                Nome
              </label>
              <input
                type="text"
                id="name"
                className="input"
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                required
              />
            </div>

            <div>
              <label htmlFor="value" className="label">
                Valor
              </label>
              <input
                type="number"
                id="value"
                className="input"
                step="0.01"
                min="0.01"
                value={formData.value}
                onChange={(e) =>
                  setFormData({ ...formData, value: parseFloat(e.target.value) || 0 })
                }
                required
              />
            </div>

            {/* Raw Materials Section */}
            <div className="border-t pt-4">
              <h3 className="text-lg font-medium text-gray-900 mb-3">
                Matérias-Primas
              </h3>

              {/* Current raw materials */}
              {currentRawMaterials.length > 0 && (
                <div className="mb-4">
                  <ul className="divide-y divide-gray-200">
                    {currentRawMaterials.map((rm) => (
                      <li
                        key={rm.rawMaterialId}
                        className="py-2 flex justify-between items-center"
                      >
                        <span>
                          {rm.rawMaterialName} - Qtd: {rm.requiredQuantity}
                        </span>
                        <button
                          type="button"
                          onClick={() => handleRemoveRawMaterial(rm.rawMaterialId)}
                          className="text-red-600 hover:text-red-800 text-sm"
                        >
                          Remover
                        </button>
                      </li>
                    ))}
                  </ul>
                </div>
              )}

              {/* Add raw material form */}
              {availableRawMaterials.length > 0 && (
                <div className="flex gap-2 items-end">
                  <div className="flex-1">
                    <label className="label">Matéria-Prima</label>
                    <select
                      className="input"
                      value={selectedRawMaterialId}
                      onChange={(e) =>
                        setSelectedRawMaterialId(
                          e.target.value ? Number(e.target.value) : ''
                        )
                      }
                    >
                      <option value="">Selecione...</option>
                      {availableRawMaterials.map((rm) => (
                        <option key={rm.id} value={rm.id}>
                          {rm.name}
                        </option>
                      ))}
                    </select>
                  </div>
                  <div className="w-32">
                    <label className="label">Quantidade</label>
                    <input
                      type="number"
                      className="input"
                      step="1"
                      value={requiredQuantity}
                      onChange={(e) =>
                        setRequiredQuantity(parseFloat(e.target.value) || 0)
                      }
                    />
                  </div>
                  <button
                    type="button"
                    onClick={handleAddRawMaterial}
                    className="btn btn-secondary"
                  >
                    Adicionar
                  </button>
                </div>
              )}

              {availableRawMaterials.length === 0 && rawMaterials.length === 0 && (
                <p className="text-sm text-gray-500">
                  Nenhuma matéria-prima disponível. Crie algumas primeiro.
                </p>
              )}
            </div>
          </div>

          <div className="mt-6 flex justify-end space-x-3">
            <button type="button" onClick={handleCloseModal} className="btn btn-secondary">
              Cancelar
            </button>
            <button type="submit" className="btn btn-primary" disabled={loading}>
              {loading ? 'Salvando...' : editingProduct ? 'Atualizar' : 'Criar'}
            </button>
          </div>
        </form>
      </Modal>

      {/* Delete Confirmation Dialog */}
      <ConfirmDialog
        isOpen={isDeleteDialogOpen}
        onClose={() => setIsDeleteDialogOpen(false)}
        onConfirm={handleDelete}
        title="Excluir Produto"
        message={`Tem certeza que deseja excluir "${productToDelete?.name}"? Esta ação não pode ser desfeita.`}
      />
    </div>
  );
}

export default ProductsPage;
