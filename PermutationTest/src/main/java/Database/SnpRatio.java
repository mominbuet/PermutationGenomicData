/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author azizmma
 */
@Entity
@Table(name = "snp_ratio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SnpRatio.findAll", query = "SELECT s FROM SnpRatio s"),
    @NamedQuery(name = "SnpRatio.findById", query = "SELECT s FROM SnpRatio s WHERE s.id = :id"),
    @NamedQuery(name = "SnpRatio.findBySnipId", query = "SELECT s FROM SnpRatio s WHERE s.snipId = :snipId"),
    @NamedQuery(name = "SnpRatio.findByType", query = "SELECT s FROM SnpRatio s WHERE s.type = :type"),
    @NamedQuery(name = "SnpRatio.findByRatio", query = "SELECT s FROM SnpRatio s WHERE s.ratio = :ratio"),
    @NamedQuery(name = "SnpRatio.findBySnipIdType", query = "SELECT s FROM SnpRatio s WHERE s.snipId = :snipId and s.type = :type"),
    @NamedQuery(name = "SnpRatio.findByUpdated", query = "SELECT s FROM SnpRatio s WHERE s.updated = :updated")})
public class SnpRatio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "snip_id")
    private String snipId;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @Column(name = "ratio")
    private double ratio;
    @Basic(optional = false)
    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @OneToMany(mappedBy = "validPermutation")
    private Collection<SnpRatio> snpRatioCollection;
    @JoinColumn(name = "valid_permutation", referencedColumnName = "id")
    @ManyToOne
    private SnpRatio validPermutation;
    @OneToMany(mappedBy = "allPermutation")
    private Collection<SnpRatio> snpRatioCollection1;
    @JoinColumn(name = "all_permutation", referencedColumnName = "id")
    @ManyToOne
    private SnpRatio allPermutation;

    public SnpRatio() {
    }

    public SnpRatio(Integer id) {
        this.id = id;
    }

    public SnpRatio(Integer id, String snipId, String type, double ratio, Date updated) {
        this.id = id;
        this.snipId = snipId;
        this.type = type;
        this.ratio = ratio;
        this.updated = updated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSnipId() {
        return snipId;
    }

    public void setSnipId(String snipId) {
        this.snipId = snipId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @XmlTransient
    public Collection<SnpRatio> getSnpRatioCollection() {
        return snpRatioCollection;
    }

    public void setSnpRatioCollection(Collection<SnpRatio> snpRatioCollection) {
        this.snpRatioCollection = snpRatioCollection;
    }

    public SnpRatio getValidPermutation() {
        return validPermutation;
    }

    public void setValidPermutation(SnpRatio validPermutation) {
        this.validPermutation = validPermutation;
    }

    @XmlTransient
    public Collection<SnpRatio> getSnpRatioCollection1() {
        return snpRatioCollection1;
    }

    public void setSnpRatioCollection1(Collection<SnpRatio> snpRatioCollection1) {
        this.snpRatioCollection1 = snpRatioCollection1;
    }

    public SnpRatio getAllPermutation() {
        return allPermutation;
    }

    public void setAllPermutation(SnpRatio allPermutation) {
        this.allPermutation = allPermutation;
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
        if (!(object instanceof SnpRatio)) {
            return false;
        }
        SnpRatio other = (SnpRatio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Database.SnpRatio[ id=" + id + " ]";
    }
    
}
