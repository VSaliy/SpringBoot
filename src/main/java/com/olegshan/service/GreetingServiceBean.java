package com.olegshan.service;

import com.olegshan.model.Greeting;
import com.olegshan.repository.GreetingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import java.util.Collection;

/**
 * Created by olegshan on 29.08.2016.
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS,
        readOnly = true)
public class GreetingServiceBean implements GreetingService {

    @Autowired
    GreetingRepository greetingRepository;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Collection<Greeting> findAll() {
        Collection<Greeting> greetings = greetingRepository.findAll();
        return greetings;
    }

    @Override
    @Cacheable(value = "greetings", key = "#id")
    public Greeting findOne(Long id) {
        Greeting greeting = greetingRepository.findOne(id);
        return greeting;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @CachePut(value = "greetings", key = "#result.id")
    public Greeting create(Greeting greeting) {

        if (greeting.getId() != null) {
            //Cannot create greeting with the specific ID value
            logger.error(
                    "Attempted to create a Greeting, but id attribute was not null.");
            throw new EntityExistsException(
                    "The id attribute must be null to persist a new entity.");
        }

        Greeting saveGreeting = greetingRepository.save(greeting);
        //Illustrates Tx rollback
        /*if (greeting.getId() == 4L) {
            throw new RuntimeException("Roll me back!");
        }*/
        return saveGreeting;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @CachePut(value = "greetings", key = "#greeting.id")
    public Greeting update(Greeting greeting) {

        Greeting greetingPersisted = findOne(greeting.getId());
        if (greetingPersisted == null) {
            //Cannot update greeting that hasn't been persisted
            logger.error(
                    "Attempted to update a Greeting, but the entity does not exist.");
            throw new NoResultException("Requested entity not found.");
        }

        Greeting updateGreeting = greetingRepository.save(greeting);
        return updateGreeting;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @CacheEvict(value = "greetings", key = "#id")
    public void delete(Long id) {
        greetingRepository.delete(id);
    }

    @Override
    //@CacheEvict means clearing of the cache
    @CacheEvict(value = "greetings", allEntries = true)
    public void evictCache() {

    }
}
