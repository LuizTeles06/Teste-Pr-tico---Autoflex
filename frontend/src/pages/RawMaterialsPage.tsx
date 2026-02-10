import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { useAppDispatch, useAppSelector } from '../store/hooks';
import {
  fetchRawMaterials,
  createRawMaterial,
  updateRawMaterial,
  deleteRawMaterial,
} from '../store/slices/rawMaterialSlice';
import { RawMaterial } from '../types';
import Modal from '../components/Modal';
import ConfirmDialog from '../components/ConfirmDialog';
import LoadingSpinner from '../components/LoadingSpinner';

function RawMaterialsPage() {
  const dispatch = useAppDispatch();
  const { items: rawMaterials, loading } = useAppSelector(
    (state) => state.rawMaterials
  );

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [editingRawMaterial, setEditingRawMaterial] = useState<RawMaterial | null>(
    null
  );
  const [rawMaterialToDelete, setRawMaterialToDelete] =
    useState<RawMaterial | null>(null);

  // Form state
  const [formData, setFormData] = useState<RawMaterial>({
    name: '',
    stockQuantity: 0,
  });

  useEffect(() => {
    dispatch(fetchRawMaterials());
  }, [dispatch]);

  const openCreateModal = () => {
    setEditingRawMaterial(null);
    setFormData({ name: '', stockQuantity: 0 });
    setIsModalOpen(true);
  };

  const openEditModal = (rawMaterial: RawMaterial) => {
    setEditingRawMaterial(rawMaterial);
    setFormData({
      name: rawMaterial.name,
      stockQuantity: rawMaterial.stockQuantity,
    });
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingRawMaterial(null);
    setFormData({ name: '', stockQuantity: 0 });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      if (editingRawMaterial) {
        await dispatch(
          updateRawMaterial({ id: editingRawMaterial.id!, rawMaterial: formData })
        ).unwrap();
        toast.success('Matéria-prima atualizada com sucesso');
      } else {
        await dispatch(createRawMaterial(formData)).unwrap();
        toast.success('Matéria-prima criada com sucesso');
      }
      handleCloseModal();
    } catch (error) {
      toast.error(error as string);
    }
  };

  const handleDelete = async () => {
    if (!rawMaterialToDelete) return;

    try {
      await dispatch(deleteRawMaterial(rawMaterialToDelete.id!)).unwrap();
      toast.success('Matéria-prima excluída com sucesso');
      setIsDeleteDialogOpen(false);
      setRawMaterialToDelete(null);
    } catch (error) {
      toast.error(error as string);
    }
  };

  const formatQuantity = (value: number) => {
    return new Intl.NumberFormat('pt-BR', {
      minimumFractionDigits: 0,
      maximumFractionDigits: 4,
    }).format(value);
  };

  if (loading && rawMaterials.length === 0) {
    return <LoadingSpinner />;
  }

  return (
    <div className="px-4 py-6 sm:px-0">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-gray-900">Matérias-Primas</h1>
        <button onClick={openCreateModal} className="btn btn-primary">
          Adicionar Matéria-Prima
        </button>
      </div>

      {/* Raw Materials Table */}
      <div className="card">
        <div className="table-container">
          <table className="table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Quantidade em Estoque</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {rawMaterials.length === 0 ? (
                <tr>
                  <td colSpan={4} className="text-center py-8 text-gray-500">
                    Nenhuma matéria-prima encontrada. Clique em "Adicionar Matéria-Prima" para criar.
                  </td>
                </tr>
              ) : (
                rawMaterials.map((rawMaterial) => (
                  <tr key={rawMaterial.id}>
                    <td>{rawMaterial.id}</td>
                    <td className="font-medium">{rawMaterial.name}</td>
                    <td>
                      <span
                        className={`${
                          rawMaterial.stockQuantity === 0
                            ? 'text-red-600 font-semibold'
                            : rawMaterial.stockQuantity < 10
                            ? 'text-yellow-600'
                            : 'text-green-600'
                        }`}
                      >
                        {formatQuantity(rawMaterial.stockQuantity)}
                      </span>
                    </td>
                    <td>
                      <div className="flex space-x-2">
                        <button
                          onClick={() => openEditModal(rawMaterial)}
                          className="text-primary-600 hover:text-primary-800"
                        >
                          Editar
                        </button>
                        <button
                          onClick={() => {
                            setRawMaterialToDelete(rawMaterial);
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
        title={editingRawMaterial ? 'Editar Matéria-Prima' : 'Criar Matéria-Prima'}
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
              <label htmlFor="stockQuantity" className="label">
                Quantidade em Estoque
              </label>
              <input
                type="number"
                id="stockQuantity"
                className="input"
                step="0.0001"
                min="0"
                value={formData.stockQuantity}
                onChange={(e) =>
                  setFormData({
                    ...formData,
                    stockQuantity: parseFloat(e.target.value) || 0,
                  })
                }
                required
              />
            </div>
          </div>

          <div className="mt-6 flex justify-end space-x-3">
            <button type="button" onClick={handleCloseModal} className="btn btn-secondary">
              Cancelar
            </button>
            <button type="submit" className="btn btn-primary" disabled={loading}>
              {loading ? 'Salvando...' : editingRawMaterial ? 'Atualizar' : 'Criar'}
            </button>
          </div>
        </form>
      </Modal>

      {/* Delete Confirmation Dialog */}
      <ConfirmDialog
        isOpen={isDeleteDialogOpen}
        onClose={() => setIsDeleteDialogOpen(false)}
        onConfirm={handleDelete}
        title="Excluir Matéria-Prima"
        message={`Tem certeza que deseja excluir "${rawMaterialToDelete?.name}"? Esta ação não pode ser desfeita.`}
      />
    </div>
  );
}

export default RawMaterialsPage;
