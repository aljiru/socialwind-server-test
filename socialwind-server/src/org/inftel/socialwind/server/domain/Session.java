package org.inftel.socialwind.server.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * La sesion representa a un surfer que ha pasado un tiempo en un spot. No es requisito
 * que el surfer navege para que se registre la sesión, ya que el sistema registrar
 * la estancia de un surfer en un spot de forma automatica.<br>
 * 
 * @author ibaca
 *
 */
@Entity
@Table(name = "sessions")
public class Session extends JpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    /** Hora inicio de la sesion */
    private Date start;
    
    /** Hora fin de la sesion */
    private Date end;
    
    /** Surfer que protagoniza la sesion */
    @OneToMany
    private Surfer surfer;
    
    /** Spot donde tiene lugar la sesion */
    @OneToMany
    private Spot spot;

}
