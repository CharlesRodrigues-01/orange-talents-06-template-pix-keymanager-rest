package br.com.zupacademy.registra.request

import br.com.zupacademy.RegistraChavePixRequest
import br.com.zupacademy.TipoDeChave
import br.com.zupacademy.TipoDeConta
import br.com.zupacademy.shared.enums.TipoDeChaveRequest
import br.com.zupacademy.shared.enums.TipoDeContaRequest
import br.com.zupacademy.shared.validation.ValidPixKey
import io.micronaut.core.annotation.Introspected
import org.slf4j.LoggerFactory
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidPixKey
@Introspected
class NovaChavePixRequest(

    @field:NotNull(message ="Dados inválidos")
    val tipoDeConta: TipoDeContaRequest?,
    @field:Size(max = 77)
    val chave: String?,
    @field:NotNull(message ="Dados inválidos")
    val tipoDeChave: TipoDeChaveRequest?) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun toModelGrpc(clientId: UUID): RegistraChavePixRequest{

        logger.info("Convertendo NovaChavePixRequest para RegistraChavePixRequest")

        return RegistraChavePixRequest
            .newBuilder()
            .setClientId(clientId.toString())
            .setTipoDeConta(tipoDeConta?.atributoGrpc ?: TipoDeConta.UNKNOWN_CONTA)
            .setTipoDeContaChave(tipoDeChave?.atributoGrpc ?: TipoDeChave.UNKNOWN_CHAVE)
            .setChave(chave ?: "")
            .build()
    }
}
