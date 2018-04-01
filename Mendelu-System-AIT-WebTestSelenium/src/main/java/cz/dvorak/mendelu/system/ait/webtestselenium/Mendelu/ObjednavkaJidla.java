package cz.dvorak.mendelu.system.ait.webtestselenium.Mendelu;

import org.testng.annotations.Test;

import cz.dvorak.mendelu.system.ait.webtestselenium.CommonAit;

public class ObjednavkaJidla extends CommonAit{
	
  @Test
  public void vyberSiPolozku() throws InterruptedException{
	prejdiNaUrl("http://www.cinajede.cz/");
	//klikniNaOdkazsTextem("Přihlásit se");
	nastavHodnotuInputuDleName("address_value", "Šumavská 35/35, Brno-střed-Veveří, Česko");
	klikniNaTlacitkoSId("SearchAdresa");
	spanek(15000);
	klikniNaOdkazsTextem("Čína");
	klikniNaElementDleXpath("//li[contains(@data-id,'578')]");
		  
  }	
  
  @Test//(dependsOnMethods = { "vyberSiPolozku" })
  protected boolean overObjednavku(){
	String objednavka=najdiOdkazDleXPath("//div[contains(@id,'load_basket_content')]").getText();
	if (objednavka.contains("1 xCena košíku168,-1xTrojky - c) pálivé malé149")){
	  return true;
	}else {
	  return false;	
	}	  
  }
  
  @Test//(dependsOnMethods = { "overObjednavku" })
  protected void smazObjednavku() throws InterruptedException{
	  klikniNaElementDleXpath("//div[contains(@id,'load_basket_content')]//a[contains(@title,'Smazat')]");
  }

}
