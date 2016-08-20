package org.example.ws.batch;

import java.util.Collection;

import org.example.ws.model.Greeting;
import org.example.ws.service.GreetingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GreeingBatchBean {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private GreetingService greetingService;

	@Scheduled(cron = "0,30 * * * * *")
	public void cronJob() {
		logger.info("> cronJob");

		// Add schedule logic here
		Collection<Greeting> greeting = greetingService.findAll();
		logger.info("There are {} greetings in the data store.", greeting.size());

		logger.info("< cronJob");
	}
}
