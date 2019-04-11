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
package org.cs.commands.service;

import org.cs.commands.domain.CS_CommandSource;
import org.cs.commands.domain.CS_CommandWrapper;
import org.cs.infrastructure.core.api.CS_JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.useradministration.domain.AppUser;

public interface CS_CommandProcessingService {

    CommandProcessingResult processAndLogCommand(CS_CommandWrapper wrapper, CS_JsonCommand command, boolean isApprovedByChecker);

    CommandProcessingResult logCommand(CS_CommandSource commandSourceResult);
    
    boolean validateCommand(final CS_CommandWrapper commandWrapper,final AppUser user);

}