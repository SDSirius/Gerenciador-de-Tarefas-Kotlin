package br.com.devaria.kotlin.projects.gerenciador_tarefas.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component

@Component
class JWTUtils {

    private val chaveSegurança = "ChaveSegurançaKotlinSuperSecreta2024MadeBySDSirius"

    fun gerarToken(idUsuario: String): String {
        return Jwts.builder()
            .setSubject(idUsuario)
            .signWith(SignatureAlgorithm.HS512, chaveSegurança.toByteArray())
            .compact()
    }

    fun isTokenValid(token: String): Boolean {
        val claims = getClaimsToken(token)
        if (claims != null) {
            val idUsuario = claims.subject
            if (!idUsuario.isNullOrEmpty() && !idUsuario.isBlank()) {
                return true
            }
        }
        return false
    }

    private fun getClaimsToken(token: String) = try {
        Jwts
            .parser()
            .setSigningKey(chaveSegurança.toByteArray())
            .parseClaimsJws(token)
            .body
    } catch (exception: Exception) {
        null
    }

    fun getUsuarioId(token:String)  :String? {
        val claims = getClaimsToken(token)
        return claims?.subject
    }

}