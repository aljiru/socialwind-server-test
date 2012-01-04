package org.inftel.socialwind.server.domain;

import org.inftel.socialwind.services.SurferService;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

/**
 * Informacion de registro de dispositivos para ser notificados.
 * 
 * Un usuario puede estar asociado a varias cuentas, y un telefono puede estar asociado a varias
 * cuentas.
 * 
 * @author ibaca
 * 
 */
@Entity
@Table(name = "devices")
public class Device extends BaseEntity {

    /**
     * Hash del id de dispositivo.
     */
    @Basic(optional = false)
    private Long deviceId;

    /**
     * El ID usado como destinatario de los mensajes.
     */
    @Basic
    private String deviceRegistrationId;

    @Transient
    private Surfer surfer;

    /**
     * Surfero asociado a este dispositivo.
     */
    @Basic
    private Long surferId;

    /**
     * Tipos soportados actualmente:
     * <ul>
     * <li>(default) - ac2dm, regular froyo+ devices using C2DM protocol</li>>
     * </ul>
     * Nuevos tipos podran añadirse - por ejemplo para enviar a chrome.
     */
    @Basic
    private String type;

    /**
     * Correo de la cuenta asociada al dispositivo.
     */
    @Basic(optional = false)
    private String userEmail;

    @Version
    private Long version;
    
    public Long getDeviceId() {
        return deviceId;
    }

    public String getDeviceRegistrationId() {
        return deviceRegistrationId;
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

    public String getType() {
        return type != null ? type : "";
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Long getVersion() {
        return version;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public void setDeviceRegistrationId(String deviceRegistrationId) {
        this.deviceRegistrationId = deviceRegistrationId;
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

    public void setType(String type) {
        this.type = type;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Date getRegistrationDate() {
        return getCreated();
    }

}
