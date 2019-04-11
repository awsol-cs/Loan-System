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
import org.cs.infrastructure.scoremanager.data.CS_ScoreManagerData;
import org.cs.infrastructure.scoremanager.service.CS_CreditScoreConfigReadPlatformService;
import org.cs.infrastructure.security.service.CS_PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.swagger.annotations.Api;


@Path("/scoremanager")
@Component
@Scope("singleton")
@Api(value = "Credit Score Configuration")
public class CS_CreditScoreConfigurationApi {

    private final Set<String> RESPONSE_DATA_PARAMETERS = new HashSet<>(Arrays.asList("id", "ruleName", "weightedValue", "ruleType", "ruleTypeDataList", "status",
            "createdBy", "createdDate", "updatedBy", "updatedDate"));
    private final String resourceNameForPermissions = "CreditScore";
    private final CS_PlatformSecurityContext context;
    private final DefaultToApiJsonSerializer<CS_ScoreManagerData> toApiJsonSerializer;
    private final ApiRequestParameterHelper apiRequestParameterHelper;
    private final CS_CreditScoreConfigReadPlatformService creditScoreReadPlatformService;
    private final CS_PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
    
    @Autowired
    public CS_CreditScoreConfigurationApi(final CS_PlatformSecurityContext context,
            final DefaultToApiJsonSerializer<CS_ScoreManagerData> toApiJsonSerializer,
            final ApiRequestParameterHelper apiRequestParameterHelper,
            final CS_CreditScoreConfigReadPlatformService creditScoreReadPlatformService,
            final CS_PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService) {
        super();
        this.context = context;
        this.toApiJsonSerializer = toApiJsonSerializer;
        this.apiRequestParameterHelper = apiRequestParameterHelper;
        this.creditScoreReadPlatformService = creditScoreReadPlatformService;
        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
    }

    @GET
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String getScoreConfigList(@Context final UriInfo uriInfo) {
        this.context.authenticatedUser().validateHasReadPermission(this.resourceNameForPermissions);
        final Collection<CS_ScoreManagerData> creditScoreDataList = this.creditScoreReadPlatformService.getScoreConfigList();
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getPathParameters());
        return this.toApiJsonSerializer.serialize(settings, creditScoreDataList, this.RESPONSE_DATA_PARAMETERS);
    }

    @GET
    @Path("/{id}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String getCreditScore(@PathParam(value = "id") final int id, @Context final UriInfo uriInfo) {
        this.context.authenticatedUser().validateHasReadPermission(this.resourceNameForPermissions);
        final CS_ScoreManagerData creditScoreData = this.creditScoreReadPlatformService.getCreditScoreById(id);
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getPathParameters());
        return this.toApiJsonSerializer.serialize(settings, creditScoreData, this.RESPONSE_DATA_PARAMETERS);
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String createScoreConfig(final String apiRequestBodyAsJson) {
        final CS_CommandWrapper commandRequest = new CS_CommandWrapperBuilder().createScoreConfig().withJson(apiRequestBodyAsJson).build();
        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
        return this.toApiJsonSerializer.serialize(result);
    }

    @PUT
    @Path("/{id}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String updateScoreConfig(@PathParam(value = "id") final int id, final String apiRequestBodyAsJson) {
        final CS_CommandWrapper commandRequest = new CS_CommandWrapperBuilder().updateScoreConfig(id).withJson(apiRequestBodyAsJson).build();
        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
        return this.toApiJsonSerializer.serialize(result);
    }

    @PUT
    @Path("/{id}/changestatus")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String updateAvailability(@PathParam(value = "id") final int id, final String apiRequestBodyAsJson) {
        final CS_CommandWrapper commandRequest = new CS_CommandWrapperBuilder().updateScoreConfigStatus(id).withJson(apiRequestBodyAsJson).build();
        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
        return this.toApiJsonSerializer.serialize(result);
    }
}
