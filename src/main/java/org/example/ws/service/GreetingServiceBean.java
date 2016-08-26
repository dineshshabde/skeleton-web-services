package org.example.ws.service;

import java.util.Collection;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;

import org.example.ws.model.Greeting;
import org.example.ws.repository.GreetingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class GreetingServiceBean implements GreetingService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private GreetingRepository greetingRepository;

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
		logger.info("> create");

		// Ensure the entity object to be created does NOT exist in the
		// repository. Prevent the default behavior of save() which will update
		// an existing entity if the entity matching the supplied id exists.
		if (greeting.getId() != null) {
			// Cannot create Greeting with specified ID value
			logger.error("Attempted to create a Greeting, but id attribute was not null.");
			throw new EntityExistsException("The id attribute must be null to persist a new entity.");
		}

		Greeting savedGreeting = greetingRepository.save(greeting);

		logger.info("< create");
		return savedGreeting;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CachePut(value = "greetings", key = "#greeting.id")
	public Greeting update(Greeting greeting) {
		logger.info("> update id:{}", greeting.getId());

		// Ensure the entity object to be updated exists in the repository to
		// prevent the default behavior of save() which will persist a new
		// entity if the entity matching the id does not exist
		Greeting greetingToUpdate = findOne(greeting.getId());
		if (greetingToUpdate == null) {
			// Cannot update Greeting that hasn't been persisted
			logger.error("Attempted to update a Greeting, but the entity does not exist.");
			throw new NoResultException("Requested entity not found.");
		}

		greetingToUpdate.setText(greeting.getText());
		Greeting updatedGreeting = greetingRepository.save(greetingToUpdate);

		logger.info("< update id:{}", greeting.getId());
		return updatedGreeting;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CacheEvict(value = "greetings", key = "#id")
	public void delete(Long id) {
		greetingRepository.delete(id);
	}

	@Override
	@CacheEvict(value = "greetings", allEntries = true)
	public void evictCache() {

	}

}
