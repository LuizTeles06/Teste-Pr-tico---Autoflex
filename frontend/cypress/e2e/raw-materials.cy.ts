describe('Raw Materials CRUD', () => {
  beforeEach(() => {
    cy.cleanupTestData();
    cy.visit('/raw-materials');
  });

  it('should display empty state when no raw materials exist', () => {
    cy.contains('No raw materials found').should('be.visible');
  });

  it('should create a new raw material', () => {
    cy.contains('Add Raw Material').click();
    
    cy.get('#name').type('Steel');
    cy.get('#stockQuantity').clear().type('1000');
    
    cy.contains('button', 'Create').click();
    
    cy.contains('Raw material created successfully').should('be.visible');
    cy.contains('Steel').should('be.visible');
    cy.contains('1,000').should('be.visible');
  });

  it('should edit a raw material', () => {
    // Create a raw material first
    cy.createRawMaterial('Aluminum', 500);
    cy.reload();
    
    cy.contains('Aluminum').parent().parent().contains('Edit').click();
    
    cy.get('#name').clear().type('Aluminum Alloy');
    cy.get('#stockQuantity').clear().type('750');
    
    cy.contains('button', 'Update').click();
    
    cy.contains('Raw material updated successfully').should('be.visible');
    cy.contains('Aluminum Alloy').should('be.visible');
  });

  it('should delete a raw material', () => {
    // Create a raw material first
    cy.createRawMaterial('Plastic', 2000);
    cy.reload();
    
    cy.contains('Plastic').parent().parent().contains('Delete').click();
    
    cy.contains('button', 'Delete').click();
    
    cy.contains('Raw material deleted successfully').should('be.visible');
    cy.contains('Plastic').should('not.exist');
  });

  it('should show validation error for empty name', () => {
    cy.contains('Add Raw Material').click();
    
    cy.get('#stockQuantity').clear().type('100');
    
    cy.contains('button', 'Create').click();
    
    // HTML5 validation should prevent submission
    cy.get('#name:invalid').should('exist');
  });

  it('should show different colors based on stock quantity', () => {
    cy.createRawMaterial('High Stock', 100);
    cy.createRawMaterial('Low Stock', 5);
    cy.createRawMaterial('No Stock', 0);
    cy.reload();
    
    // High stock should be green
    cy.contains('High Stock').parent().parent().find('.text-green-600').should('exist');
    
    // Low stock should be yellow
    cy.contains('Low Stock').parent().parent().find('.text-yellow-600').should('exist');
    
    // No stock should be red
    cy.contains('No Stock').parent().parent().find('.text-red-600').should('exist');
  });
});
