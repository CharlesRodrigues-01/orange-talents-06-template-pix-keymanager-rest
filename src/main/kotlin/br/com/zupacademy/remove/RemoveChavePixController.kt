package br.com.zupacademy.remove

import br.com.zupacademy.KeyManagerRemoveGrpcServiceGrpc
import br.com.zupacademy.RemoveChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject

@Validated
@Controller("/api/v1/clientes/")
class RemoveChavePixController(
    @Inject val removeChavePixClient: KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Delete("{clientId}/pix/{pixId}")
    fun remove(@PathVariable("clientId") clientId: UUID,
               @PathVariable("pixId") pixId: UUID): HttpResponse<Any> {

        logger.info("Enviando requisição para remover chave Pix")

        removeChavePixClient.remove(RemoveChavePixRequest
            .newBuilder()
            .setClientId(clientId.toString())
            .setPixId(pixId.toString())
            .build())

        return HttpResponse.ok()
    }
}