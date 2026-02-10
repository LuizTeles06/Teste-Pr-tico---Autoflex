describe('Production Suggestion', () => {
  beforeEach(() => {
    cy.cleanupTestData();
  });

  it('should display empty state when no production is possible', () => {
    cy.visit('/production-suggestion');
    
    cy.contains('No production possible').should('be.visible');
    cy.contains('$0.00').should('be.visible');
  });

  it('should calculate production suggestion based on available stock', () => {
    // Setup: Create raw materials
    cy.createRawMaterial('Steel', 100);
    cy.createRawMaterial('Plastic', 200);
    
    // Get raw material IDs
    cy.request('/api/raw-materials').then((response) => {
      const rawMaterials = response.body;
      const steel = rawMaterials.find((rm: { name: string }) => rm.name === 'Steel');
      const plastic = rawMaterials.find((rm: { name: string }) => rm.name === 'Plastic');
      
      // Create products with raw materials
      // Product A: $150, needs 10 Steel, 20 Plastic (can make 5 with 100 Steel)
      cy.request({
        method: 'POST',
        url: '/api/products',
        body: {
          name: 'Product A',
          value: 150,
          rawMaterials: [
            { rawMaterialId: steel.id, requiredQuantity: 10 },
            { rawMaterialId: plastic.id, requiredQuantity: 20 },
          ],
        },
      });
      
      // Product B: $75, needs 5 Steel (would normally make 20, but Steel is used by A first)
      cy.request({
        method: 'POST',
        url: '/api/products',
        body: {
          name: 'Product B',
          value: 75,
          rawMaterials: [
            { rawMaterialId: steel.id, requiredQuantity: 5 },
          ],
        },
      });
    });
    
    cy.visit('/production-suggestion');
    
    // Product A should be prioritized (higher value)
    cy.contains('Product A').should('be.visible');
    cy.contains('$150.00').should('be.visible');
    
    // Total value should reflect production
    cy.get('table tfoot').should('contain', '$');
  });

  it('should refresh suggestion when clicking refresh button', () => {
    cy.visit('/production-suggestion');
    
    cy.contains('button', 'Refresh').click();
    
    // Page should reload data
    cy.contains('Total Potential Value').should('be.visible');
  });

  it('should show how the suggestion works info card', () => {
    cy.visit('/production-suggestion');
    
    cy.contains('How the suggestion works').should('be.visible');
    cy.contains('Products are sorted by value').should('be.visible');
    cy.contains('highest first').should('be.visible');
  });

  it('should prioritize higher value products', () => {
    // Setup: Create a raw material with limited stock
    cy.createRawMaterial('Limited Resource', 10);
    
    cy.request('/api/raw-materials').then((response) => {
      const rawMaterial = response.body[0];
      
      // Create two products competing for the same resource
      // Expensive product: $200, needs 5 units
      cy.request({
        method: 'POST',
        url: '/api/products',
        body: {
          name: 'Expensive Product',
          value: 200,
          rawMaterials: [
            { rawMaterialId: rawMaterial.id, requiredQuantity: 5 },
          ],
        },
      });
      
      // Cheap product: $50, needs 5 units
      cy.request({
        method: 'POST',
        url: '/api/products',
        body: {
          name: 'Cheap Product',
          value: 50,
          rawMaterials: [
            { rawMaterialId: rawMaterial.id, requiredQuantity: 5 },
          ],
        },
      });
    });
    
    cy.visit('/production-suggestion');
    
    // Should show Expensive Product with 2 units (uses all 10 resources)
    cy.contains('Expensive Product').should('be.visible');
    cy.contains('2 unit(s)').should('be.visible');
    
    // Cheap Product should not appear (no resources left)
    cy.contains('Cheap Product').should('not.exist');
  });
});
