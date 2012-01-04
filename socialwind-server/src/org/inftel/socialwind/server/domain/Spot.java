package org.inftel.socialwind.server.domain;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.GeocellUtils;
import com.beoui.geocell.annotations.Geocells;
import com.beoui.geocell.annotations.Latitude;
import com.beoui.geocell.annotations.Longitude;
import com.beoui.geocell.model.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

@Entity
@Table(name = "spots")
public class Spot extends BaseEntity {

    private String description;

    @Geocells
    @OneToMany(fetch = EAGER)
    private List<String> geoCellsData = new ArrayList<String>();

    /** Si es true indica que la playa es hotspot */
    private Boolean hot;

    private String imgUrl;

    @Latitude
    private double latitude;

    @Transient
    private Location location;
    
    @Longitude
    private double longitude;

    private String name;

    /** Contador de todos los surfer que han pasado por la playa */
    private Integer surferCount;

    /** Contador de todos los surfer que estan en la playa */
    private Integer surferCurrentCount;

    @Version
    private Long version;

    public String getDescription() {
        return description;
    }

    public List<String> getGeoCellsData() {
        return (geoCellsData == null) ? Collections.<String>emptyList() : geoCellsData;
    }

    public Boolean getHot() {
        return (hot == null) ? false : hot;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public Location getLocation() {
        if (location == null) {
            location = new Location();
            location.setLatitude(latitude);
            location.setLongitude(longitude);
        }
        return location;
    }
    
    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return (name == null) ? "" : name;
    }

    public Integer getSurferCount() {
        return (surferCount == null) ? 0 : surferCount;
    }

    public Integer getSurferCurrentCount() {
        return (surferCurrentCount == null) ? 0 : surferCurrentCount;
    }

    public Long getVersion() {
        return version;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGeoCellsData(List<String> geoCellsData) {
        this.geoCellsData = geoCellsData;
    }

    public void setHot(Boolean hot) {
        this.hot = hot;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * Modifica la latitude sin modificar geocells.
     * 
     * @see Spot#setLatLng(double, double)
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
        this.location = null;
    }

    /**
     * Modifica la latitud y longitud de la playa y actualiza las geoceldas.
     * 
     * @param latitude
     * @param longitude
     */
    public void setLocation(Location location) {
        // TODO quitar/encapsular todas las dependencias a Geocell de las entidades
        Point point = new Point(location.getLatitude(), location.getLongitude());
        setGeoCellsData(GeocellManager.generateGeoCell(point));
        this.location = location;
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    /**
     * Modifica la latitude sin modificar geocells.
     * 
     * @see Spot#setLatLng(double, double)
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
        this.location = null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurferCount(Integer surferCount) {
        this.surferCount = surferCount;
    }

    public void setSurferCurrentCount(Integer surferCurrentCount) {
        this.surferCurrentCount = surferCurrentCount;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
