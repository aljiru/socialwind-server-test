package org.inftel.socialwind.server.domain;

import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
public abstract class JpaEntity {

    public abstract Long getId();

    public abstract void setId(Long id);

    public abstract Long getVersion();

    public abstract void setVersion(Long version);

    private Date created;
    private Date updated;

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
    
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
