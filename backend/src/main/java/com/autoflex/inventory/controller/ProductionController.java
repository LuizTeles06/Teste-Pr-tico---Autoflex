package com.autoflex.inventory.controller;

import com.autoflex.inventory.dto.ProductionSuggestionDTO;
import com.autoflex.inventory.service.ProductionSuggestionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/production")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Production", description = "Production suggestion operations")
public class ProductionController {

    @Inject
    ProductionSuggestionService productionSuggestionService;

    @GET
    @Path("/suggestion")
    @Operation(
            summary = "Get production suggestion",
            description = "Calculates which products can be produced with available raw materials, " +
                    "prioritizing products with higher value"
    )
    @APIResponse(responseCode = "200", description = "Production suggestion calculated successfully")
    public ProductionSuggestionDTO getProductionSuggestion() {
        return productionSuggestionService.calculateProductionSuggestion();
    }
}
