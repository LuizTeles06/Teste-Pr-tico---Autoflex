export interface Product {
  id?: number;
  name: string;
  value: number;
  rawMaterials?: ProductRawMaterial[];
}

export interface RawMaterial {
  id?: number;
  name: string;
  stockQuantity: number;
}

export interface ProductRawMaterial {
  id?: number;
  rawMaterialId: number;
  rawMaterialName?: string;
  requiredQuantity: number;
}

export interface ProductionItem {
  productId: number;
  productName: string;
  productValue: number;
  quantity: number;
  subtotal: number;
}

export interface ProductionSuggestion {
  items: ProductionItem[];
  totalValue: number;
}

export interface ApiError {
  message: string;
  status?: number;
  errors?: Record<string, string>;
}
