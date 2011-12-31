package org.inftel.socialwind.server.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Entidad que representa un surfero registrado en el sistema.
 * 
 * Estrictamente, los usuarios no se registran, ya que realizan el login a través de google
 * autentication. Por tanto, la existencia de la entidad surfer solo indica que el usuario a
 * accedido almenos una vez a la aplicación.
 * 
 * @author ibaca
 * 
 */
@Entity
@Table(name = "surfers")
public class Surfer extends JpaEntity {

    private String displayName;

    private String email;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    @Version
    private Long version;
    
    private Long currentSpotId;
    
    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public Long getVersion() {
        return version;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void setCurrentSpotId(Long currentSpotId) {
        this.currentSpotId = currentSpotId;
    }

    public Long getCurrentSpotId() {
        return currentSpotId;
    }
}
