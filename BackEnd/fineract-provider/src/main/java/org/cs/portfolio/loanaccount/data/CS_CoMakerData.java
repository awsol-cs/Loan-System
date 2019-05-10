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
package org.cs.portfolio.loanaccount.domain;

import org.cs.portfolio.client.domain.CS_KycInfoData;

@SuppressWarnings("unused")
final public class CS_CoMakerData implements Comparable<CS_CoMakerData> {

    private CS_KycInfoData kyc;
    private CS_CoMakerInfoData info;

    public static CS_CoMakerData instance(CS_KycInfoData kycInfo, CS_CoMakerInfoData coMakerInfo){
        return new CS_CoMakerData(kycInfo, coMakerInfo);
    }

    private CS_CoMakerData(CS_KycInfoData kycInfo, CS_CoMakerInfoData coMakerInfo){
        this.kyc = kycInfo;
        this.info = coMakerInfo;                                              
    }

    @Override
    public int compareTo(final CS_CoMakerData obj) {
        return 0;
    }

    @Override
    public boolean equals(final Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

}