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
 * Error message containing meaningful data about the error.
 */
data class ErrorMessage @JvmOverloads constructor(

    /**
     * Either specific error code in case of invalid request or HTTP status code.
     *
     */
    val status: Int,

    /**
     * Information about the error type.
     */
    val type: String? = null,

    /**
     * Message describing error.
     */
    val message: String? = null,

    /**
     * Detailed information about the error.
     */
    val details: List<String> = emptyList(),

    /**
     * Optional stacktraces, sent when node is in debug mode.
     */
    val stacktraces: List<String> = emptyList()
)
