package org.inftel.socialwind.server.domain;

import com.beoui.geocell.annotations.Geocells;
import com.beoui.geocell.annotations.Latitude;
import com.beoui.geocell.annotations.Longitude;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
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
public class Surfer extends BaseEntity {

    // @OneToOne
    private Long activeSessionId;

    private String displayName;

    private String email;

    @Geocells
    @OneToMany(fetch = EAGER)
    private List<String> geoCellsData = new ArrayList<String>();

   
    @Latitude
    private double latitude;

    @Longitude
    private double longitude;

    private String userName;

    @Version
    private Long version;

    public Long getActiveSessionId() {
        return activeSessionId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getGeoCellsData() {
        return geoCellsData;
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getUserName() {
        return userName;
    }

    public Long getVersion() {
        return version;
    }

    public void setActiveSessionId(Long sessionId) {
        this.activeSessionId = sessionId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGeoCellsData(List<String> geoCellsData) {
        this.geoCellsData = geoCellsData;
    }


    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
