package org.web3j.eth2.api.client

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.web3j.eth2.api.schema.Attestation
import org.web3j.eth2.api.schema.AttestationData
import org.web3j.eth2.api.schema.AttesterSlashing
import org.web3j.eth2.api.schema.BeaconBlock
import org.web3j.eth2.api.schema.BeaconBlockBody
import org.web3j.eth2.api.schema.BeaconBlockHeader
import org.web3j.eth2.api.schema.Checkpoint
import org.web3j.eth2.api.schema.Eth1Data
import org.web3j.eth2.api.schema.IndexedAttestation
import org.web3j.eth2.api.schema.NamedBlockId
import org.web3j.eth2.api.schema.NamedStateId
import org.web3j.eth2.api.schema.ProposerSlashing
import org.web3j.eth2.api.schema.SignedBeaconBlock
import org.web3j.eth2.api.schema.SignedBeaconBlockHeader
import org.web3j.eth2.api.schema.SignedVoluntaryExit
import org.web3j.eth2.api.schema.ValidatorStatus
import org.web3j.eth2.api.schema.VoluntaryExit
import java.util.EnumSet

@DisplayName("/eth/v1/beacon")
class BeaconResourceTest: BeaconNodeApiTest() {

    @Test
    @DisplayName("GET /genesis")
    fun `get genesis`() {
        assertThat(client.beacon.genesis.data.time).isNotEmpty()
    }

    @Nested
    @DisplayName("/states/{state_id}")
    inner class StatesTest {

        private val stateResource = client.beacon.states.withId(NamedStateId.HEAD)

        @Test
        @DisplayName("GET /root")
        fun `get state root`() {
            assertThat(stateResource.root.data.root).isNotEmpty()
        }

        @Test
        @DisplayName("GET /fork")
        fun `get state fork`() {
            assertThat(stateResource.fork.data.currentVersion).isNotEmpty()
        }

        @Test
        @DisplayName("GET /finality_checkpoints")
        fun `get state finality checkpoints`() {
            assertThat(stateResource.finalityCheckpoints.data.finalized.root).isNotEmpty()
        }

        @Nested
        @DisplayName("/validators")
        inner class ValidatorsTest {

            @Test
            @DisplayName("GET /")
            @Disabled("Too long")
            fun `find all state validators`() {
                assertThat(stateResource.validators.findAll().data).isNotEmpty()
            }

            @Test
            @DisplayName("GET /{validator_id}")
            fun `find state validator by ID`() {
                val validator = stateResource.validators.findById("0").data
                assertThat(validator.index).isEqualTo("0")
            }

            @Test
            @DisplayName("GET /?status=pending_initialized")
            fun `find state validators by status`() {
                val statuses = EnumSet.of(ValidatorStatus.PENDING_INITIALIZED)
                val validators = stateResource.validators.findByStatus(statuses).data
                assertThat(validators.first().status).isEqualTo(ValidatorStatus.PENDING_INITIALIZED)
            }

            @Test
            @DisplayName("GET /?id=0")
            fun `find state validators by IDs`() {
                val validators = stateResource.validators.findByIds(listOf("0")).data
                assertThat(validators.first().index).isEqualTo("0")
            }
        }
    }

    @Nested
    @DisplayName("/headers")
    inner class HeadersTest {

        @Test
        @DisplayName("GET /")
        fun `find all headers`() {
            assertThat(client.beacon.headers.findAll().data).isNotEmpty()
        }

        @Test
        @DisplayName("GET /?slot=0")
        fun `find headers by slot`() {
            assertThat(client.beacon.headers.findBySlot("0").data).isNotEmpty()
        }

        @Test
        @DisplayName("GET /{block_id}")
        fun `find headers by block ID`() {
            assertThat(client.beacon.headers.findByBlockId(NamedBlockId.HEAD).data.root).isNotEmpty()
        }
    }

    @Nested
    @DisplayName("/blocks")
    inner class BlocksTest {

        private val headBlock = client.beacon.blocks.withId(NamedBlockId.HEAD)

        @Nested
        @DisplayName("/{block_id}")
        inner class BlockTest {

            @Test
            @DisplayName("GET /")
            fun `get block by ID`() {
                val block = client.beacon.blocks.findById(NamedBlockId.HEAD).data
                assertThat(block.signature).isNotEmpty()
            }

            @Test
            @DisplayName("GET /attestations")
            fun `find all attestations`() {
                assertThat(headBlock.attestations.findAll().data).isNotEmpty()
            }

            @Test
            @DisplayName("GET /root")
            fun `find block root`() {
                assertThat(headBlock.root.data.root).isNotEmpty()
            }
        }

        @Test
        @DisplayName("POST /")
        fun `publish block`() {
            client.beacon.blocks.publish(
                SignedBeaconBlock(
                    message = BeaconBlock(
                        slot = "0",
                        proposerIndex = "0",
                        parentRoot = "0",
                        stateRoot = "0",
                        body = BeaconBlockBody(
                            randaoReveal = "0x0",
                            eth1Data = Eth1Data(
                                depositRoot = "0",
                                depositCount = "0",
                                blockHash = "0x0"
                            ),
                            graffiti = "0",
                            proposerSlashings = emptyList(),
                            attesterSlashings = emptyList(),
                            attestations = emptyList(),
                            deposits = emptyList(),
                            voluntaryExits = emptyList()
                        )
                    ),
                    signature = "0x0"
                )
            )
        }
    }

    @Nested
    @DisplayName("/pool")
    inner class PoolTest {

        @Nested
        @DisplayName("/attestations")
        inner class AttestationsTest {

            @Test
            @DisplayName("GET /")
            fun `find all attestations`() {
                assertThat(client.beacon.pool.attestations.findAll().data).isEmpty()
            }

            @Test
            @DisplayName("GET /?slot=0")
            fun `find attestations by slot`() {
                assertThat(client.beacon.pool.attestations.findBySlot("0").data).isEmpty()
            }

            @Test
            @DisplayName("GET /?committee_index=0")
            fun `find attestations by committee index`() {
                assertThat(client.beacon.pool.attestations.findByCommitteeIndex("0").data).isEmpty()
            }

            @Test
            @DisplayName("POST /")
            fun `submit attestation`() {
                client.beacon.pool.attestations.submit(
                    Attestation(
                        aggregationBits = "0x1",
                        signature = "0x0",
                        data = AttestationData(
                            slot = "0",
                            index = "0",
                            beaconBlockRoot = "0",
                            source = Checkpoint(
                                epoch = "0",
                                root = "0"
                            ),
                            target = Checkpoint(
                                epoch = "0",
                                root = "0"
                            )
                        )
                    )
                )
            }
        }

        @Nested
        @DisplayName("/attester_slashings")
        inner class AttesterSlashingsTest {

            @Test
            @DisplayName("GET /")
            fun `find all attester slashings`() {
                assertThat(client.beacon.pool.attesterSlashings.findAll().data).isEmpty()
            }

            @Test
            @DisplayName("POST /")
            fun `submit attester slashing`() {
                client.beacon.pool.attesterSlashings.submit(
                    AttesterSlashing(
                        attestation1 = IndexedAttestation(
                            attestingIndices = emptyList(),
                            `data` = AttestationData(
                                slot = "0",
                                index = "0",
                                beaconBlockRoot = "0",
                                source = Checkpoint(
                                    epoch = "0",
                                    root = "0"
                                ),
                                target = Checkpoint(
                                    epoch = "0",
                                    root = "0"
                                )
                            ),
                            signature = "0x0"
                        ),
                        attestation2 = IndexedAttestation(
                            attestingIndices = emptyList(),
                            `data` = AttestationData(
                                slot = "0",
                                index = "0",
                                beaconBlockRoot = "0",
                                source = Checkpoint(
                                    epoch = "0",
                                    root = "0"
                                ),
                                target = Checkpoint(
                                    epoch = "0",
                                    root = "0"
                                )
                            ),
                            signature = "0x0"
                        )
                    )
                )
            }
        }

        @Nested
        @DisplayName("/proposer_slashings")
        inner class ProposerSlashingsTest {

            @Test
            @DisplayName("GET /")
            fun `find all proposer slashings`() {
                assertThat(client.beacon.pool.proposerSlashings.findAll().data).isEmpty()
            }

            @Test
            @DisplayName("POST /")
            fun `submit proposer slashing`() {
                client.beacon.pool.proposerSlashings.submit(
                    ProposerSlashing(
                        signedHeader1 = SignedBeaconBlockHeader(
                            message = BeaconBlockHeader(
                                slot = "0",
                                proposerIndex = "0",
                                parentRoot = "0x0",
                                stateRoot = "0x0",
                                bodyRoot = "0x0"
                            ), signature = "0x0"
                        ),
                        signedHeader2 = SignedBeaconBlockHeader(
                            message = BeaconBlockHeader(
                                slot = "0",
                                proposerIndex = "0",
                                parentRoot = "0x0",
                                stateRoot = "0x0",
                                bodyRoot = "0x0"
                            ), signature = "0x0"
                        )
                    )
                )
            }
        }

        @Nested
        @DisplayName("/voluntary_exits")
        inner class VoluntaryExitsTest {

            @Test
            @DisplayName("GET /")
            fun `find all voluntary exits`() {
                assertThat(client.beacon.pool.voluntaryExits.findAll().data).isEmpty()
            }

            @Test
            @DisplayName("POST /")
            fun `submit voluntary exit`() {
                client.beacon.pool.voluntaryExits.submit(
                    SignedVoluntaryExit(
                        message = VoluntaryExit(
                            epoch = "0",
                            validatorIndex = "0"
                        ),
                        signature = "0x0"
                    )
                )
            }
        }
    }
}