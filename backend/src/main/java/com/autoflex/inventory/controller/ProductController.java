package com.autoflex.inventory.controller;

import com.autoflex.inventory.dto.ProductDTO;
import com.autoflex.inventory.dto.ProductRawMaterialDTO;
import com.autoflex.inventory.service.ProductService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.net.URI;
import java.util.List;

@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Products", description = "Product management operations")
public class ProductController {

    @Inject
    ProductService productService;

    @GET
    @Operation(summary = "Get all products", description = "Retrieves a list of all products")
    @APIResponse(responseCode = "200", description = "List of products retrieved successfully")
    public List<ProductDTO> findAll() {
        return productService.findAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieves a specific product by its ID")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Product found"),
            @APIResponse(responseCode = "404", description = "Product not found")
    })
    public ProductDTO findById(
            @Parameter(description = "Product ID", required = true)
            @PathParam("id") Long id) {
        return productService.findById(id);
    }

    @POST
    @Operation(summary = "Create a new product", description = "Creates a new product with optional raw materials")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Product created successfully"),
            @APIResponse(responseCode = "400", description = "Invalid product data")
    })
    public Response create(@Valid ProductDTO dto) {
        ProductDTO created = productService.create(dto);
        return Response.created(URI.create("/api/products/" + created.getId()))
                .entity(created)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update a product", description = "Updates an existing product")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Product updated successfully"),
            @APIResponse(responseCode = "404", description = "Product not found"),
            @APIResponse(responseCode = "400", description = "Invalid product data")
    })
    public ProductDTO update(
            @Parameter(description = "Product ID", required = true)
            @PathParam("id") Long id,
            @Valid ProductDTO dto) {
        return productService.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a product", description = "Deletes a product and its raw material associations")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Product deleted successfully"),
            @APIResponse(responseCode = "404", description = "Product not found")
    })
    public Response delete(
            @Parameter(description = "Product ID", required = true)
            @PathParam("id") Long id) {
        productService.delete(id);
        return Response.noContent().build();
    }

    // Raw material association endpoints
    @POST
    @Path("/{productId}/raw-materials")
    @Operation(summary = "Add raw material to product", description = "Associates a raw material with a product")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Raw material added successfully"),
            @APIResponse(responseCode = "404", description = "Product or raw material not found"),
            @APIResponse(responseCode = "400", description = "Invalid data or raw material already associated")
    })
    public ProductDTO addRawMaterial(
            @Parameter(description = "Product ID", required = true)
            @PathParam("productId") Long productId,
            @Valid ProductRawMaterialDTO dto) {
        return productService.addRawMaterial(productId, dto);
    }

    @PUT
    @Path("/{productId}/raw-materials/{rawMaterialId}")
    @Operation(summary = "Update raw material quantity", description = "Updates the required quantity of a raw material for a product")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Raw material quantity updated successfully"),
            @APIResponse(responseCode = "404", description = "Product or raw material association not found")
    })
    public ProductDTO updateRawMaterial(
            @Parameter(description = "Product ID", required = true)
            @PathParam("productId") Long productId,
            @Parameter(description = "Raw Material ID", required = true)
            @PathParam("rawMaterialId") Long rawMaterialId,
            @Valid ProductRawMaterialDTO dto) {
        return productService.updateRawMaterial(productId, rawMaterialId, dto);
    }

    @DELETE
    @Path("/{productId}/raw-materials/{rawMaterialId}")
    @Operation(summary = "Remove raw material from product", description = "Removes the association between a raw material and a product")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Raw material removed successfully"),
            @APIResponse(responseCode = "404", description = "Product or raw material association not found")
    })
    public ProductDTO removeRawMaterial(
            @Parameter(description = "Product ID", required = true)
            @PathParam("productId") Long productId,
            @Parameter(description = "Raw Material ID", required = true)
            @PathParam("rawMaterialId") Long rawMaterialId) {
        return productService.removeRawMaterial(productId, rawMaterialId);
    }
}
