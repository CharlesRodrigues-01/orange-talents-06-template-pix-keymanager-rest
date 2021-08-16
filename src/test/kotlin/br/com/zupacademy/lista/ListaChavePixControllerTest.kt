package br.com.zupacademy.lista

import br.com.zupacademy.KeyManagerListaGrpcServiceGrpc
import br.com.zupacademy.ListaChavePixResponse
import br.com.zupacademy.TipoDeChave
import br.com.zupacademy.TipoDeConta
import br.com.zupacademy.shared.KeyManagerGrpcFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ListaChavePixControllerTest{

    @field:Inject
    lateinit var grpcClient: KeyManagerListaGrpcServiceGrpc.KeyManagerListaGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    companion object {
        val tipoDeConta = TipoDeConta.CONTA_CORRENTE
        val tipo_email = TipoDeChave.EMAIL
        val tipo_celular = TipoDeChave.CELULAR
        val email = "teste@zup.com.br"
        val celular = "+551912345678"
        val criadaEm = LocalDateTime.now()
    }

    @Test
    fun `deve listar todas as chaves Pix existentes`(){

        // cenário
        val clientId = UUID.randomUUID().toString()
        val responseGrpc = listaChavePixResponse(clientId)

        `when`(grpcClient.lista(Mockito.any())).thenReturn(responseGrpc)

        // ação
        val request = HttpRequest.GET<Any>("/api/v1/clientes/${clientId}/pix/")
        val response = this.client.toBlocking().exchange(request, List::class.java)

        // validação
        with(response){
            assertEquals(HttpStatus.OK, response.status)
            assertNotNull(response.body())
            assertEquals(2, response.body()!!.size )
        }

    }

    private fun listaChavePixResponse(clientId: String): ListaChavePixResponse {

        val chaveEmail = ListaChavePixResponse.ChavePix
            .newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setTipo(tipo_email)
            .setChave(email)
            .setTipoDeConta(tipoDeConta)
            .setCriadaEm(criadaEm.let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            })
            .build()

        val chaveCelular = ListaChavePixResponse.ChavePix
            .newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setTipo(tipo_celular)
            .setChave(celular)
            .setTipoDeConta(tipoDeConta)
            .setCriadaEm(criadaEm.let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            })
            .build()

        return ListaChavePixResponse
            .newBuilder()
            .setClientId(clientId)
            .addAllChaves(listOf(chaveEmail, chaveCelular))
            .build()

    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory{

        @Singleton
        fun stubMock() =
            Mockito.mock(KeyManagerListaGrpcServiceGrpc.KeyManagerListaGrpcServiceBlockingStub::class.java)
    }
}