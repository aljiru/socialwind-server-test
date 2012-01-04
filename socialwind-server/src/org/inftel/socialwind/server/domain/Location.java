package org.inftel.socialwind.server.domain;

import javax.persistence.Embeddable;

/**
 * Representa una posicion en el espacion. Es usada principalmente para facilitar el traspaso de
 * informacion a la capa de presentacion. No es {@link Embeddable} debido a limitaciones de geocell.
 * 
 * @author ibaca
 * 
 */
public class Location {

    private double latitude;

    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    double getLongitude() {
        return longitude;
    }

    void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
