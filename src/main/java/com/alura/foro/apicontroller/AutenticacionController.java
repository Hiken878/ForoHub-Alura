package com.alura.foro.apicontroller;

import com.alura.foro.domain.topico.usuarios.Usuario;
import com.alura.foro.infra.security.DatosJWTtoken;
import com.alura.foro.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")

public class AutenticacionController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity realizarLogin(@RequestBody DatosAutenticacion datos){
        var authenticationToken = new UsernamePasswordAuthenticationToken(datos.login(), datos.clave());
        var usuarioAutenticado = manager.authenticate(authenticationToken);
        var JWTtoken = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());

        return ResponseEntity.ok(new DatosJWTtoken(JWTtoken));
    }
}
