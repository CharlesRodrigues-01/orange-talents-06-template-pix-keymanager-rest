package br.com.zupacademy.registra.controller

import br.com.zupacademy.KeyManagerRegistraGrpcServiceGrpc
import br.com.zupacademy.registra.request.NovaChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller("/api/v1/clientes/")
class RegistraChavePixController(
    @Inject val registraChavePixClient: KeyManagerRegistraGrpcServiceGrpc.KeyManagerRegistraGrpcServiceBlockingStub
) {

    @Post("{clientId}/pix")
    fun criate(@PathVariable("clientId") clientId: UUID,
               @Body @Valid request: NovaChavePixRequest): HttpResponse<Any> {

        val grpcResponse = registraChavePixClient.registra(request.toModelGrpc(clientId))

        return HttpResponse.created(location(clientId, grpcResponse.pixId))
    }

    private fun location(clientId: UUID, pixId: String) = HttpResponse
                        .uri("/api/v1/clientes/$clientId/pix/${pixId}")

}
