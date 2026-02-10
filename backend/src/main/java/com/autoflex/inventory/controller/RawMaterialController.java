package com.autoflex.inventory.controller;

import com.autoflex.inventory.dto.RawMaterialDTO;
import com.autoflex.inventory.service.RawMaterialService;
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

@Path("/api/raw-materials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Raw Materials", description = "Raw material management operations")
public class RawMaterialController {

    @Inject
    RawMaterialService rawMaterialService;

    @GET
    @Operation(summary = "Get all raw materials", description = "Retrieves a list of all raw materials")
    @APIResponse(responseCode = "200", description = "List of raw materials retrieved successfully")
    public List<RawMaterialDTO> findAll() {
        return rawMaterialService.findAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get raw material by ID", description = "Retrieves a specific raw material by its ID")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Raw material found"),
            @APIResponse(responseCode = "404", description = "Raw material not found")
    })
    public RawMaterialDTO findById(
            @Parameter(description = "Raw Material ID", required = true)
            @PathParam("id") Long id) {
        return rawMaterialService.findById(id);
    }

    @GET
    @Path("/search")
    @Operation(summary = "Search raw materials", description = "Search raw materials by name")
    @APIResponse(responseCode = "200", description = "Search results retrieved successfully")
    public List<RawMaterialDTO> search(
            @Parameter(description = "Search term")
            @QueryParam("name") String name) {
        return rawMaterialService.search(name);
    }

    @POST
    @Operation(summary = "Create a new raw material", description = "Creates a new raw material")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Raw material created successfully"),
            @APIResponse(responseCode = "400", description = "Invalid raw material data")
    })
    public Response create(@Valid RawMaterialDTO dto) {
        RawMaterialDTO created = rawMaterialService.create(dto);
        return Response.created(URI.create("/api/raw-materials/" + created.getId()))
                .entity(created)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update a raw material", description = "Updates an existing raw material")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Raw material updated successfully"),
            @APIResponse(responseCode = "404", description = "Raw material not found"),
            @APIResponse(responseCode = "400", description = "Invalid raw material data")
    })
    public RawMaterialDTO update(
            @Parameter(description = "Raw Material ID", required = true)
            @PathParam("id") Long id,
            @Valid RawMaterialDTO dto) {
        return rawMaterialService.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a raw material", description = "Deletes a raw material if not associated with any product")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Raw material deleted successfully"),
            @APIResponse(responseCode = "404", description = "Raw material not found"),
            @APIResponse(responseCode = "400", description = "Cannot delete raw material associated with products")
    })
    public Response delete(
            @Parameter(description = "Raw Material ID", required = true)
            @PathParam("id") Long id) {
        rawMaterialService.delete(id);
        return Response.noContent().build();
    }
}
