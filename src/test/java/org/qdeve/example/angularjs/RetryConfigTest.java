package org.qdeve.example.angularjs;

import static org.junit.Assert.fail;
import org.mockito.Matchers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.qdeve.example.angularjs.data.Item;
import org.qdeve.example.angularjs.repo.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AcmeApplication.class)
public class RetryConfigTest {

	@Autowired
	private RetryConfig retryConfig;

	@Mock
	private ItemRepository itemDAO;

	@Value("${acme.retry.db.count}")
	private int maxAttempts;

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@SuppressWarnings("unchecked")
	@Test(expected = ObjectOptimisticLockingFailureException.class)
	public void exceedRetryDbCountOnOptimisticLockingException() {
		// Given
		RetryTemplate template = retryConfig
				.createRetryOnOptimisticLockTemplate();

		// When
		Mockito
			.when(itemDAO.save(Matchers.anyListOf(Item.class)))
			.thenThrow(ObjectOptimisticLockingFailureException.class);
		
		// Then
		template.execute((context) -> {
			return itemDAO.save(Matchers.anyListOf(Item.class));
		});

		Mockito.verify(itemDAO, Mockito.times(maxAttempts));
		fail("RetryTemplate should throw an exception when retried operation fails more than "
				+ maxAttempts + " times");
	}

}
