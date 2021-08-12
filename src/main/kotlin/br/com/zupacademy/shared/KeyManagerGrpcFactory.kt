package br.com.zupacademy.shared

import br.com.zupacademy.KeyManagerCarregaGrpcServiceGrpc
import br.com.zupacademy.KeyManagerListaGrpcServiceGrpc
import br.com.zupacademy.KeyManagerRegistraGrpcServiceGrpc
import br.com.zupacademy.KeyManagerRemoveGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class KeyManagerGrpcFactory(@GrpcChannel("keyManager") val channel: ManagedChannel) {

    @Singleton
    fun registraChave() : KeyManagerRegistraGrpcServiceGrpc.KeyManagerRegistraGrpcServiceBlockingStub? {
        return KeyManagerRegistraGrpcServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun deletaChave() : KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub? {
        return KeyManagerRemoveGrpcServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun carregaChave() : KeyManagerCarregaGrpcServiceGrpc.KeyManagerCarregaGrpcServiceBlockingStub? {
        return KeyManagerCarregaGrpcServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun listaChave() : KeyManagerListaGrpcServiceGrpc.KeyManagerListaGrpcServiceBlockingStub? {
        return KeyManagerListaGrpcServiceGrpc.newBlockingStub(channel)
    }
}