package com.arkaly.arkalybackend.repositories;

import com.arkaly.arkalybackend.models.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Integer> {
    List<Documento> findByIdUsuario(Integer idUsuario);
    List<Documento> findByIdCarpeta(Integer idCarpeta);
}