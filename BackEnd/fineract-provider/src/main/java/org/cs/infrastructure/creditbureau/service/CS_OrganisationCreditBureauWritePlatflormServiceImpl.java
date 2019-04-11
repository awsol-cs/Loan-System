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
package org.cs.infrastructure.creditbureau.service;

import org.cs.infrastructure.core.api.CS_JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResultBuilder;
import org.cs.infrastructure.creditbureau.domain.CS_CreditBureau;
import org.cs.infrastructure.creditbureau.domain.CS_CreditBureauRepository;
import org.cs.infrastructure.creditbureau.serialization.CS_CreditBureauCommandFromApiJsonDeserializer;
import org.cs.infrastructure.security.service.CS_PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CS_OrganisationCreditBureauWritePlatflormServiceImpl
		implements CS_OrganisationCreditBureauWritePlatflormService {

	private final CS_PlatformSecurityContext context;

	private final CS_CreditBureauRepository creditBureauRepository;

	private final CS_CreditBureauCommandFromApiJsonDeserializer fromApiJsonDeserializer;

	@Autowired
	public CS_OrganisationCreditBureauWritePlatflormServiceImpl(final CS_PlatformSecurityContext context, final CS_CreditBureauRepository creditBureauRepository,
			final CS_CreditBureauCommandFromApiJsonDeserializer fromApiJsonDeserializer) {
		this.context = context;
		this.creditBureauRepository = creditBureauRepository;
		this.fromApiJsonDeserializer = fromApiJsonDeserializer;

	}

	@Transactional
	@Override
	public CommandProcessingResult createCreditBureau(CS_JsonCommand command) {
		this.context.authenticatedUser();
		this.fromApiJsonDeserializer.validateForCreate(command.json());

		final CS_CreditBureau creditBureau = CS_CreditBureau.fromJson(command);

		this.creditBureauRepository.save(creditBureau);

		return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(creditBureau.getId())
				.build();

	}

}
