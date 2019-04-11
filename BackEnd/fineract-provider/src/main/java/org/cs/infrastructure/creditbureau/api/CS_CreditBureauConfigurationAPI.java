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

package org.cs.infrastructure.creditbureau.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import io.swagger.annotations.Api;
import org.cs.commands.domain.CS_CommandWrapper;
import org.cs.commands.service.CS_CommandWrapperBuilder;
import org.cs.commands.service.CS_PortfolioCommandSourceWritePlatformService;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.apache.fineract.infrastructure.creditbureau.data.CreditBureauData;
import org.cs.infrastructure.security.service.CS_PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Path("/CreditBureauConfiguration/cs")
@Component
@Scope("singleton")
@Api(value = "CreditBureau Configuration")
public class CS_CreditBureauConfigurationAPI {
	private final Set<String> RESPONSE_DATA_PARAMETERS = new HashSet<>(
			Arrays.asList("creditBureauId", "alias", "country", "creditBureauProductId", "startDate", "endDate", "isActive"));
	private final String resourceNameForPermissions = "CreditBureau";
	private final CS_PlatformSecurityContext context;
	private final DefaultToApiJsonSerializer<CreditBureauData> toApiJsonSerializer;
	private final CS_PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;

	@Autowired
	public CS_CreditBureauConfigurationAPI(final CS_PlatformSecurityContext context,
			final DefaultToApiJsonSerializer<CreditBureauData> toApiJsonSerializer,
			final CS_PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService) {
		this.context = context;
		this.toApiJsonSerializer = toApiJsonSerializer;
		this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;

	}

	@POST
	@Path("/createCreditBureau")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String createCreditBureau(final String apiRequestBodyAsJson) {

		final CS_CommandWrapper commandRequest = new CS_CommandWrapperBuilder().createCreditBureau()
				.withJson(apiRequestBodyAsJson).build();

		final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);

		return this.toApiJsonSerializer.serialize(result);
	}

}
