package org.qdeve.example.angularjs;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qdeve.example.angularjs.dao.ItemRepository;
import org.qdeve.example.angularjs.model.Item;
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

	@Test
	@SuppressWarnings("unchecked")
	public void shouldThrowExceptionWhenRetryingOnExceptionAllTheTime() {
		// Given
		given(itemDAO.findAll())
			.willThrow(ObjectOptimisticLockingFailureException.class);

		// When
		RetryTemplate template = retryConfig.createRetryOnOptimisticLockTemplate();
		catchException(template)
			.execute((context) -> {
					return itemDAO.findAll();
			});

		// Then
		assertThat(caughtException(), isA(ObjectOptimisticLockingFailureException.class));		
		verify(itemDAO, times(maxAttempts)).findAll();
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void shouldReturnItemWhenRetryingOneTime() {
		// Given
		Item dbItem = new Item.Builder().withDefaultValues().build();
		given(itemDAO.findOne(anyLong()))
			.willThrow(ObjectOptimisticLockingFailureException.class)
			.willReturn(dbItem);
		
		// When
		RetryTemplate template = retryConfig.createRetryOnOptimisticLockTemplate();
		Item retrievedItem = template.execute(
			(context) -> {
				return itemDAO.findOne(anyLong());
			}
		);
		
		// Then
		assertThat(retrievedItem, equalTo(dbItem));
		verify(itemDAO, times(2)).findOne(anyLong());
	}

}
