package br.com.zupacademy.carrega

import br.com.zupacademy.CarregaChavePixResponse
import br.com.zupacademy.KeyManagerCarregaGrpcServiceGrpc
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
import org.mockito.Mockito.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class CarregaChavePixControllerTest{

    @field:Inject
    lateinit var grpcClient: KeyManagerCarregaGrpcServiceGrpc.KeyManagerCarregaGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    companion object {
        val tipoDeConta = TipoDeConta.CONTA_CORRENTE
        val tipo_email = TipoDeChave.EMAIL
        val email = "teste@zup.com.br"
        val instituicao = "ITAÚ UNIBANCO S.A."
        val agencia = "0001"
        val numeroConta = "291900"
        val nomeTitular = "Alguém a ser testado"
        val cpf = "00000000000"
        val criadaEm = LocalDateTime.now()
    }

    @Test
    fun `deve carregar uma chave Pix existente`() {

        // cenário
        val clientId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        `when`(grpcClient.carrega(any())).thenReturn(carregaChavePixResponse(clientId, pixId))

        // ação
        val request = HttpRequest.GET<Any>("/api/v1/clientes/${clientId}/pix/${pixId}")
        val response = this.client.toBlocking().exchange(request, Any::class.java)

        // validação
        with(response) {
            assertEquals(HttpStatus.OK, this.status)
            assertNotNull(this.body())
        }
    }

    private fun carregaChavePixResponse(clientId: String, pixId: String): CarregaChavePixResponse {
        return CarregaChavePixResponse
            .newBuilder()
            .setClientId(clientId)
            .setPixId(pixId)
            .setChave(CarregaChavePixResponse.ChavePix
                .newBuilder()
                .setTipo(tipo_email)
                .setChave(email)
                .setConta(CarregaChavePixResponse.ChavePix.ContaInfo
                    .newBuilder()
                    .setTipo(tipoDeConta)
                    .setInstituicao(instituicao)
                    .setNomeDoTitular(nomeTitular)
                    .setCpfDoTitular(cpf)
                    .setAgencia(agencia)
                    .setNumeroDaConta(numeroConta)
                    .build())
                .setCriadaEm(criadaEm.let {
                    val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                    Timestamp.newBuilder()
                        .setSeconds(createdAt.epochSecond)
                        .setNanos(createdAt.nano)
                        .build()
                })
                .build())
            .build()
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory{

        @Singleton
        fun stubMock() =
            mock(KeyManagerCarregaGrpcServiceGrpc.KeyManagerCarregaGrpcServiceBlockingStub::class.java)
    }
}