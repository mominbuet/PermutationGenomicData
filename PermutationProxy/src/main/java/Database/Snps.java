/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author azizmma
 */
@Entity
@Table(name = "snps")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Snps.findAll", query = "SELECT s FROM Snps s"),
    @NamedQuery(name = "Snps.findById", query = "SELECT s FROM Snps s WHERE s.id = :id"),
    @NamedQuery(name = "Snps.findBySnip", query = "SELECT s FROM Snps s WHERE s.snip = :snip"),
    @NamedQuery(name = "Snps.findBySnipType", query = "SELECT s FROM Snps s WHERE s.snip = :snip and s.type = :type"),
    @NamedQuery(name = "Snps.findByDescription", query = "SELECT s FROM Snps s WHERE s.description = :description"),
    @NamedQuery(name = "Snps.findByUpdated", query = "SELECT s FROM Snps s WHERE s.updated = :updated"),
    @NamedQuery(name = "Snps.findByType", query = "SELECT s FROM Snps s WHERE s.type = :type"),
    @NamedQuery(name = "Snps.findByServerName", query = "SELECT s FROM Snps s WHERE s.serverName = :serverName")})
public class Snps implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "snip")
    private String snip;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "description")
    private String description;
    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "type")
    private String type;
    @Size(max = 20)
    @Column(name = "server_name")
    private String serverName;

    public Snps() {
    }

    public Snps(Integer id) {
        this.id = id;
    }

    public Snps(Integer id, String snip, String description, String type) {
        this.id = id;
        this.snip = snip;
        this.description = description;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSnip() {
        return snip;
    }

    public void setSnip(String snip) {
        this.snip = snip;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Snps)) {
            return false;
        }
        Snps other = (Snps) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Database.Snps[ id=" + id + " ]";
    }
    
}
