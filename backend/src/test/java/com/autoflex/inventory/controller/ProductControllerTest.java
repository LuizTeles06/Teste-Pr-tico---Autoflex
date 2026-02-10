package com.autoflex.inventory.controller;

import com.autoflex.inventory.dto.ProductDTO;
import com.autoflex.inventory.service.ProductService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class ProductControllerTest {

    @InjectMock
    ProductService productService;

    @Test
    void testGetAllProducts() {
        ProductDTO product1 = new ProductDTO(1L, "Product 1", new BigDecimal("100.00"));
        ProductDTO product2 = new ProductDTO(2L, "Product 2", new BigDecimal("200.00"));
        when(productService.findAll()).thenReturn(Arrays.asList(product1, product2));

        given()
            .when().get("/api/products")
            .then()
                .statusCode(200)
                .body("size()", is(2))
                .body("[0].name", equalTo("Product 1"))
                .body("[1].name", equalTo("Product 2"));
    }

    @Test
    void testGetAllProducts_Empty() {
        when(productService.findAll()).thenReturn(Collections.emptyList());

        given()
            .when().get("/api/products")
            .then()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void testGetProductById() {
        ProductDTO product = new ProductDTO(1L, "Product 1", new BigDecimal("100.00"));
        when(productService.findById(1L)).thenReturn(product);

        given()
            .when().get("/api/products/1")
            .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", equalTo("Product 1"))
                .body("value", equalTo(100.00f));
    }

    @Test
    void testCreateProduct() {
        ProductDTO inputDto = new ProductDTO(null, "New Product", new BigDecimal("150.00"));
        ProductDTO createdDto = new ProductDTO(1L, "New Product", new BigDecimal("150.00"));
        when(productService.create(any(ProductDTO.class))).thenReturn(createdDto);

        given()
            .contentType(ContentType.JSON)
            .body(inputDto)
            .when().post("/api/products")
            .then()
                .statusCode(201)
                .body("id", equalTo(1))
                .body("name", equalTo("New Product"));
    }

    @Test
    void testCreateProduct_ValidationError() {
        ProductDTO invalidDto = new ProductDTO(null, "", new BigDecimal("-10.00"));

        given()
            .contentType(ContentType.JSON)
            .body(invalidDto)
            .when().post("/api/products")
            .then()
                .statusCode(400);
    }

    @Test
    void testUpdateProduct() {
        ProductDTO updatedDto = new ProductDTO(1L, "Updated Product", new BigDecimal("200.00"));
        when(productService.update(any(Long.class), any(ProductDTO.class))).thenReturn(updatedDto);

        given()
            .contentType(ContentType.JSON)
            .body(updatedDto)
            .when().put("/api/products/1")
            .then()
                .statusCode(200)
                .body("name", equalTo("Updated Product"))
                .body("value", equalTo(200.00f));
    }

    @Test
    void testDeleteProduct() {
        given()
            .when().delete("/api/products/1")
            .then()
                .statusCode(204);
    }
}
