package br.com.zupacademy.shared.enums

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class TipoDeChaveRequestTest{

    @Nested
    inner class CPF{

        @Test
        fun `deve ser valido quando cpf for valido`(){
            with(TipoDeChaveRequest.CPF) {
                assertTrue(valida("00000000000"))
            }
        }

        @Test
        fun `nao deve ser valido quando cpf for invalido`(){
            with(TipoDeChaveRequest.CPF){
                assertFalse(valida("000000"))
            }
        }

        @Test
        fun `nao deve ser valido quando cpf nao for passado`(){
            with(TipoDeChaveRequest.CPF){
                assertFalse(valida(""))
            }
        }

        @Test
        fun `nao deve ser valido quando cpf possuir letras`(){
            with(TipoDeChaveRequest.CPF){
                assertFalse(valida("00000Aa0000"))
            }
        }
    }

    @Nested
    inner class CELULAR{

        @Test
        fun `deve ser valido quando celular for valido`(){
            with(TipoDeChaveRequest.CELULAR){
                assertTrue(valida("+5585988714077"))
            }
        }

        @Test
        fun `nao deve ser valido quando celular nao for valido`(){
            with(TipoDeChaveRequest.CELULAR) {
                assertFalse(valida("5585988714077"))
                assertFalse(valida("+558598871407a"))
            }
        }

        @Test
        fun `nao deve ser valido quando celular nao for passado`(){
            with(TipoDeChaveRequest.CELULAR){
                assertFalse(valida(""))
                assertFalse(valida(null))
            }
        }

    }

    @Nested
    inner class EMAIL{

        @Test
        fun `deve ser valido se o email for valido`(){
            with(TipoDeChaveRequest.EMAIL){
                assertTrue(valida("teste@zup.com.br"))
            }
        }

        @Test
        fun `nao deve ser valido quando email nao for valido`(){
            with(TipoDeChaveRequest.EMAIL) {
                assertFalse(valida("testezup.com.br"))
                assertFalse(valida("teste@zup.com."))
            }
        }

        @Test
        fun `nao deve ser valido quando email nao for passado`(){
            with(TipoDeChaveRequest.EMAIL){
                assertFalse(valida(""))
                assertFalse(valida(null))
            }
        }
    }

    @Nested
    inner class ALEATORIA{

        @Test
        fun `deve ser valido quando chave for nula ou vazia`(){
            with(TipoDeChaveRequest.ALEATORIA){
                assertTrue(valida(null))
                assertTrue((valida("")))
            }
        }

        @Test
        fun `nao deve ser valido quando a chave tiver um valor`(){
            with(TipoDeChaveRequest.ALEATORIA){
                assertFalse(valida("teste"))
            }
        }
    }
}