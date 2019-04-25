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
package org.cs.message.api;

import com.google.gson.JsonElement;
import io.swagger.annotations.*;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.cs.infrastructure.security.service.CS_PlatformSecurityContext;

import org.nexmo.quickstart.sms.SendMessage;


@Path("/message")
@Component
@Scope("singleton")
@Api(value = "Loans", description = "The API for Loan Actions like Approve and Rejection of Loans")
public class CS_SendMessageApiResource {
    
    private final CS_PlatformSecurityContext context;

    @Autowired
    public CS_SendMessageApiResource(final CS_PlatformSecurityContext context) {
        this.context = context;
    }

    @POST
    @Path("/approved")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public void sendApprovalMessage(@Context final UriInfo uriInfo) {
        SendMessage sendMessage = new SendMessage();
        try{
            sendMessage.sendApprovalMessage();
        }catch(Exception e){
            System.out.println(e.getStackTrace());
        }
    }

    @POST
    @Path("/rejected")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public void sendRejectionMessage(@Context final UriInfo uriInfo) {
        SendMessage sendMessage = new SendMessage();
        try{
            sendMessage.sendRejectionMessage();
        }catch(Exception e){
            System.out.println(e.getStackTrace());
        }
    }
}