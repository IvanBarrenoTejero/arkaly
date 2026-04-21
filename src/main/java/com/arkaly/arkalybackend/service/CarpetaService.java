package com.arkaly.arkalybackend.service;

import com.arkaly.arkalybackend.dto.CarpetaDTO;
import com.arkaly.arkalybackend.models.Carpeta;
import com.arkaly.arkalybackend.models.Usuario;
import com.arkaly.arkalybackend.repositories.CarpetaRepository;
import com.arkaly.arkalybackend.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarpetaService {

    private final CarpetaRepository carpetaRepository;
    private final UsuarioRepository usuarioRepository;

    public CarpetaService(CarpetaRepository carpetaRepository,
                          UsuarioRepository usuarioRepository) {
        this.carpetaRepository = carpetaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Obtener carpetas raíz del usuario
    public List<Carpeta> getCarpetasRaiz(Integer idUsuario) {
        return carpetaRepository.findByIdUsuarioIdAndIdCarpetaPadreIsNull(idUsuario);
    }

    // Obtener subcarpetas
    public List<Carpeta> getSubcarpetas(Integer idCarpetaPadre) {
        return carpetaRepository.findByIdCarpetaPadreId(idCarpetaPadre);
    }

    // Crear carpeta
    public Carpeta crearCarpeta(Integer idUsuario, String nombre,
                                String color, Integer idCarpetaPadre) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (carpetaRepository.existsByNombreAndIdUsuarioIdAndIdCarpetaPadreId(
                nombre, idUsuario, idCarpetaPadre)) {
            throw new RuntimeException("Ya existe una carpeta con ese nombre en este nivel");
        }

        Carpeta carpeta = new Carpeta();
        carpeta.setIdUsuario(usuario);
        carpeta.setNombre(nombre);
        carpeta.setColor(color != null ? color : "#4A90E2");

        if (idCarpetaPadre != null) {
            Carpeta padre = carpetaRepository.findById(idCarpetaPadre)
                    .orElseThrow(() -> new RuntimeException("Carpeta padre no encontrada"));
            carpeta.setIdCarpetaPadre(padre);
        }

        return carpetaRepository.save(carpeta);
    }

    // Renombrar carpeta
    public Carpeta renombrarCarpeta(Integer idCarpeta, String nuevoNombre) {
        Carpeta carpeta = carpetaRepository.findById(idCarpeta)
                .orElseThrow(() -> new RuntimeException("Carpeta no encontrada"));
        carpeta.setNombre(nuevoNombre);
        return carpetaRepository.save(carpeta);
    }

    // Cambiar color
    public Carpeta cambiarColor(Integer idCarpeta, String color) {
        Carpeta carpeta = carpetaRepository.findById(idCarpeta)
                .orElseThrow(() -> new RuntimeException("Carpeta no encontrada"));
        carpeta.setColor(color);
        return carpetaRepository.save(carpeta);
    }

    // Eliminar carpeta
    public void eliminarCarpeta(Integer idCarpeta) {
        Carpeta carpeta = carpetaRepository.findById(idCarpeta)
                .orElseThrow(() -> new RuntimeException("Carpeta no encontrada"));
        carpetaRepository.delete(carpeta);
    }

    public CarpetaDTO toDTO(Carpeta carpeta) {
        CarpetaDTO dto = new CarpetaDTO();
        dto.setId(carpeta.getId());
        dto.setNombre(carpeta.getNombre());
        dto.setColor(carpeta.getColor());
        dto.setFechaCreacion(carpeta.getFechaCreacion());
        dto.setIdUsuario(carpeta.getIdUsuario().getId());
        dto.setIdCarpetaPadre(carpeta.getIdCarpetaPadre() != null ?
                carpeta.getIdCarpetaPadre().getId() : null);
        return dto;
    }
}