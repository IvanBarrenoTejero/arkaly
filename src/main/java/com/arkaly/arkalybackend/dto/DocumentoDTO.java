package com.arkaly.arkalybackend.dto;

import lombok.Data;

// Se usa para reciibr datos del frontend cuando sube un documento, solo los campos necesarios
@Data
public class DocumentoDTO {
    private Integer id;
    private String nombreOriginal;
    private String tipoMime;
    private Long tamanioBytes;
    private String categoria;
    private Integer idUsuario;
    private Integer idCarpeta;
}