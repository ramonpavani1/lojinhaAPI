package com.example.lojinha.controller;

import com.example.lojinha.model.Usuario;
import com.example.lojinha.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // POST - Registrar um novo usuário
    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario novoUsuario = usuarioService.registrarUsuario(usuario);
            return ResponseEntity.ok(novoUsuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // POST - Login de usuário
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String senha) {
        return usuarioService.autenticarUsuario(email, senha)
                .map(usuario -> ResponseEntity.ok("Login bem-sucedido!"))
                .orElse(ResponseEntity.status(401).body("Falha no login. Credenciais inválidas."));
    }
}
