import java.util.logging.*;

public class Logthing {
    public static void main(String[] args) {
	// Mahdollista myös sanoa getLoggerille kakkosparametrinä
	// resourcebundle, jonka perusteella lokiviestit voidaan kääntää.
	// Emäloggeri.
	Logger logtopus = Logger.getLogger("fi.hu.cs.titokone");
	// Ei lähetetä emoille, koska sitten ne kirjoittaa hölmöä 
	// oletusoutputtia jota ei haluta. Sen sijaan kaikki meidän
	// lapset (kuten logomatic) lähettää kaiken kiltisti meille
	// ja me käsitellään ne.
	logtopus.setUseParentHandlers(false); 
	Handler[] foo = logtopus.getHandlers();
	for(int i = 0; i < foo.length; i++) {
	    logtopus.removeHandler(foo[i]);
	    System.out.println("Poistin handlerin: " + foo[i]);
	} // Ei niitä oletuksena oo.
	logtopus.addHandler(new LokiHandler());
	logtopus.setLevel(Level.INFO);
	// Lapsiloggeri.
	Logger logomatic = Logger.getLogger("fi.hu.cs.titokone.gui");
	Handler[] foo2 = logomatic.getHandlers();
	if(foo2.length == 0) 
	    System.out.println("Ei oo logomaticilla " +
			       "oletushandleria!"); // Eikä ookaan.
	
	// Jos tämän asettaa falseksi, logomatic ei lähetä viestejä
	// ylävirtaan emolleen logtopusille, eikä niitä julkaista.
	// logomatic.setUseParentHandlers(false);
	logomatic.setLevel(null); // Peri vanhemmaltas.
	logomatic.severe("HUI KAMALA!");
	logomatic.warning("Varoitus!");
	logomatic.info("Informatiivinen viesti.");
	logomatic.fine("Turha viesti.");
	logtopus.setLevel(Level.FINE); // Vaihdetaan emon leveliä.
	logomatic.info("Tietosisältöinen viesti.");
	logomatic.fine("Turhampi viesti.");
	logomatic.setLevel(Level.SEVERE); // Vaihdetaan lapsen leveliä.
	logomatic.severe("KAI HUMALA!");
	logomatic.warning("Warning warning.");

    }
}
