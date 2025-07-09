package com.stopsnearme.app.ws.ui.model;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Repository;

import com.stopsnearme.app.ws.ui.model.entity.Job;

// import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

// @Stateless
@Repository
public class JobRepository {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @PersistenceContext
    private EntityManager em;

    public Job create(Job job) {
        logger.log(Level.INFO, "Creating new job {0}", job.hashCode());
        em.persist(job);

        return job;
    }

    public List<Job> findAll() {
        logger.info("Getting all jobs");
        return em.createQuery("SELECT c FROM Job c", Job.class).getResultList();
    }

    public Optional<Job> findById(Long id) {
        logger.log(Level.INFO, "Getting job by id {0}", id);
        return Optional.ofNullable(em.find(Job.class, id));
    }

    public void delete(Long id) {
        logger.log(Level.INFO, "Deleting coffee by id {0}", id);
        var job = findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid job Id:" + id));
        em.remove(job);
    }

    public Job update(Job job) {
        logger.log(Level.INFO, "Updating job {0}", job.getId());
        return em.merge(job);
    }
}
