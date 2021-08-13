package br.com.zupacademy.lista

import br.com.zupacademy.KeyManagerListaGrpcServiceGrpc
import br.com.zupacademy.ListaChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject

@Controller("/api/v1/clientes/")
class ListaChavePixController(
    @Inject val listaChavePixClient: KeyManagerListaGrpcServiceGrpc.KeyManagerListaGrpcServiceBlockingStub
    ) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Get("{clientId}/pix")
    fun lista(@PathVariable("clientId") clientId: UUID): HttpResponse<Any> {

        val pix = listaChavePixClient.lista(ListaChavePixRequest
            .newBuilder()
            .setClientId(clientId.toString())
            .build())

        val chaves = pix.chavesList.map {
            ChavePixResponse(it)
        }
        return HttpResponse.ok(chaves)
    }
}