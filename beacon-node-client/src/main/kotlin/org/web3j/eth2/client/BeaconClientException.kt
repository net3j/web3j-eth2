/*
 * Copyright 2019 Web3 Labs Ltd.
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
package org.web3j.eth2.client

import org.web3j.eth2.api.schema.ErrorMessage
import javax.ws.rs.ClientErrorException
import javax.ws.rs.core.MediaType

/**
 * Client API exception containing error data.
 */
class BeaconClientException internal constructor(
    val errorMessage: ErrorMessage?
) : RuntimeException(errorMessage?.message) {
    companion object {

        @JvmStatic
        fun of(exception: ClientErrorException): BeaconClientException {
            return with(exception.response) {
                if (hasEntity() && mediaType == MediaType.APPLICATION_JSON_TYPE) {
                    BeaconClientException(readEntity(ErrorMessage::class.java))
                } else {
                    BeaconClientException(
                        ErrorMessage(
                            code = exception.response.status,
                            message = exception.response.statusInfo.reasonPhrase,
                            stacktraces = emptyList()
                        )
                    )
                }
            }
        }
    }
}
