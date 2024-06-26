package com.alura.foro.infra.security;

import com.alura.foro.domain.topico.usuarios.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class TokenService {

@Value("${api.security.token.secret}")
private String apiSecret;

    public String generarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create()
                    .withIssuer("foro_api")
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(generarFechaDeExpiracion())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("error al generar el  token jwt");
        }
    }
    public String getSubject(String token) {
        if (token == null ) {
            throw new RuntimeException("token null");
        }
        DecodedJWT verifier = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            verifier = JWT.require(algorithm)
                    // specify any specific claim validations
                    .withIssuer("foro_api")
                    // reusable verifier instance
                    .build()
                    .verify(token);
            verifier.getSubject();
        } catch (JWTVerificationException exception) {
            System.out.println(exception.toString());
            // Invalid signature/claims
        }
        if (verifier.getSubject() == null) {
            throw new RuntimeException("error al verificar el  token");
        }
        return verifier.getSubject();
    }
    private Instant generarFechaDeExpiracion(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-06:00"));
    }
}
