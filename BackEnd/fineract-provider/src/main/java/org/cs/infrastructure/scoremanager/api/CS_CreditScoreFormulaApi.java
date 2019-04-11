package org.cs.infrastructure.scoremanager.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.cs.commands.domain.CS_CommandWrapper;
import org.cs.commands.service.CS_CommandWrapperBuilder;
import org.cs.commands.service.CS_PortfolioCommandSourceWritePlatformService;
import org.apache.fineract.infrastructure.core.api.ApiRequestParameterHelper;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.apache.fineract.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.cs.infrastructure.scoremanager.data.CS_CreditScoreFormulaData;
import org.cs.infrastructure.scoremanager.service.CS_CreditScoreFormulaReadPlatformService;
import org.cs.infrastructure.security.service.CS_PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.swagger.annotations.Api;

@Path("/scoreformula")
@Component
@Scope("singleton")
@Api(value = "Credit Score Formula")
public class CS_CreditScoreFormulaApi {
    
    private final Set<String> responseDataParameters = new HashSet<>(Arrays.asList("id", "formulaName", "formula", "status", "createdBy", "createdDate", "updatedBy",
            "updatedDate"));
    private final String resourceNameForPermissions = "CreditScoreFormula";
    private final CS_PlatformSecurityContext context;
    private final ApiRequestParameterHelper apiRequestParameterHelper;
    private final CS_PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
    private final DefaultToApiJsonSerializer<CS_CreditScoreFormulaData> toApiJsonSerializer;
    private final CS_CreditScoreFormulaReadPlatformService service;
    
    @Autowired
    public CS_CreditScoreFormulaApi(final CS_PlatformSecurityContext context, final ApiRequestParameterHelper apiRequestParameterHelper,
            final CS_PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService, 
            final DefaultToApiJsonSerializer<CS_CreditScoreFormulaData> toApiJsonSerializer, CS_CreditScoreFormulaReadPlatformService service) {
        this.context = context;
        this.apiRequestParameterHelper = apiRequestParameterHelper;
        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
        this.toApiJsonSerializer = toApiJsonSerializer;
        this.service = service;
    }
    
    @GET
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String getCreditScoreFormulaList(@Context final UriInfo uriInfo) {
        this.context.authenticatedUser().validateHasReadPermission(this.resourceNameForPermissions);
        final Collection<CS_CreditScoreFormulaData> dataList = this.service.getCreditScoreFormulaDataList();
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getPathParameters());
        return this.toApiJsonSerializer.serialize(settings, dataList, responseDataParameters);
    }
    
    @GET
    @Path("/{id}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String getCreditScoreFormula(@PathParam(value = "id") final long id, @Context final UriInfo uriInfo) {
        this.context.authenticatedUser().validateHasReadPermission(this.resourceNameForPermissions);
        final CS_CreditScoreFormulaData data = this.service.getCreditScoreFormulaDataById(id);
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getPathParameters());
        return this.toApiJsonSerializer.serialize(settings, data, responseDataParameters);
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String createCreditScoreFormula(final String apiRequestBodyAsJson) {
        final CS_CommandWrapper commandRequest = new CS_CommandWrapperBuilder().createCreditScoreFormula().withJson(apiRequestBodyAsJson).build();
        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
        return this.toApiJsonSerializer.serialize(result);        
    }
    
    @PUT
    @Path("/{id}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String updateCreditScoreFormula(@PathParam(value = "id") final long id, final String apiRequestBodyAsJson) {
        final CS_CommandWrapper commandRequest = new CS_CommandWrapperBuilder().updateCreditScoreFormula(id).withJson(apiRequestBodyAsJson).build();
        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
        return this.toApiJsonSerializer.serialize(result);
    }

}