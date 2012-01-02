package org.inftel.socialwind.server.domain;

import com.beoui.geocell.annotations.Geocells;
import com.beoui.geocell.annotations.Latitude;
import com.beoui.geocell.annotations.Longitude;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "spots")
public class Spot extends JpaEntity {

    private String description;

    @Geocells
    @OneToMany(fetch = EAGER)
    private List<String> geoCellsData = new ArrayList<String>();

    /** Si es true indica que la playa es hotspot */
    private Boolean hot = false;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String imgUrl;

    @Latitude
    private double latitude;

    @Longitude
    private double longitude;

    private String name;

    /** Contador de todos los surfer que han pasado por la playa */
    private Integer surferCount = 0;

    /** Contador de todos los surfer que estan en la playa */
    private Integer surferCurrentCount = 0;

    @Version
    private Long version;

    public String getDescription() {
        return description;
    }

    public List<String> getGeoCellsData() {
        return geoCellsData;
    }

    public Boolean getHot() {
        return hot;
    }

    public Long getId() {
        return id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public Integer getSurferCount() {
        return surferCount;
    }

    public Integer getSurferCurrentCount() {
        return surferCurrentCount;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
