package br.com.zupacademy.registra.controller

import br.com.zupacademy.KeyManagerRegistraGrpcServiceGrpc
import br.com.zupacademy.RegistraChavePixResponse
import br.com.zupacademy.registra.request.NovaChavePixRequest
import br.com.zupacademy.shared.KeyManagerGrpcFactory
import br.com.zupacademy.shared.enums.TipoDeChaveRequest.*
import br.com.zupacademy.shared.enums.TipoDeContaRequest.*
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RegistraChavePixControllerTest{

    @field:Inject
    lateinit var grpcClient: KeyManagerRegistraGrpcServiceGrpc.KeyManagerRegistraGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `deve registrar uma nova chave Pix`() {

        // cenário
        val clientId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val responseGrpc = RegistraChavePixResponse
            .newBuilder()
            .setClientId(clientId)
            .setPixId(pixId)
            .build()

        `when`(grpcClient.registra(Mockito.any())).thenReturn(responseGrpc)

        val novaChavePix = NovaChavePixRequest(
            tipoDeConta = CONTA_CORRENTE,
            chave = "teste@zup.com.br",
            tipoDeChave = EMAIL
        )

        // ação
        val request = HttpRequest.POST("/api/v1/clientes/${clientId}/pix", novaChavePix)
        val response = this.client.toBlocking().exchange(request, Any::class.java)

        // validação
        with(response) {
            assertEquals(HttpStatus.CREATED, this.status)
            assertTrue(response.headers.contains("Location"))
            assertTrue(response.header("Location")!!.contains(pixId))
        }
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory{

        @Singleton
        fun stubMock() = Mockito.mock(KeyManagerRegistraGrpcServiceGrpc.KeyManagerRegistraGrpcServiceBlockingStub::class.java)
    }
}