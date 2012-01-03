package org.inftel.socialwind.server.domain;

import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * Entidad base para las entidades persistentes. Permite unificar el comportamiento y facilita el
 * desarrollo de los servicios DAO.<br>
 * 
 * Nota: Version no puede pornerse en esta entidad mapeada porque sino JPA no actualiza
 * correctamente el valor. Por tanto, solo se crean aquí el get/set abstracto.
 * 
 * @author ibaca
 * 
 */
@Entity
@MappedSuperclass
public abstract class BaseEntity {

    @Basic
    private Date created;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Basic
    private Date updated;

    public Date getCreated() {
        return created;
    }

    public Long getId() {
        return id;
    }

    public Date getUpdated() {
        return updated;
    }

    public abstract Long getVersion();

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public abstract void setVersion(Long version);

    @PrePersist
    void onCreate() {
        Date current = new Date();
        setCreated(current);
        setUpdated(current);
    }

    @PreUpdate
    void onUpdate() {
        Date current = new Date();
        setUpdated(current);
    }
}
