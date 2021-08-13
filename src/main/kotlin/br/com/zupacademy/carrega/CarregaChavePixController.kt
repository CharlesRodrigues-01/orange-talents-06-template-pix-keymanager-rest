package br.com.zupacademy.carrega

import br.com.zupacademy.CarregaChavePixRequest
import br.com.zupacademy.KeyManagerCarregaGrpcServiceGrpc
import br.com.zupacademy.KeyManagerListaGrpcServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject

@Controller("/api/v1/clientes/")
class CarregaChavePixController(
    @Inject val carregaChavePixClient: KeyManagerCarregaGrpcServiceGrpc.KeyManagerCarregaGrpcServiceBlockingStub,
    ) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Get("{clientId}/pix/{pixId}")
    fun carrega(@PathVariable("clientId") clientId: UUID,
                @PathVariable("pixId") pixId: UUID
    ): HttpResponse<Any> {

        logger.info("Enviando requisição para carregar chaves Pix")

        val response = carregaChavePixClient.carrega(CarregaChavePixRequest
            .newBuilder()
            .setPixId(CarregaChavePixRequest.FiltroPorPixId
                .newBuilder()
                .setClientId(clientId.toString())
                .setPixId(pixId.toString())
                .build())
            .build())

        return HttpResponse.ok(DetalhesChavePixResponse(response))

    }
}