package org.qdeve.example.angularjs;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryConfig {

	@Value("${acme.retry.db.interval}")
	private long initialInterval = 100;

	@Value("${acme.retry.db.count}")
	private int maxAttempts = 3;

	public RetryTemplate createRetryTemplate() {
		return createRetryTemplate(ObjectOptimisticLockingFailureException.class);
	}
	
	public RetryTemplate createRetryTemplate(Class<? extends Throwable> ex) {
		Map<Class<? extends Throwable>, Boolean> exceptions = new HashMap<>();
		exceptions.put(ex, true);

		RetryTemplate template = new RetryTemplate();
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(maxAttempts, exceptions);
		template.setRetryPolicy(retryPolicy);

		ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
		backOffPolicy.setInitialInterval(initialInterval);
		template.setBackOffPolicy(backOffPolicy);

		return template;
	}
}
