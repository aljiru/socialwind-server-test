package org.inftel.socialwind.server.domain;

import com.beoui.geocell.annotations.Geocells;
import com.beoui.geocell.annotations.Latitude;
import com.beoui.geocell.annotations.Longitude;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class Surfer extends JpaEntity {

    @OneToMany(fetch = LAZY)
    public Session activeSession;

    @OneToMany(mappedBy = "surfer")
    public Set<Session> sessions;

    private String displayName;

    private String email;

    @Geocells
    @OneToMany(fetch = EAGER)
    private List<String> geoCellsData = new ArrayList<String>();

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Latitude
    private Double latitude;

    @Longitude
    private Double longitude;

    private String userName;

    @Version
    private Long version;

    public Session getActiveSession() {
        return activeSession;
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

    public Long getId() {
        return id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    public String getUserName() {
        return userName;
    }

    public Long getVersion() {
        return version;
    }

    public void setActiveSession(Session currentSession) {
        this.activeSession = currentSession;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setSessions(Set<Session> sessions) {
        this.sessions = sessions;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
