package org.inftel.socialwind.server.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * La sesion representa a un surfer que ha pasado un tiempo en un spot. No es requisito que el
 * surfer navege para que se registre la sesión, ya que el sistema registrar la estancia de un
 * surfer en un spot de forma automatica.<br>
 * 
 * @author ibaca
 * 
 */
@Entity
@Table(name = "sessions")
public class Session extends JpaEntity {

    /** Hora fin de la sesion */
    private Date end;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Spot donde tiene lugar la sesion */
    @OneToMany
    private Spot spot;

    /** Hora inicio de la sesion */
    private Date start;

    /** Surfer que protagoniza la sesion */
    @OneToMany
    private Surfer surfer;

    @Version
    private Long version;

    public Date getEnd() {
        return end;
    }

    public Long getId() {
        return id;
    }

    public Spot getSpot() {
        return spot;
    }

    public Date getStart() {
        return start;
    }

    public Surfer getSurfer() {
        return surfer;
    }

    public Long getVersion() {
        return version;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setSurfer(Surfer surfer) {
        this.surfer = surfer;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getSurferName() {
        return surfer.getDisplayName();
    }

}
