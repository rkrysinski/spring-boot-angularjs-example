package org.qdeve.example.angularjs.integration.ui;

import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.qdeve.example.angularjs.AcmeApplication;
import org.qdeve.example.angularjs.data.Item;
import org.qdeve.example.angularjs.repo.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Base class for Selenium WebDrive based Web UI testing.
 * <p>
 * Provides start up of integration server, set up of a web driver and it's
 * cleanup. It also provisions test item into DB at startup time.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AcmeApplication.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0" })
public class SeleniumTestBase {

	public static final long DEFAULT_WAIT_TIME_SECONDS = 5;

	@Value("${local.server.port}")
	private int port;

	@Autowired
	private ItemRepository itemDAO;

	protected URI baseURL;
	protected Item dbItem;
	protected WebDriver driver;
	protected WebDriverWait wait;

	@Before
	public void setUp() throws Exception {
		setUpDB();
		baseURL = new URI("http://localhost:" + port);
		driver = new FirefoxDriver();
		driver.manage().timeouts()
				.implicitlyWait(DEFAULT_WAIT_TIME_SECONDS, TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, DEFAULT_WAIT_TIME_SECONDS);
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

	private void setUpDB() {
		dbItem = new Item.Builder().withDefaultValues().build();
		itemDAO.deleteAll();
		dbItem = itemDAO.save(Arrays.asList(dbItem)).get(0);
	}

}
