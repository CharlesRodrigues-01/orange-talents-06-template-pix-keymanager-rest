package br.com.zupacademy.carrega

import br.com.zupacademy.CarregaChavePixResponse
import br.com.zupacademy.TipoDeConta
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class DetalhesChavePixResponse(response: CarregaChavePixResponse) {

    val pixId = response.pixId
    val tipo = response.chave.tipo
    val chave = response.chave.chave
    val criadaEm = response.chave.criadaEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }

    val tipoConta = when(response.chave.conta.tipo){
        TipoDeConta.CONTA_POUPANCA -> "CONTA_POUPANCA"
        TipoDeConta.CONTA_CORRENTE -> "CONTA_CORRENTE"
        else -> "NAO_RECONHECIDA"
    }

    val conta = mapOf(Pair("tipo", tipoConta),
                Pair("instituicao", response.chave.conta.instituicao),
                Pair("nomeDoTitular", response.chave.conta.nomeDoTitular),
                Pair("cpfDoTitular", response.chave.conta.cpfDoTitular),
                Pair("agencia", response.chave.conta.agencia),
                Pair("numero", response.chave.conta.numeroDaConta))
}