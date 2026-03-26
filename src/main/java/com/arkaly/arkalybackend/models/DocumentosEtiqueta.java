package com.arkaly.arkalybackend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "documentos_etiquetas", schema = "arkaly_bd", indexes = {
        @Index(name = "id_etiqueta", columnList = "id_etiqueta")
})
public class DocumentosEtiqueta {
    @EmbeddedId
    private DocumentosEtiquetaId id;

    @MapsId("idDocumento")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_documento", nullable = false)
    private Documento idDocumento;

    @MapsId("idEtiqueta")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_etiqueta", nullable = false)
    private Etiqueta idEtiqueta;

}