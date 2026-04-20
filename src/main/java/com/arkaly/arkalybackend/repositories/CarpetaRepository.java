package com.arkaly.arkalybackend.repositories;

import com.arkaly.arkalybackend.models.Carpeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarpetaRepository extends JpaRepository<Carpeta, Integer> {
    List<Carpeta> findByIdUsuarioAndIdCarpetaPadreIsNull(Integer idUsuario);
    List<Carpeta> findByIdCarpetaPadre(Integer idCarpetaPadre);
}