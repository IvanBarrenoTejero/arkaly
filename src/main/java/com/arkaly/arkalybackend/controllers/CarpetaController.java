package com.arkaly.arkalybackend.controllers;

import com.arkaly.arkalybackend.dto.CarpetaDTO;
import com.arkaly.arkalybackend.models.Carpeta;
import com.arkaly.arkalybackend.repositories.UsuarioRepository;
import com.arkaly.arkalybackend.service.CarpetaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/carpetas")
public class CarpetaController {

    private final CarpetaService carpetaService;
    private final UsuarioRepository usuarioRepository;

    public CarpetaController(CarpetaService carpetaService,
                             UsuarioRepository usuarioRepository) {
        this.carpetaService = carpetaService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/raiz")
    public ResponseEntity<List<CarpetaDTO>> getCarpetasRaiz(Authentication authentication) {
        Integer idUsuario = getIdUsuario(authentication);
        List<Carpeta> carpetas = carpetaService.getCarpetasRaiz(idUsuario);
        List<CarpetaDTO> dtos = carpetas.stream()
                .map(carpetaService::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/subcarpetas")
    public ResponseEntity<List<CarpetaDTO>> getSubcarpetas(@PathVariable Integer id) {
        List<Carpeta> subcarpetas = carpetaService.getSubcarpetas(id);
        List<CarpetaDTO> dtos = subcarpetas.stream()
                .map(carpetaService::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<CarpetaDTO> crearCarpeta(@RequestBody Map<String, String> request,
                                                   Authentication authentication) {
        Integer idUsuario = getIdUsuario(authentication);
        String nombre = request.get("nombre");
        String color = request.get("color");
        Integer idPadre = request.get("idCarpetaPadre") != null ?
                Integer.parseInt(request.get("idCarpetaPadre")) : null;

        Carpeta carpeta = carpetaService.crearCarpeta(idUsuario, nombre, color, idPadre);
        return ResponseEntity.ok(carpetaService.toDTO(carpeta));
    }

    @PutMapping("/{id}/renombrar")
    public ResponseEntity<CarpetaDTO> renombrarCarpeta(@PathVariable Integer id,
                                                       @RequestBody Map<String, String> request) {
        Carpeta carpeta = carpetaService.renombrarCarpeta(id, request.get("nombre"));
        return ResponseEntity.ok(carpetaService.toDTO(carpeta));
    }

    @PutMapping("/{id}/color")
    public ResponseEntity<CarpetaDTO> cambiarColor(@PathVariable Integer id,
                                                   @RequestBody Map<String, String> request) {
        Carpeta carpeta = carpetaService.cambiarColor(id, request.get("color"));
        return ResponseEntity.ok(carpetaService.toDTO(carpeta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCarpeta(@PathVariable Integer id) {
        carpetaService.eliminarCarpeta(id);
        return ResponseEntity.ok("Carpeta eliminada correctamente");
    }

    private Integer getIdUsuario(Authentication authentication) {
        String email = authentication.getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();
    }
}