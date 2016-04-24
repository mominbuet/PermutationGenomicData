/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 *
 * @author azizmma
 */
public class QueryDB {
    
    public List<SequencePermuted> getPermutatedSequence(int limit, int offset) {
        List<SequencePermuted> res = new ArrayList<>();
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("outbound");
            EntityManager em = emf.createEntityManager();
            res = em.createNamedQuery("SequencePermuted.findAll", SequencePermuted.class)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }
    
    public List<Sequence> getRegularSequence(int limit, int offset) {
        List<Sequence> res = new ArrayList<>();
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("outbound");
            EntityManager em = emf.createEntityManager();
            res = em.createNamedQuery("Sequence.findAll", Sequence.class)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }
    
    public <T> T insertGeneric(T p) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("outbound");//org.umanitoba_smc_genome_clients_jar_1.0PU
            EntityManager em = emf.createEntityManager();
            EntityTransaction entr = em.getTransaction();
            entr.begin();
            em.persist(p);
            entr.commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return p;
    }
    
    public List<SnpRatio> getAllSnipRatios(String type) {
        List<SnpRatio> res = new ArrayList<SnpRatio>();
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.umanitoba_smc_genome_clients_jar_1.0PU");
            EntityManager em = emf.createEntityManager();
            res = em.createNamedQuery("SnpRatio.findByType", SnpRatio.class)
                    .setParameter("type", type).getResultList();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }

    

    public SnpRatio getAllSnipRatios(String type, int position) {
        SnpRatio res = null;
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.umanitoba_smc_genome_clients_jar_1.0PU");
            EntityManager em = emf.createEntityManager();
            List<SnpRatio> tmpres = em.createNamedQuery("SnpRatio.findByTypePosition", SnpRatio.class)
                    .setParameter("type", type).setParameter("position", position).getResultList();
            if (tmpres.size() > 0) {
                res = tmpres.get(0);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }
    
    public List<SnpRatio> getAllSnipRatios() {
        List<SnpRatio> res = new ArrayList<SnpRatio>();
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.umanitoba_smc_genome_clients_jar_1.0PU");
            EntityManager em = emf.createEntityManager();
            res = em.createNamedQuery("SnpRatio.findAll", SnpRatio.class).getResultList();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }
    
    public List<Snps> getAllSnip() {
        List<Snps> res = new ArrayList<Snps>();
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.umanitoba_smc_genome_clients_jar_1.0PU");
            EntityManager em = emf.createEntityManager();
            res = em.createNamedQuery("Snps.findAll", Snps.class).getResultList();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }
    
    public List<Snps> getAllSnip(String type) {
        List<Snps> res = new ArrayList<Snps>();
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.umanitoba_smc_genome_clients_jar_1.0PU");
            EntityManager em = emf.createEntityManager();
            res = em.createNamedQuery("Snps.findByType", Snps.class).setParameter("type", type).getResultList();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }
    
    public List<SnpRatio> getFromSnipType(String snip, String type) {
        List<SnpRatio> res = new ArrayList<SnpRatio>();
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.umanitoba_smc_genome_clients_jar_1.0PU");
            EntityManager em = emf.createEntityManager();
            res = em.createNamedQuery("SnpRatio.findBySnipIdType", SnpRatio.class).setParameter("snipId", snip).setParameter("type", type).setMaxResults(1).getResultList();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }
    
    public List<Snps> getFromSnip(String snip) {
        List<Snps> res = new ArrayList<Snps>();
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.umanitoba_smc_genome_clients_jar_1.0PU");
            EntityManager em = emf.createEntityManager();
            res = em.createNamedQuery("Snps.findBySnip", Snps.class).setParameter("snip", snip).getResultList();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }
    
    public List<SnpRatio> getRatioFromSnip(String snip) {
        List<SnpRatio> res = new ArrayList<SnpRatio>();
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.umanitoba_smc_genome_clients_jar_1.0PU");
            EntityManager em = emf.createEntityManager();
            res = em.createNamedQuery("SnpRatio.findBySnipId", SnpRatio.class).setParameter("snipId", snip).getResultList();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }
}
