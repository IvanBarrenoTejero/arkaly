package com.arkaly.arkalybackend.dto;

import lombok.Data;

import java.time.Instant;

// Se usa para devolver datos al frontend
@Data
public class DocumentoResponseDTO {
    private Integer id;
    private String nombreOriginal;
    private String rutaRelativa;
    private String tipoMime;
    private Long tamanioBytes;
    private String categoria;
    private Instant fechaSubida;
    private Integer idUsuario;
    private Integer idCarpeta;
}