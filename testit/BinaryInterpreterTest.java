//Tämä on tärkeä, sillä muuten JUnitin luokat (tässä TestCase, Assert ja TestSuite) eivät tule käyttöön.

package fi.hu.cs.titokone;
import junit.framework.*;


//TestCasen perintä tarvitaan, jotta JUnit osaa käsitellä tätä luokkaa.
public class BinaryInterpreterTest extends TestCase {


    private BinaryInterpreter bini;


    /* Tämä on ikäänkuin luokan konstruktori. Sitä kutsutaan
       ensimmäisenä ja siinä asetetaan halutut muuttujat
       halutunlaisiksi.
    */
    protected void setUp() {
	bini = new BinaryInterpreter();
    }
	
   
    public void testBinaryToString() {
	
	Assert.assertEquals(bini.binaryToString(125), "NOP"); 
	Assert.assertEquals(bini.binaryToString(0),"NOP"); 
	Assert.assertEquals(bini.binaryToString(885063680), "POP SP, R1"); 
	Assert.assertEquals(bini.binaryToString(901775360), "PUSHR SP"); 
	Assert.assertEquals(bini.binaryToString(18874568),"STORE R1, 200(R0)"); 
	Assert.assertEquals(bini.binaryToString(35651584),"LOAD R1, =0(R0)"); 
	Assert.assertEquals(bini.binaryToString(555745380),"JNEG R1, 100(R0)"); 
	Assert.assertEquals(bini.binaryToString(69206016),"OUT R1, =CRT"); 
	Assert.assertEquals(bini.binaryToString(538968073),"JUMP 9(R0)"); 
	Assert.assertEquals(bini.binaryToString(1891631115),"SVC SP, =HALT"); 

    }
   
    public void testGetOpCodeFromBinary(){
	
	Assert.assertEquals(bini.getOpCodeFromBinary(12),"0");
	Assert.assertEquals(bini.getOpCodeFromBinary(18874371),"1");
	Assert.assertEquals(bini.getOpCodeFromBinary(1234),"0");
	Assert.assertEquals(bini.getOpCodeFromBinary(271089665),null);
    }

    public void testGetFirstRegisterFromBinary(){
	Assert.assertEquals(bini.getFirstRegisterFromBinary(35651585),"R1");
	Assert.assertEquals(bini.getFirstRegisterFromBinary(1891631115),"SP");
	Assert.assertEquals(bini.getFirstRegisterFromBinary(48234506),"FP");
    }
    public void testGetMemoryModeFromBinary(){
	Assert.assertEquals(bini.getMemoryModeFromBinary(35651585),"=");
	Assert.assertEquals(bini.getMemoryModeFromBinary(36175875),"");
	Assert.assertEquals(bini.getMemoryModeFromBinary(36700163),"@");
	//Assert.assertEquals(bini.getMemoryModeFromBinary(272596993),null);
    }
    
    public void testGetSecondRegisterFromBinary(){
	Assert.assertEquals(bini.getSecondRegisterFromBinary(35651585),"R0");
	Assert.assertEquals(bini.getSecondRegisterFromBinary(555810826),"R1");
	Assert.assertEquals(bini.getSecondRegisterFromBinary(273055745),"SP");
	Assert.assertEquals(bini.getSecondRegisterFromBinary(273121281),"FP");
    }
    //TODO More tests here
    public void testGetAddressFrombinary(){
	Assert.assertEquals(bini.getAddressFromBinary(35651594),"10");
	Assert.assertEquals(bini.getAddressFromBinary(35651585),"1");
	Assert.assertEquals(bini.getAddressFromBinary(35717119),"-1");
	Assert.assertEquals(bini.getAddressFromBinary(35684353),"-32767");
	Assert.assertEquals(bini.getAddressFromBinary(18972670),"32766");
    }


    /*
      public void testaaTyhjennaVatsa() { kisumisu.tyhjennaVatsa(); /*
      Vatsan ei pitäisi nyt olla täysi. Seuraava metodi testaa onko näin.
      Assert-luokan assert-alkuiset metodit ilmoittavat JUnitille tapahtuiko
      jotain virheitä ja käyttäjä sitten näkee nämä virheet JUnitin
      käyttöliittymässä.  Tällä kertaa kisumisun vatsa onkin täysi vaikka
      sen pitäisi olla tyhjä, joten assertTrue saa parametrikseen falsen,
      vaikka se odottaa true:a, koska testaaja on asettanut sen odottamaan
      sitä. On siis testaajan vastuulla, että testit ovat oikeita.
  
      Assert.assertTrue(!kisumisu.onkoVatsaTaysi()); 
      }
      
    */
    /* Tämän merkitys on mulle jotenkin hämärä. Tällä metodilla voi
       ainakin koota yhteen useampia testiluokkia ja suorittaa ne
       yhdessä. Kätevää esim. tehdä AllTests luokka, jonka ajamalla
       ajetaan kerralla kaikki testit. Tässä tällä ei kuitenkaan taida
       olla mitään virkaa, sillä JUnit osaa ajaa tämän luokan ihan
       ilmankin.
    */
    public static Test suite() {
	return new TestSuite(BinaryInterpreterTest.class);
    }
    
    public static void main (String[] args) {
	/* Kun luokka yritetään suorittaa sellaisenaan komennolla
	   "java KissaTest", niin ajetaan testi
	   shell-pohjaisena. Tähän voisi toki rakentaa vaikka hienon
	   valikkosysteemin, josta voisi valita että ajetaanko SWing
	   vai tekstipohjainen.
	*/
	junit.textui.TestRunner.run (suite());
    }
    
}
