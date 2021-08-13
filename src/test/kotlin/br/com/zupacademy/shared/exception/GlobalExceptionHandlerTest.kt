package br.com.zupacademy.shared.exception

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GlobalExceptionHandlerTest{

    val requestGeneric = HttpRequest.GET<Any>("/")

    @Test
    fun `deve retornar 404 quando statusException for not found`(){

        // cenário
        val mensagem = "não encontrado"
        val notFoundException = StatusRuntimeException(Status.NOT_FOUND.withDescription(mensagem))

        // ação
        val response = GlobalExceptionHandler().handle(requestGeneric, notFoundException)

        // validação
        with(response){
            assertEquals(HttpStatus.NOT_FOUND, this.status)
            assertNotNull(response.body())
            assertEquals(mensagem, (response.body() as JsonError).message)
        }
    }

    @Test
    fun `deve retornar 422 quando statusException for already exists`(){

        val mensagem = "Esta chave já existe"
        val alreadyExistsException = StatusRuntimeException(Status.ALREADY_EXISTS.withDescription(mensagem))

        // ação
        val response = GlobalExceptionHandler().handle(requestGeneric, alreadyExistsException)

        // validação
        with(response){
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, this.status)
            assertNotNull(response.body())
            assertEquals(mensagem, (response.body() as JsonError).message)
        }
    }

    @Test
    fun `deve retornar 400 quando statusException for invalid argument`(){

        // cenário
        val invalidArgumentException = StatusRuntimeException(Status.INVALID_ARGUMENT)

        // ação
        val response = GlobalExceptionHandler().handle(requestGeneric, invalidArgumentException)

        // validação
        with(response){
            assertEquals(HttpStatus.BAD_REQUEST, this.status)
            assertNotNull(response.body())
            assertEquals("Dados da requisição estão inválidos", (response.body() as JsonError).message)
        }
    }

    @Test
    fun `deve retornar 500 quando qualquer outro erro for lançado`(){

        // cenário
        val internalServerException = StatusRuntimeException(Status.UNKNOWN)

        // ação
        val response = GlobalExceptionHandler().handle(requestGeneric, internalServerException)

        // validação
        with(response){
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, this.status)
            assertNotNull(response.body())
            assertEquals("Não foi possível completar a requisição",
                (this.body() as JsonError).message)
        }
    }
}