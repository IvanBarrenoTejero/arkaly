package com.arkaly.arkalybackend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class DocumentosEtiquetaId implements Serializable {
    private static final long serialVersionUID = -5394861356528573171L;
    @Column(name = "id_documento", nullable = false)
    private Integer idDocumento;

    @Column(name = "id_etiqueta", nullable = false)
    private Integer idEtiqueta;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DocumentosEtiquetaId entity = (DocumentosEtiquetaId) o;
        return Objects.equals(this.idDocumento, entity.idDocumento) &&
                Objects.equals(this.idEtiqueta, entity.idEtiqueta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDocumento, idEtiqueta);
    }

}