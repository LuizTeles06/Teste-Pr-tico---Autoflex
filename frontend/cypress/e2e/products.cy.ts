describe('Products CRUD', () => {
  beforeEach(() => {
    cy.cleanupTestData();
    cy.visit('/products');
  });

  it('should display empty state when no products exist', () => {
    cy.contains('No products found').should('be.visible');
  });

  it('should create a new product', () => {
    cy.contains('Add Product').click();
    
    cy.get('#name').type('Widget A');
    cy.get('#value').clear().type('99.99');
    
    cy.contains('button', 'Create').click();
    
    cy.contains('Product created successfully').should('be.visible');
    cy.contains('Widget A').should('be.visible');
    cy.contains('$99.99').should('be.visible');
  });

  it('should edit a product', () => {
    cy.createProduct('Widget B', 50);
    cy.reload();
    
    cy.contains('Widget B').parent().parent().contains('Edit').click();
    
    cy.get('#name').clear().type('Widget B Updated');
    cy.get('#value').clear().type('75.00');
    
    cy.contains('button', 'Update').click();
    
    cy.contains('Product updated successfully').should('be.visible');
    cy.contains('Widget B Updated').should('be.visible');
  });

  it('should delete a product', () => {
    cy.createProduct('Widget C', 30);
    cy.reload();
    
    cy.contains('Widget C').parent().parent().contains('Delete').click();
    
    cy.contains('button', 'Delete').click();
    
    cy.contains('Product deleted successfully').should('be.visible');
    cy.contains('Widget C').should('not.exist');
  });

  it('should add raw material to product', () => {
    // Create raw material first
    cy.createRawMaterial('Steel', 1000);
    cy.createProduct('Gadget X', 150);
    cy.reload();
    
    cy.contains('Gadget X').parent().parent().contains('Edit').click();
    
    // Add raw material
    cy.get('select').select('Steel');
    cy.get('input[type="number"]').last().clear().type('10');
    cy.contains('button', 'Add').click();
    
    cy.contains('Raw material added').should('be.visible');
    cy.contains('Steel - Qty: 10').should('be.visible');
  });

  it('should remove raw material from product', () => {
    // Setup: create raw material, product, and associate them
    cy.createRawMaterial('Aluminum', 500);
    cy.createProduct('Gadget Y', 200);
    
    // Associate raw material with product via API
    cy.request('/api/raw-materials').then((rmResponse) => {
      const rawMaterial = rmResponse.body[0];
      cy.request('/api/products').then((pResponse) => {
        const product = pResponse.body[0];
        cy.request({
          method: 'POST',
          url: `/api/products/${product.id}/raw-materials`,
          body: {
            rawMaterialId: rawMaterial.id,
            requiredQuantity: 5,
          },
        });
      });
    });
    
    cy.reload();
    
    cy.contains('Gadget Y').parent().parent().contains('Edit').click();
    
    cy.contains('Aluminum - Qty: 5').parent().contains('Remove').click();
    
    cy.contains('Raw material removed').should('be.visible');
    cy.contains('Aluminum - Qty: 5').should('not.exist');
  });
});
