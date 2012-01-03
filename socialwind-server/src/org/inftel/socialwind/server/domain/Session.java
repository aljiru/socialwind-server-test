package org.inftel.socialwind.server.domain;

import org.inftel.socialwind.services.SpotService;
import org.inftel.socialwind.services.SurferService;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
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
public class Session extends BaseEntity {

    /** Hora fin de la sesion */
    private Date end;

    @Transient
    private Spot spot;

    /** Spot donde tiene lugar la sesion */
    // @OneToMany
    private Long spotId;

    /** Hora inicio de la sesion */
    private Date start;

    @Transient
    private Surfer surfer;

    /** Surfer que protagoniza la sesion */
    // @OneToMany
    private Long surferId;

    @Version
    private Long version;

    public Date getEnd() {
        return end;
    }

    public Spot getSpot() {
        if (spotId == null) {
            return null;
        }
        if (spot == null) {
            spot = SpotService.findSpot(getSpotId());
        }
        return spot;
    }

    public Long getSpotId() {
        return spotId;
    }

    public Date getStart() {
        return start;
    }

    public Surfer getSurfer() {
        if (surferId == null) {
            return null;
        }
        if (surfer == null) {
            surfer = SurferService.findSurfer(getSurferId());
        }
        return surfer;
    }

    public Long getSurferId() {
        return surferId;
    }

    public Long getVersion() {
        return version;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setSpot(Spot spot) {
        if (spot == null) {
            this.spot = null;
            this.spotId = null;
        } else {
            this.spot = spot;
            this.spotId = spot.getId();
        }
    }

    public void setSpotId(Long spotId) {
        this.spotId = spotId;
        this.spot = null;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setSurfer(Surfer surfer) {
        if (surfer == null) {
            this.surfer = null;
            this.surferId = null;
        } else {
            this.surfer = surfer;
            this.surferId = surfer.getId();
        }
    }

    public void setSurferId(Long surferId) {
        this.surferId = surferId;
        this.surfer = null;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
