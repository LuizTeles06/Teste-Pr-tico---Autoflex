describe('Navigation', () => {
  it('should navigate to home page', () => {
    cy.visit('/');
    
    cy.contains('Inventory Control System').should('be.visible');
    cy.contains('Manage your products, raw materials').should('be.visible');
  });

  it('should navigate to products page from home', () => {
    cy.visit('/');
    
    cy.contains('Products').first().click();
    
    cy.url().should('include', '/products');
    cy.contains('Add Product').should('be.visible');
  });

  it('should navigate to raw materials page from home', () => {
    cy.visit('/');
    
    cy.contains('Raw Materials').first().click();
    
    cy.url().should('include', '/raw-materials');
    cy.contains('Add Raw Material').should('be.visible');
  });

  it('should navigate to production suggestion page from home', () => {
    cy.visit('/');
    
    cy.contains('Production Suggestion').first().click();
    
    cy.url().should('include', '/production-suggestion');
    cy.contains('Total Potential Value').should('be.visible');
  });

  it('should navigate using navbar links', () => {
    cy.visit('/');
    
    // Navigate to Products
    cy.get('nav').contains('Products').click();
    cy.url().should('include', '/products');
    
    // Navigate to Raw Materials
    cy.get('nav').contains('Raw Materials').click();
    cy.url().should('include', '/raw-materials');
    
    // Navigate to Production Suggestion
    cy.get('nav').contains('Production Suggestion').click();
    cy.url().should('include', '/production-suggestion');
    
    // Navigate back to Home
    cy.get('nav').contains('Home').click();
    cy.url().should('eq', Cypress.config('baseUrl') + '/');
  });

  it('should highlight active navigation item', () => {
    cy.visit('/products');
    
    cy.get('nav').contains('Products').should('have.class', 'bg-primary-700');
    cy.get('nav').contains('Raw Materials').should('not.have.class', 'bg-primary-700');
  });

  it('should be responsive on mobile', () => {
    cy.viewport('iphone-x');
    cy.visit('/');
    
    // Mobile menu should be visible
    cy.get('.md\\:hidden').should('be.visible');
    
    // Navigation links should still work
    cy.get('.md\\:hidden').contains('Products').click();
    cy.url().should('include', '/products');
  });
});
