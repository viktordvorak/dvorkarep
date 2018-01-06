package cz.dvorak.mendelu.system.ait.webtestselenium;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;












import ru.yandex.qatools.allure.annotations.Step;

public class CommonAit {

  protected static final long TIMEOUT_IN_SECONDS = 60; // hodnota timeoutu pro WebDriverWait pro rozpoznání neočekávaných situací (v "Nová část obce" 10 sekund na uložení nestačilo, zvyšuji na 15)
  protected static final long INVISIBILITY_TIMEOUT_IN_SECONDS = 60; // hodnota timeoutu pro WebDriverWait pro detekci zmizení
  protected static final int SLEEP_IN_MILISECONDS = 500; // hodnota timeoutu pro Thread.sleep (po přihlášení, konecCiselniku, souhlasimSUpozornenimIfExist) a také implicitní čekání (imlicitlyWait)
  protected static final int MICROSLEEP_IN_MILISECONDS = 15; // hodnota krátkého timeoutu pro Thread.sleep (rozbalení dropdownu)	
  
  protected WebDriver driver;
  protected String baseUrl;	
  protected Properties defaultProperties = new Properties(); // setting-ait-*.properties
  protected Properties prop = new Properties(defaultProperties); // ciBaseUrl.properties
  protected StringBuffer verificationErrors = new StringBuffer();
  protected WebDriverWait wait;
	
  
  @BeforeMethod
public void setUp(){
	loadPropertiesFromFile();
	applyParametrs();
	//basePath = prop.getProperty("path");  
	System.setProperty("webdriver.chrome.driver", "c:/Users/Mirka/Downloads/chromedriver/chromedriver2.exe");
	final ChromeOptions options = new ChromeOptions();
    options.addArguments("--no-sandbox");

    final DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability(ChromeOptions.CAPABILITY, options);
    driver = new ChromeDriver(capabilities);
  
    driver.manage().timeouts().implicitlyWait(SLEEP_IN_MILISECONDS, TimeUnit.MILLISECONDS);
    driver.manage().window().maximize();
	  
	  
  }	
  
  protected void loadPropertiesFromFile() {
	    FileReader input = null;
	    final String filename = "settings-ait-mendelu.properties";
	    try {

	      input = new FileReader(new File(filename));
	      prop.load(input);
	    } catch (final IOException ex) {
	      System.out.println("WARNING: Soubor " + filename + "nebyl nalezen!");
	    } finally {
	      if (input != null) {
	        try {
	          input.close();
	        } catch (final IOException e) {
	          e.printStackTrace();
	        }
	      }
	    }
	  }
  
  private void applyParametrs() {
	    // 1. nejdřív hodnota od Jenkinse, je-li zadána
	    baseUrl = System.getProperty("baseUrl");
	    //Přebírání ze system propoeries	    

	    // 2. potom hodnota předaná z CI (ciBaseUrl.properties), je-li zadána
	    // 3. nakonec hodnoty z properties souborů (settings-ait-*.properties)
	    if (StringUtils.isEmpty(baseUrl)) {
	      baseUrl = prop.getProperty("url");
	      System.out.println("base url:"+baseUrl);
	      
	    }
  }
  
  @AfterMethod
protected void tearDown() throws Exception {
	    driver.quit();
	    final String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      //fail(verificationErrorString);
	    }
  }
  
  

  public WebDriver getDriver() {
	return driver;
  }
  
  @Step("Přechod na URL: {0}.")
  protected void prejdiNaUrl(final String url) {
	 driver.get(url);
  }
  
  protected WebDriverWait webDriverWait(){	  
	return new WebDriverWait(driver, MICROSLEEP_IN_MILISECONDS);	  
  }
  
  @Step("Hledání viditelného elementu podle jména: {0}.")
  protected WebElement najdiElementDleName(final String aName) {
	try {
		WebElement element=webDriverWait().until(ExpectedConditions.presenceOfElementLocated(By.name(aName)));
		return element;
		
	} catch (NoSuchElementException e) {
	  e.printStackTrace();		
	}  	  
	return null;  
  }
  
  
  protected void nastavHodnotuInputuDleName(final String aName, final String hodnota) throws InterruptedException{
	while(true){ 
	   try {
		  WebElement element=najdiElementDleName(aName);
		  webDriverWait().until(ExpectedConditions.elementToBeClickable(element)); 
		 
		  final String value=element.getAttribute("value");
		  if (!Objects.equals(hodnota, value)){
			  
			 final String upravenaHodnota = hodnota.replaceAll("\\(", Keys.chord(Keys.SHIFT,"9"));

	         //input = viditelnyElementSJmenem(name);
	         element.sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.DELETE + upravenaHodnota);			  
			  
		  }
		  break;
		
	   } catch (StaleElementReferenceException e) {
		   osetriStaleElement(e);
	   }	 
	 
	 }
	  
  }
  
  protected WebElement najdiOdkazDleXPath(final String text){
	try {
	  WebElement element=webDriverWait().until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[.//text()[contains(.,'"+ text +"')]]")));	
	  	return element;
	  } catch (final NoSuchElementException e) {
		  e.printStackTrace();
		  return null;		  
	}  		    
  } 
  
  protected WebElement najdiElementBoxDleXPathPoradi(final String XPath, final int vyskyt){
	WebElement input=null;	
	  try {
		List<WebElement> ListElements = webDriverWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(XPath)));
		input=ListElements.get(vyskyt);
		return input;
	} catch (final NoSuchElementException e) {
		e.printStackTrace();
		return null;	
		
	}			  
  }
	  
  
  
  protected void zaskrkniChecboxDleXpath(final String XPath, final int poradi)throws InterruptedException{
	while (true){
	  try {
		WebElement element=najdiElementBoxDleXPathPoradi(XPath, poradi);//("//input[contains(@name, 'email')][contains(@value, '"+value+"')]");
		if (!element.isSelected()){
		  element.click();	
		}
		return;		
	  } catch (final StaleElementReferenceException e) {
		osetriStaleElement(e);
	  }
	}
  }
  
  protected WebElement najdiElementDleXPath(final String XPath){
	try {
	  WebElement element=webDriverWait().until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPath)));
	  return element;
		
	} catch (final NoSuchElementException e) {
		return null;
	}
  }
  
  protected void klikniNaElementDleXpath(final String XPath) throws InterruptedException{
	while (true){
	  try {
		najdiElementDleXPath(XPath).click();  		 
		return;
		
	} catch (StaleElementReferenceException e) {
		osetriStaleElement(e);
	}	
	}  
  }
  
  protected void klikniNaElementDleXpathPoradi(final String XPath, final int poradi) throws InterruptedException{
	while (true){
	  try {
		  najdiElementBoxDleXPathPoradi(XPath, poradi).click(); 		 
		return;
		
	} catch (StaleElementReferenceException e) {
		osetriStaleElement(e);
	}	
	}  
  }
  
  protected WebElement najdiElementDleTagu(final String tag,final String hodnota){
	while(true){
	  try {
	    WebElement element=webDriverWait().until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[self::input or self::button][contains(@"+tag+",'"+hodnota+"')]")));	
	  	return element;
	  } catch (final NoSuchElementException e) {
		return null;   
	  }  	
	   
	}  
  }
  
  @Step("Ztráta fokusu.")
  protected void ztratFokus() throws InterruptedException {
    final Actions actions = new Actions(driver);
    while (true) {
      try {
        final WebElement element = elementSTagem("body");
        webDriverWait().until(ExpectedConditions.visibilityOf(element)); //this will wait for elememt to be visible for 20 seconds
        log("moveToElement " + element);
        actions.moveToElement(element).click().build().perform();
        break;
      } catch (final StaleElementReferenceException e) {
        osetriStaleElement(e);
      } catch (final MoveTargetOutOfBoundsException e) {
        //Z neznámého důvodu občas padá, ale doufám že to nebde mít velký vliv
        log("WARNING: Zachycena MoveTargetOutOfBoundsException");
        e.printStackTrace();
        break;

      }
    }
  }
  
  @Step("Hledání elementu podle jména tagu: {0}.")
  protected WebElement elementSTagem(final String aName) {
    try {
      return webDriverWait().until(ExpectedConditions.presenceOfElementLocated(By.tagName(aName)));
    } catch (final NoSuchElementException e) {
      //fail("Element nenalezen: " + aName + ".");
      return null; // zbytečné, ale jinak kompilační chyba
    }
  }
  
  
  
  protected void klikniNaOdkazsTextem(final String text)throws InterruptedException{
	while(true){
	  try {
		spanek(2000);	  
		WebElement element=najdiOdkazDleXPath(text);
		prejdiFokusemNaElement(element);
		element.click();
		return;
		
	 } catch (final StaleElementReferenceException e) {
		osetriStaleElement(e);	
	  	  
	 }
	} 
  }
  
  protected void klikniNaTlačítkoSName(final String aName)throws InterruptedException{
	while(true){
	  try {
		  najdiElementDleName(aName).click();
		  //WebElement element=najdiElementDleTagu("name", "login");
		  //prejdiFokusemNaElement(element);
		  //element.click();
		  return;
	  } catch (final StaleElementReferenceException e) {
		  osetriStaleElement(e);
	  }	
	}  
  }
  
  
  protected void zkontrolujTextNaStrance(final String text){
	try{
	  spanek(2000);	
	  String stranka=driver.getPageSource();
	  if (stranka.contains(text)){
		 System.out.println("Text zobrazen vezdroji ok:"+text);	
	  }else{
		 System.out.println("Text nezobrazen vezdroji:"+text);
	  }
						
	} catch (Exception e) {
		e.printStackTrace();
	}  
	  
  }
  
  protected void prejdiFokusemNaElement(final WebElement element){
	try {
			
	  ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);			
	} catch (Exception e) {
	  e.printStackTrace();
	}  	  	  	  	  
  }
  
  protected void spanek(int kolik)throws InterruptedException{
	  Thread.sleep(kolik);
  }
  
  protected void osetriStaleElement(final StaleElementReferenceException e) throws InterruptedException {
	    // kdyby někde nastávala StaleElementReferenceException mockrát a/nebo často, bude potřeba řešit, tak abychom věděli kde ...
	    System.out.println("StaleElementReferenceException odchycena: ");
	    System.out.println(new Throwable().getStackTrace()[1]);
	    System.out.println(new Throwable().getStackTrace()[2]);
	    System.out.println(new Throwable().getStackTrace()[3]);
	    System.out.println(new Throwable().getStackTrace()[4]);
	    System.out.println(new Throwable().getStackTrace()[5]);
	    // došlo k překreslení elementu - chvilku počkat, pak vzít "novou verzi" elementu a pokusit se akci provést znovu
	    spanek(500);
  }
  
  protected void nastavHodnotuDropdownuDleName(final String aName, final int hodnota)throws InterruptedException{
	while (true){
	  int noSuchElementExceptionCounter = 0;
	  try {
		final WebElement element=najdiElementDleName(aName);
		final Select select = new Select(element);
		select.selectByIndex(hodnota);
		return;
	  } catch (StaleElementReferenceException e) {
		osetriStaleElement(e);
	  }	catch (final NoSuchElementException e) {
	        // když kvůli nedokončenému javascriptu není dropdown připraven
	      if (noSuchElementExceptionCounter == 0) {
	        noSuchElementExceptionCounter++;
	        spanek(500);
	      } else {
	          throw e;
	      }
	  }  
	 
	}
	  
  }
  
  @Step("Klik na odkaz s textem: {0}.")
  protected void klikniNaOdkazSTextem(final String text) throws InterruptedException {
    while (true) {
      try {
        viditelnyElementSXPath("//a[.//text()[contains(.,'"+ text +"')]]").click();
        return;
      } catch (final StaleElementReferenceException e) {
    	  osetriStaleElement(e);
      }
    }
  }

protected void nastavHodnotuInputuSJmenem(final String name, final String hodnota) throws InterruptedException {
    while (true) {
      try {
        final WebElement input = viditelnyElementSJmenem(name);
        //input[contains(@png-model,'popr.pojistnik.jmeno')]

        webDriverWait().until(ExpectedConditions.elementToBeClickable(input));

        final String value = input.getAttribute("value");
        if (!Objects.equals(hodnota, value)) {

          //input.clear();

          // řešení problému http://stackoverflow.com/questions/19704559/selenium-sendkeys-not-working-for-open-brackets-and-harsh-keys-when-using-java
          final String upravenaHodnota = hodnota.replaceAll("\\(", Keys.chord(Keys.SHIFT,"9"));

          //input = viditelnyElementSJmenem(name);
          input.sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.DELETE + upravenaHodnota);
        }
        break;
      } catch (final StaleElementReferenceException e) {
    	  osetriStaleElement(e);
      }
    }
  }

	  @Step("Hledání viditelného elementu podle jména: {0}.")
  private WebElement viditelnyElementSJmenem(final String aName) {
    try {
      return webDriverWait().until(ExpectedConditions.presenceOfElementLocated(By.name(aName)));
    } catch (final NoSuchElementException e) {
      //fail("Element nenalezen nebo není visible: " + aName + ".");
      return null; // zbytečné, ale jinak kompilační chyba
    }
  }
	  
  @Step("Hledání viditelného elementu podle xpath: {0}.")
  protected WebElement viditelnyElementSXPath(final String aXPath) {
	try {
	  return webDriverWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(aXPath)));
	} catch (final NoSuchElementException e) {
	      //fail("Element nenalezen nebo není visible: " + aXPath + ".");
	  return null; // zbytečné, ale jinak kompilační chyba
	}
  }	 
  
  @Step ("LOG: {0}")
  protected void log(final String logMessage) {
    //empty metod pro logování do ALLURE
  }
	  

  
  
  

}
