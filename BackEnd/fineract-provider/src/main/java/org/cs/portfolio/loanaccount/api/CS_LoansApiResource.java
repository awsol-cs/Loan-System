/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.cs.portfolio.loanaccount.api;

import io.swagger.annotations.*;

import java.util.*;

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
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.portfolio.loanaccount.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.cs.portfolio.loanaccount.service.CS_CoMakerReadPlatformService;
import org.apache.fineract.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.cs.portfolio.loanaccount.domain.CS_CoMakerData;
import org.apache.fineract.infrastructure.core.api.ApiRequestParameterHelper;


@Path("/cs_loans")
@Component
@Scope("singleton")
@Api(value = "Loans", description = "The API concept of loans models the loan application process and the loan contract/monitoring process.")
public class CS_LoansApiResource {

    private final Set<String> RESPONSE_DATA_PARAMETERS = new HashSet<>(Arrays.asList("info", "kyc"));

    private final PlatformSecurityContext context;
    private final DefaultToApiJsonSerializer<LoanAccountData> toApiJsonSerializer;
    private final DefaultToApiJsonSerializer<CS_CoMakerData> coMakertoApiJsonSerializer;
    private final CS_PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
    private final CS_CoMakerReadPlatformService coMakerReadPlatformService;
    private final ApiRequestParameterHelper apiRequestParameterHelper;


    @Autowired
    public CS_LoansApiResource(final PlatformSecurityContext context,
            final DefaultToApiJsonSerializer<LoanAccountData> toApiJsonSerializer,
            final DefaultToApiJsonSerializer<CS_CoMakerData> coMakertoApiJsonSerializer,
            final CS_PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,
            final CS_CoMakerReadPlatformService coMakerReadPlatformService,
            final ApiRequestParameterHelper apiRequestParameterHelper) {
        this.context = context;
        this.toApiJsonSerializer = toApiJsonSerializer;
        this.coMakertoApiJsonSerializer = coMakertoApiJsonSerializer;
        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
        this.coMakerReadPlatformService = coMakerReadPlatformService;
        this.apiRequestParameterHelper = apiRequestParameterHelper;
    }
    
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String SubmitLoanApplication(@Context final UriInfo uriInfo, @ApiParam(hidden = true) final String apiRequestBodyAsJson) {

        final CS_CommandWrapper commandRequest = new CS_CommandWrapperBuilder().cs_CreateLoan().withJson(apiRequestBodyAsJson).build();

        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);

        return this.toApiJsonSerializer.serialize(result);
    }
    
    @GET
    @Path("comaker/{loanId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String GetCoMaker(
        @PathParam("loanId") final Long loanId, 
        @Context final UriInfo uriInfo, @ApiParam(hidden = true) final String apiRequestBodyAsJson) {

        CS_CoMakerData comaker = this.coMakerReadPlatformService.retrieveOne(loanId);

        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getPathParameters());
        return this.coMakertoApiJsonSerializer.serialize(settings, comaker, this.RESPONSE_DATA_PARAMETERS);
    }
    
    @PUT
    @Path("comaker/{comakerId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String UpdateCoMaker(
        @PathParam("comakerId") final Long comakerId, 
        @Context final UriInfo uriInfo, @ApiParam(hidden = true) final String apiRequestBodyAsJson) {

        final CS_CommandWrapper commandRequest = new CS_CommandWrapperBuilder().cs_UpdateLoan(comakerId).withJson(apiRequestBodyAsJson).build();

        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);

        return this.toApiJsonSerializer.serialize(result);
    }
}