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
@Table(name = "sequence_permuted")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SequencePermuted.findAll", query = "SELECT s FROM SequencePermuted s"),
    @NamedQuery(name = "SequencePermuted.findById", query = "SELECT s FROM SequencePermuted s WHERE s.id = :id"),
    @NamedQuery(name = "SequencePermuted.findByType", query = "SELECT s FROM SequencePermuted s WHERE s.type = :type"),
    @NamedQuery(name = "SequencePermuted.findByInserted", query = "SELECT s FROM SequencePermuted s WHERE s.inserted = :inserted"),
    @NamedQuery(name = "SequencePermuted.findByPermutatedSequence", query = "SELECT s FROM SequencePermuted s WHERE s.permutatedSequence = :permutatedSequence"),
    @NamedQuery(name = "SequencePermuted.findByTag", query = "SELECT s FROM SequencePermuted s WHERE s.tag = :tag")})
public class SequencePermuted implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "type")
    private String type;
    @Column(name = "inserted")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inserted;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "permutated_sequence")
    private String permutatedSequence;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "tag")
    private String tag;

    public SequencePermuted() {
    }

    public SequencePermuted(Integer id) {
        this.id = id;
    }

    public SequencePermuted(Integer id, String type, String permutatedSequence, String tag) {
        this.id = id;
        this.type = type;
        this.permutatedSequence = permutatedSequence;
        this.tag = tag;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getInserted() {
        return inserted;
    }

    public void setInserted(Date inserted) {
        this.inserted = inserted;
    }

    public String getPermutatedSequence() {
        return permutatedSequence;
    }

    public void setPermutatedSequence(String permutatedSequence) {
        this.permutatedSequence = permutatedSequence;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
        if (!(object instanceof SequencePermuted)) {
            return false;
        }
        SequencePermuted other = (SequencePermuted) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Database.SequencePermuted[ id=" + id + " ]";
    }
    
}
