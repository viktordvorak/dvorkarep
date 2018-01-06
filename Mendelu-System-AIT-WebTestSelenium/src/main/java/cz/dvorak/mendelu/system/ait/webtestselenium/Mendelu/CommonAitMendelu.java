package cz.dvorak.mendelu.system.ait.webtestselenium.Mendelu;

import cz.dvorak.mendelu.system.ait.webtestselenium.CommonAit;

public class CommonAitMendelu extends CommonAit{
	
	protected void prihlaseni() throws InterruptedException{	
	  
	  klikniNaOdkazsTextem("Přihlášení do osobní administrativy UIS");
	  nastavHodnotuInputuDleName("credential_0", "28381");
	  nastavHodnotuInputuDleName("credential_1", "zko5Zum9");
	  //klikniNaTlačítkoSName("Přihlásit se");
	  viditelnyElementSXPath("//input[contains(@name, 'login')]").click();
	  klikniNaOdkazsTextem("Portál studenta");
	  klikniNaOdkazsTextem("List záznamníku učitele");
      nastavHodnotuDropdownuDleName("studium", 0);
      ztratFokus();
      nastavHodnotuDropdownuDleName("obdobi", 3);
      klikniNaTlačítkoSName("omezit_popupy");
	  //zkontrolujTextNaStrance("Vítejte v Osobní administrativě Univerzitního informačního systému.");
	}
	
	protected void listZaznamnikuUcitele() throws InterruptedException{
	  //klikniNaOdkazSTextem("Portál studenta");
	  //klikniNaOdkazsTextem("Portál studenta");
     				
	}
	
	protected void dorucenaPosta()throws InterruptedException{
	  klikniNaOdkazSTextem("zpráv");
	  nastavHodnotuDropdownuDleName("razeni", 1);
	  klikniNaTlačítkoSName("seradit");
	  zaskrkniChecboxDleXpath("//input[contains(@name, 'email')]", 0);
	  klikniNaElementDleXpathPoradi("//tr//td[8]//img[contains(@title,'Smazat')]", 0);
	  
	}
	
	protected void odhlasitSe() throws InterruptedException{
	  klikniNaElementDleXpath("//a[contains(@title,'Odhlásit se')]");
	  klikniNaTlačítkoSName("odhlaseni");
		
	}	

}
