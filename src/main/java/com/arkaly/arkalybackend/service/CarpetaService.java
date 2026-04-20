package com.arkaly.arkalybackend.service;

import com.arkaly.arkalybackend.models.Carpeta;
import com.arkaly.arkalybackend.repositories.CarpetaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarpetaService {

    private final CarpetaRepository carpetaRepository;

    public CarpetaService(CarpetaRepository carpetaRepository) {
        this.carpetaRepository = carpetaRepository;
    }

    // Obtener carpetas raíz del usuario (No tienen carpeta padre)
    public List<Carpeta> findAll(Integer idUsuario) {
        return carpetaRepository.findByIdUsuarioAndIdCarpetaPadreIsNull(idUsuario);
    }

    // Obtener subcarpetas de una carpeta
    public List<Carpeta> getSubcarpetas(Integer idCarpetaPadre) {
        return carpetaRepository.findByIdCarpetaPadre(idCarpetaPadre);
    }

    // Crear carpeta
    public Carpeta crearCarpeta(Carpeta carpeta) {
        return carpetaRepository.save(carpeta);
    }

    // Eliminar carpeta
    public void eliminarCarpeta(Integer id) {
        carpetaRepository.deleteById(id);
    }

    // Actualizar carpeta
    public Carpeta actualizarCarpeta(Carpeta carpeta) {
        return carpetaRepository.save(carpeta);
    }


}
