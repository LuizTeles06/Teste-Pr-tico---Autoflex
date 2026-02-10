// ***********************************************
// Custom commands for Cypress
// ***********************************************

declare global {
  namespace Cypress {
    interface Chainable {
      /**
       * Create a raw material via API
       */
      createRawMaterial(name: string, stockQuantity: number): Chainable<void>;
      
      /**
       * Create a product via API
       */
      createProduct(name: string, value: number): Chainable<void>;
      
      /**
       * Clean up all test data
       */
      cleanupTestData(): Chainable<void>;
    }
  }
}

Cypress.Commands.add('createRawMaterial', (name: string, stockQuantity: number) => {
  cy.request({
    method: 'POST',
    url: '/api/raw-materials',
    body: {
      name,
      stockQuantity,
    },
  });
});

Cypress.Commands.add('createProduct', (name: string, value: number) => {
  cy.request({
    method: 'POST',
    url: '/api/products',
    body: {
      name,
      value,
    },
  });
});

Cypress.Commands.add('cleanupTestData', () => {
  // Get and delete all products
  cy.request('/api/products').then((response) => {
    const products = response.body;
    products.forEach((product: { id: number }) => {
      cy.request({
        method: 'DELETE',
        url: `/api/products/${product.id}`,
        failOnStatusCode: false,
      });
    });
  });

  // Get and delete all raw materials
  cy.request('/api/raw-materials').then((response) => {
    const rawMaterials = response.body;
    rawMaterials.forEach((rm: { id: number }) => {
      cy.request({
        method: 'DELETE',
        url: `/api/raw-materials/${rm.id}`,
        failOnStatusCode: false,
      });
    });
  });
});

export {};
