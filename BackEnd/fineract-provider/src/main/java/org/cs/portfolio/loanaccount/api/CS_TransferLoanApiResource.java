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

import com.google.gson.JsonElement;
import io.swagger.annotations.*;

import static org.apache.fineract.portfolio.loanproduct.service.LoanEnumerations.interestType;

import java.io.InputStream;
import java.util.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.cs.commands.domain.CS_CommandWrapper;
import org.cs.commands.service.CS_CommandWrapperBuilder;
import org.cs.commands.service.CS_PortfolioCommandSourceWritePlatformService;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.cs.infrastructure.security.service.CS_PlatformSecurityContext;
import org.apache.fineract.portfolio.loanaccount.data.LoanAccountData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;


@Path("/transferloan")
@Component
@Scope("singleton")
@Api(value = "Loans", description = "The API for Transferring loan accounts")
public class CS_TransferLoanApiResource {

    private final CS_PlatformSecurityContext context;
    private final DefaultToApiJsonSerializer<LoanAccountData> toApiJsonSerializer;
    private final CS_PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;

    @Autowired
    public CS_TransferLoanApiResource(final CS_PlatformSecurityContext context,
            final DefaultToApiJsonSerializer<LoanAccountData> toApiJsonSerializer,
            final CS_PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService) {
        this.context = context;
        this.toApiJsonSerializer = toApiJsonSerializer;
        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
    }

    
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String transferLoan(@ApiParam(hidden = true) final String apiRequestBodyAsJson) {

        final CS_CommandWrapperBuilder builder = new CS_CommandWrapperBuilder().withJson(apiRequestBodyAsJson);

        final CS_CommandWrapper commandRequest =  builder.transferLoan().build();
        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);

        return this.toApiJsonSerializer.serialize(result);
    }
}