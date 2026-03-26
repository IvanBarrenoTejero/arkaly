package com.arkaly.arkalybackend.controllers;

import com.arkaly.arkalybackend.models.Usuario;
import com.arkaly.arkalybackend.models.enums.Rol;
import com.arkaly.arkalybackend.repositories.UsuarioRepository;
import com.arkaly.arkalybackend.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                          UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // --- RUTA 1: REGISTRO (Para guardar la contraseña encriptada) ---
    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody Map<String, String> request) {
        // ... comprobación de email ...

        Usuario nuevoUsuario = new Usuario();

        // ESTA LÍNEA ES LA CLAVE:
        nuevoUsuario.setNombre(request.get("nombre"));

        nuevoUsuario.setEmail(request.get("email"));
        nuevoUsuario.setUsername(request.get("username"));
        nuevoUsuario.setPasswordHash(passwordEncoder.encode(request.get("password")));

        // ... resto de campos (rol, activo, etc) ...

        usuarioRepository.save(nuevoUsuario);
        return ResponseEntity.ok("Usuario registrado con éxito");
    }

    // --- RUTA 2: LOGIN (Para obtener el Token) ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        // 1. Spring Security comprueba si el email y la contraseña coinciden con la BD
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.get("email"), request.get("password"))
        );

        // 2. Si es correcto, generamos el Token
        String token = jwtUtils.generateToken(request.get("email"));

        // 3. Se lo enviamos al usuario en formato JSON
        return ResponseEntity.ok(Map.of("token", token));
    }
}