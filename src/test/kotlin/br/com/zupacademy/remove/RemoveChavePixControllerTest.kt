package br.com.zupacademy.remove

import br.com.zupacademy.KeyManagerRegistraGrpcServiceGrpc
import br.com.zupacademy.KeyManagerRemoveGrpcServiceGrpc
import br.com.zupacademy.RemoveChavePixResponse
import br.com.zupacademy.shared.KeyManagerGrpcFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RemoveChavePixControllerTest{

    @field:Inject
    lateinit var grpcClient: KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `deve remover uma nova chave Pix existente`() {

        // cenário
        val clientId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val responseGrpc = RemoveChavePixResponse
            .newBuilder()
            .setClientId(clientId)
            .setPixId(pixId)
            .build()

        `when`(grpcClient.remove(any())).thenReturn(responseGrpc)

        // ação
        val request = HttpRequest.DELETE<Any>("/api/v1/clientes/${clientId}/pix/${pixId}")
        val response = this.client.toBlocking().exchange(request, Any::class.java)

        // validação
        with(response) {
            Assertions.assertEquals(HttpStatus.OK, this.status)
        }
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory{

        @Singleton
        fun stubMock() = mock(KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub::class.java)
    }
}