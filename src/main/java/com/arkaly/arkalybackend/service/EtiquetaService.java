package com.arkaly.arkalybackend.service;


import com.arkaly.arkalybackend.models.Etiqueta;
import com.arkaly.arkalybackend.repositories.EtiquetaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EtiquetaService {

    private EtiquetaRepository etiquetaRepository;

    public EtiquetaService(EtiquetaRepository etiquetaRepository) {
        this.etiquetaRepository = etiquetaRepository;
    }

    public List<Etiqueta> getEtiquetasUsuario(Integer idUsuario) {
        return etiquetaRepository.findByIdUsuario(idUsuario);
    }

    public Etiqueta crearEtiqueta(Etiqueta etiqueta) {
        return etiquetaRepository.save(etiqueta);
    }

    public Etiqueta actualizarEtiqueta(Etiqueta etiqueta) {
        return etiquetaRepository.save(etiqueta);
    }

    public void eliminarEtiqueta(Integer idEtiqueta) {
        etiquetaRepository.deleteById(idEtiqueta);
    }
}
