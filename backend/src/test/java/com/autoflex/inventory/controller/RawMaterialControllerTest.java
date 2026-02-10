package com.autoflex.inventory.controller;

import com.autoflex.inventory.dto.RawMaterialDTO;
import com.autoflex.inventory.service.RawMaterialService;
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
class RawMaterialControllerTest {

    @InjectMock
    RawMaterialService rawMaterialService;

    @Test
    void testGetAllRawMaterials() {
        RawMaterialDTO rm1 = new RawMaterialDTO(1L, "Steel", new BigDecimal("100.0000"));
        RawMaterialDTO rm2 = new RawMaterialDTO(2L, "Aluminum", new BigDecimal("50.0000"));
        when(rawMaterialService.findAll()).thenReturn(Arrays.asList(rm1, rm2));

        given()
            .when().get("/api/raw-materials")
            .then()
                .statusCode(200)
                .body("size()", is(2))
                .body("[0].name", equalTo("Steel"))
                .body("[1].name", equalTo("Aluminum"));
    }

    @Test
    void testGetAllRawMaterials_Empty() {
        when(rawMaterialService.findAll()).thenReturn(Collections.emptyList());

        given()
            .when().get("/api/raw-materials")
            .then()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void testGetRawMaterialById() {
        RawMaterialDTO rm = new RawMaterialDTO(1L, "Steel", new BigDecimal("100.0000"));
        when(rawMaterialService.findById(1L)).thenReturn(rm);

        given()
            .when().get("/api/raw-materials/1")
            .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", equalTo("Steel"));
    }

    @Test
    void testCreateRawMaterial() {
        RawMaterialDTO inputDto = new RawMaterialDTO(null, "Plastic", new BigDecimal("200.0000"));
        RawMaterialDTO createdDto = new RawMaterialDTO(1L, "Plastic", new BigDecimal("200.0000"));
        when(rawMaterialService.create(any(RawMaterialDTO.class))).thenReturn(createdDto);

        given()
            .contentType(ContentType.JSON)
            .body(inputDto)
            .when().post("/api/raw-materials")
            .then()
                .statusCode(201)
                .body("id", equalTo(1))
                .body("name", equalTo("Plastic"));
    }

    @Test
    void testCreateRawMaterial_ValidationError() {
        RawMaterialDTO invalidDto = new RawMaterialDTO(null, "", new BigDecimal("-10.0000"));

        given()
            .contentType(ContentType.JSON)
            .body(invalidDto)
            .when().post("/api/raw-materials")
            .then()
                .statusCode(400);
    }

    @Test
    void testUpdateRawMaterial() {
        RawMaterialDTO updatedDto = new RawMaterialDTO(1L, "Updated Steel", new BigDecimal("150.0000"));
        when(rawMaterialService.update(any(Long.class), any(RawMaterialDTO.class))).thenReturn(updatedDto);

        given()
            .contentType(ContentType.JSON)
            .body(updatedDto)
            .when().put("/api/raw-materials/1")
            .then()
                .statusCode(200)
                .body("name", equalTo("Updated Steel"));
    }

    @Test
    void testDeleteRawMaterial() {
        given()
            .when().delete("/api/raw-materials/1")
            .then()
                .statusCode(204);
    }

    @Test
    void testSearchRawMaterials() {
        RawMaterialDTO rm = new RawMaterialDTO(1L, "Steel", new BigDecimal("100.0000"));
        when(rawMaterialService.search("steel")).thenReturn(Arrays.asList(rm));

        given()
            .queryParam("name", "steel")
            .when().get("/api/raw-materials/search")
            .then()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].name", equalTo("Steel"));
    }
}
