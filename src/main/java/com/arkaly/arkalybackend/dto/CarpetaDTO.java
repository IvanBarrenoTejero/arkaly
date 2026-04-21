package com.arkaly.arkalybackend.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class CarpetaDTO {
    private Integer id;
    private String nombre;
    private String color;
    private Instant fechaCreacion;
    private Integer idUsuario;
    private Integer idCarpetaPadre;
}