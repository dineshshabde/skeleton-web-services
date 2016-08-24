package org.example.ws.batch;

import java.util.Collection;

import org.example.ws.model.Greeting;
import org.example.ws.service.GreetingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Adding profile as batch even if base configuration is invoked, profile
 * configuration overrides base since profile configuration is called at last.
 * 
 * Sprig looks for application-<profile name>.properties
 * 
 * @author dinesh
 *
 */
@Profile("batch")
@Component
public class GreeingBatchBean {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private GreetingService greetingService;

	@Scheduled(cron = "${batch.greeting.cron}")
	public void cronJob() {
		logger.info("> cronJob");

		// Add schedule logic here
		Collection<Greeting> greeting = greetingService.findAll();
		logger.info("There are {} greetings in the data store.", greeting.size());

		logger.info("< cronJob");
	}

	@Scheduled(initialDelayString = "${batch.greeting.fixeddelay}", fixedRateString = "${batch.greeting.fixedrate}")
	public void fixedRateJobWithInitialDelay() {
		logger.info("> fixedRateJobWithInitialDelay");

		// Add scheduling logic here
		// Simulate Job processing time
		long pause = 5000;
		long start = System.currentTimeMillis();
		do {
			if (start + pause < System.currentTimeMillis()) {
				break;
			}
		} while (true);
		logger.info("Processing time was {} seconds.", pause / 1000);

		logger.info("< fixedRateJobWithInitialDelay");
	}

	@Scheduled(initialDelayString = "${batch.greeting.initialdelay}", fixedDelayString = "${batch.greeting.fixeddelay}")
	public void fixedDelayJobWithInitialDelay() {
		logger.info("> fixedDelayJobWithInitialDelay");

		// Add scheduling logic here
		// Simulate Job processing time
		long pause = 5000;
		long start = System.currentTimeMillis();
		do {
			if (start + pause < System.currentTimeMillis()) {
				break;
			}
		} while (true);
		logger.info("Processing time was {} seconds.", pause / 1000);

		logger.info("< fixedDelayJobWithInitialDelay");
	}
}
