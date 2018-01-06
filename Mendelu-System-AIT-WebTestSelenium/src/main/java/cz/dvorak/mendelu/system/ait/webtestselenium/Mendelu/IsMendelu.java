package cz.dvorak.mendelu.system.ait.webtestselenium.Mendelu;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ru.yandex.qatools.allure.annotations.Step;

public class IsMendelu extends CommonAitMendelu{
	
	@Override	
	@BeforeClass
	public void setUp(){
	 super.setUp();
				
	}
	
	@Test
	public void nactiUvodniStranku()throws InterruptedException{
	  prejdiNaUrl(baseUrl);
	  prihlaseni();	
	  dorucenaPosta();
	  odhlasitSe();
	  
	}
			
	//@Test (dependsOnMethods="nactiUvodniStranku")
	public void projdiZaznamnikUcitele()throws InterruptedException{
	  listZaznamnikuUcitele();	
	}
	
	//@Test (dependsOnMethods="nactiUvodniStranku")
	public void ukonceniAplikace()throws InterruptedException{
		odhlasitSe();
	}
	

	
	//@AfterClass
	public void ukonceni()throws Exception{
	  tearDown();
	}

}
