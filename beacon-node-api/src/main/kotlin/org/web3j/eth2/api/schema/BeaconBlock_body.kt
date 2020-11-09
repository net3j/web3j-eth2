/*
 * Copyright 2020 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.eth2.api.schema

/**
 * The [`BeaconBlockBody`](https://github.com/ethereum/eth2.0-specs/blob/v0.12.2/specs/phase0/beacon-chain.md#beaconblockbody) object from the Eth2.0 spec.
 * @param randaoReveal
 * @param eth1Data
 * @param graffiti
 * @param proposerSlashings
 * @param attesterSlashings
 * @param attestations
 * @param deposits
 * @param voluntaryExits
 */
data class BeaconBlockBody(

    val randaoReveal: AllOfBeaconBlockBodyRandaoReveal? = null,
    val eth1Data: BeaconStateEth1Data? = null,
    val graffiti: String? = null,
    val proposerSlashings: Array<Body3>? = null,
    val attesterSlashings: Array<Body2>? = null,
    val attestations: Array<Body1>? = null,
    val deposits: Array<BeaconBlockBodyDeposits>? = null,
    val voluntaryExits: Array<Body4>? = null
)