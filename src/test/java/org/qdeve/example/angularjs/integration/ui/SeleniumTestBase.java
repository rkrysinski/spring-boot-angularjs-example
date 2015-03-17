package org.qdeve.example.angularjs.integration.ui;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.qdeve.example.angularjs.AcmeApplication;
import org.qdeve.example.angularjs.data.Item;
import org.qdeve.example.angularjs.repo.ItemRepository;
import org.seleniumhq.selenium.fluent.FluentBy;
import org.seleniumhq.selenium.fluent.FluentWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Base class for Selenium WebDrive based UI testing.
 * <p>
 * Provides start up of integration server, set up a web driver, opens the browser
 * and handle it's cleanup. It also provisions Item under test, and has utilities.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AcmeApplication.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0" })
public class SeleniumTestBase {

	private static final long DEFAULT_WAIT_TIME_SECONDS = 10L;
	private static final long DEFAULT_EXPLICIT_WAIT_TIME_SECONDS = 5L;

	@Value("${local.server.port}")
	private int port;

	@Autowired
	private ItemRepository itemDAO;

	private WebDriver driver;
	protected URI baseURL;
	protected Item dbItem;
	protected FluentWebDriver fwd;

	@Before
	public void setUp() throws Exception {
		setUpDB();
		baseURL = new URI("http://localhost:" + port);
		driver = new FirefoxDriver();
		driver.manage().timeouts()
				.implicitlyWait(DEFAULT_WAIT_TIME_SECONDS, TimeUnit.SECONDS);
		fwd = new FluentWebDriver(driver);
		driver.get(baseURL.toString());
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
	
	/**
	 * Hooks into Angular's internals, and block until requests have completed.
	 */
	public static By ngWait(final By by) {
	    return new FluentBy() {
	        @Override
	        public void beforeFindElement(WebDriver driver) {
	            driver.manage().timeouts().setScriptTimeout(DEFAULT_EXPLICIT_WAIT_TIME_SECONDS, TimeUnit.SECONDS);
	            ((JavascriptExecutor) driver).executeAsyncScript("var callback = arguments[arguments.length - 1];" +
	                "angular.element(document.body).injector().get('$browser').notifyWhenNoOutstandingRequests(callback);");
	            super.beforeFindElement(driver);
	        }

	        @Override
	        public List<WebElement> findElements(SearchContext context) {
	            return by.findElements(context);
	        }

	        @Override
	        public WebElement findElement(SearchContext context) {
	            return by.findElement(context);
	        }
	    };
	}	

}
