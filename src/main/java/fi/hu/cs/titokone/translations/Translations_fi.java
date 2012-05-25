/* Copyright 2004 University of Helsinki, Department of Computer
   Science. See license.txt for details. */
    
package fi.hu.cs.titokone.translations;

import java.util.ListResourceBundle;

/** This translation is based on Translations.java. Look there for
    details. */
public class Translations_fi extends ListResourceBundle {
   public Object[][] getContents() { 
     return contents;
   }
     
   protected static final Object[][] contents = {
      // Localize below, pairs of key-value (what key is in your language)...
      // Remove lines which you do not wish to translate completely. Leaving
      // in a value of "" will translate the message to "" as well.

      // Class: Animator 
      
      {"R0","R0"},
      {"R1","R1"},
      {"R2","R2"},
      {"R3","R3"},
      {"R4","R4"},
      {"R5","R5"},
      {"R6","R6"},
      {"R7","R7"},
      {"TR","TR"},
      {"PC","PC"},
      {"IR","IR"},
      {"SR","SR"},
      {"BASE","BASE"},
      {"LIMIT","LIMIT"},
      {"MAR","MAR"},
      {"MBR","MBR"},
      {"IN1","IN1"},
      {"IN2","IN2"},
      {"OUT","OUT"},
      { "Registers", "Rekisterit"},
      { "Control unit", "Kontrolliyksikkö"},
      { "MMU", "MMU"},
      { "ALU", "ALU"},
      { "External device", "Ulkoinen laite"},
      { "Memory", "Muisti"},
      
      { "Current command: ", "Suoritettava komento: "},
      { "Fetch the next instruction from memory slot {0} to IR and " +
        "increase PC by one.", "Nouda seuraava käsky muistipaikasta {0} IR:iin ja kasvata PC:n arvoa yhdellä"},
      { "No-operation command completed.", "NOP-käsky suoritettu"},
      { "Fetch second operand from memory slot {0}.", "Nouda jälkimmäinen operandi muistipaikasta {0}."},
      { "Indirect memory addressing mode.", "Epäsuora muistiosoitus."},
      { "1: Fetch indexing value from memory slot {0}.", "1: Nouda indeksointiarvo muistipaikasta {0}."}, 
      { "2: Fetch second operand from memory slot {0}.", "2: Nouda jälkimmäinen operandi muistipaikasta {0}."},
      { "Write value {0} from register R{1} to memory slot {2}.", "Kirjoita rekisterin R{1} arvo {0} muistipaikkaan {2}."}, 
      { "Load value {0} to register R{1}.", "Lataa arvo {0} rekisteriin R{1}"},
      { "Read value {0} from {1} to register R{2}.", "Lue {1} arvo {0} rekisteriin R{2}."},
      { "Write value {0} from register R{1} to {2}.", "Kirjoita rekisterin R{1} arvo {0} {2}."},
      { "Copy register R{0} to ALU IN1.", "Kopioi rekisterin R{0} sisältö ALU:n paikkaan IN1."},
      { "Copy second operand to ALU IN2.", "Kopioi jälkimmäinen operandi ALU:n paikkaan IN2."},
      { "ALU computes the result.", "ALU laskee tuloksen."},
      { "Copy ALU result to register R{0}", "Kopioi ALU:n laskema tulos rekisteriin R{0}."},
      { "ALU computes the comparison result.", "ALU laskee vertailun tuloksen."}, 
      { "0=greater, 1=equal, 2=less", "0=suurempi kuin, 1=yhtä suuri, 2=vähemmän kuin"},
      { "Set comparison result to SR", "Aseta vertailun tulos tilarekisteriin."},
      { "Branching command - branching condition is false, so do nothing.", 
        "Haarautumiskäskyn ehto on epätosi, joten ei tehdä mitään."},
      { "Branching command - branching condition is true, so update PC.",
        "Haarautumiskäskyn ehto on tosi, PC:n arvo päivitetään"},
      { "Save new PC to TR", "Talleta uusi PC:n arvo TR:iin"},
      { "Increase stack pointer R{0} by one and push PC to stack.", "Kasvata pinorekisterin R{0} arvoa yhdellä ja vie PC:n arvo pinoon."},
      { "Increase stack pointer R{0} by one and push FP to stack.", "Kasvata pinorekisterin R{0} arvoa yhdellä ja vie FP:n arvo pinoon."},
      { "Copy stack pointer to FP.", "Kopioi pino-osoittimen arvo FP:n arvoksi."},
      { "Update PC.", "Päivitä PC:n arvo."},
      { "Pop PC from stack and decrease stack pointer R{0} by one.", 
        "Tuo PC:n arvo pinosta ja vähennä pino-osoittimen R{0} arvoa yhdellä."},
      { "Pop FP from stack and decrease stack pointer R{0} by one.", 
        "Tuo FP:n arvo pinosta ja vähennä pino-osoittimen R{0} arvoa yhdellä."},
      { "Decrease {0} parameters from stack pointer R{1}.", 
        "Vähennä arvo {0} pino-osoittimen R{1} arvosta."},
      { "Increase stack pointer R{0} by one, then write second operand to " +
        "stack", "Kasvata pino-osoittimen R{0} arvoa yhdellä ja kirjoita jälkimmäisen operandin arvo pinoon."},
      { "Read value from stack to R{0}, then decrease stack pointer R{1} " +
        "by one.", "Lue arvo pinosta rekisteriin R{0} ja vähennä pino-osoittimen R{1} arvoa yhdellä."},
      {"Read value from stack to R{0} then decrease stack pointer R{1} by one.", "Lue arvo pinosta rekisteriin R{0} ja vähennä pino-osoittimen R{1} arvoa yhdellä."},
      { "Supervisor call to operating system's services.", 
"Palvelupyyntö käyttöjärjestelmälle." },      
      
      // Class: Application.
      // General messages: (none)
      // Exception messages:
      { "No more keyboard data stored on application." , 
        "Sovellukseen ei ole talletettu enempää näppäimistödataa." },
      { "No more stdin data stored on application.", 
	"Sovellukseen ei ole talletettu enempää stdin-dataa." },
      {	"Keyboard input string \"{0}\" invalid, should be eg. \\n-separated " +
	"list of integers." , 
	"Näppäimistösyöte \"{0}\" ei kelpaa; sen tulisi olla esimerkiksi " +
	"\\n-merkein eroteltu kokonaislukulista."},
      { "Stdin input string \"{0}\" invalid, should be eg. \\n-separated " +
	"list of integers.", 
	"Stdin-syöte \"{0}\" ei kelpaa; sen tulisi olla esimerkiksi " +
	"\\n-merkein eroteltu kokonaislukulista."},
      // Log messages:
      { "Application has no more keyboard data, read: {0}, buffer length " +
	"{1}.", 
	"Sovelluksessa ei ole enää näppäimistödataa; luettu {0}, puskurin " +
	"pituus {1}."},
      { "Application has no more stdin data, read: {0}, buffer length {1}.", 
	"Sovelluksessa ei ole enää stdin-dataa; luettu {0}, puskurin " +
	"pituus {1}." },
      { "Accepted \"{0}\" as keyboard input, tokens found: {1}.", 
	"\"{0}\" hyväksytty näppäinsyötteeksi, {1} syöteriviä eritelty." },
      { "Accepted \"{0}\" as stdin input, tokens found: {1}.", 
	"\"{0}\" hyväksytty stdin-syötteeksi, {1} syöteriviä eritelty."}, 

      // Class: BinaryInterpreter, no messages.

      // Class: Binary
      {"___b91___ is missing.", "___b91___ puuttuu."},
      {"___code___ is missing.", "___code___ puuttuu."},
      {"Invalid code area value on line: {0}", 
       "Koodialueen alkuarvo rivillä {0} ei kelpaa."},
      {"Invalid code area length on line: {0}", 
       "Koodialueen pituus rivillä {0} ei kelpaa."},
      {"Invalid command on line: {0}", 
       "Epäkelpo komento rivillä {0}."},
      {"Invalid number of code lines.", 
       "Koodirivien määrä ei kelpaa."},
      {"___data___ is missing.", "___data___ puuttuu."},
      {"Invalid data area value on line: {0}", 
       "Data-alueen alkuarvo rivillä {0} ei kelpaa."},
      {"Invalid data area length on line: {0}", 
       "Data-alueen pituus rivillä {0} ei kelpaa."},
      {"Invalid data on line: {0}", 
       "Data rivillä {0} ei kelpaa."},
      {"___symboltable___ is missing.", 
       "___symboltable___ puuttuu."},
      {"Invalid symbol on line: {0}", 
       "Epäkelpo symbolinimi rivillä {0}."},
      {"Invalid symbol value on line: {0}", 
       "Epäkelpo symbolin arvo rivillä {0}."},
      {"___end___ is missing.", "___end___ puuttuu."},
      {"Lines after ___end___", "___end___-merkin jälkeen on vielä rivejä."},
      
      // Class: CompileDebugger, no messages.
      // Class: CompileInfo, no messages.

      // Class: Compiler				// line (approx)
      {"Compilation is not finished yet.", 
       "Käännös ei ole vielä valmistunut."},	        // 203
      {"Invalid command.", "Epäkelpo komento."},	// 255
      {"Invalid label.", "Epäkelpo muistiviite."},     // 271, 328, 345 and 349
      {"Found label {0} and variable {1}.", 
       "Löydettiin muistiviite {0} ja muuttuja {1}."},	// 323
      {"Variable {0} used.", "Muuttujaa {0} käytettiin."},// 328
      {"Label {0} found.", "Löydettiin muistiviite {0}."}, // 333
      {"Invalid size for a DS.", "Epäkelpo DS:n koko."},// 358 and 362
      {"Invalid value for a DC.", "Epäkelpo DC:n arvo."},// 373
      {"Variable {0} defined as {1}.", 
       "Muuttuja {0} samaistettu arvoon {1}."},		// 401
      {"Found variable {0}.", "Löydettiin muuttuja {0}."}, // 419 and 436
      {"{0} defined as {1}.", 
       "Asetukselle {0} määriteltiin arvoksi {1}."},	// 449
      {"Invalid DEF operation.", "Epäkelpo DEF-komento."}, // 454
      {"{0} --> {1} ({2}) ", "{0} --> {1} ({2})"}, // symb --> bin (:-sep. bin); 650
      //ADDED 26.4. OLLi
      {"Missing referred label {0}.", "Käytetty muistiviite {0} puuttuu."}, //662
      

      {"Compilation failed: {0}", "Käännös epäonnistui: {0}"},         //  767
      {"invalid label {0}.", "epäkelpo muistiviite {0}."},       //  768
      {"invalid opcode {0}.", "epäkelpo komento {0}."},             //  781, 787, 1057
      {"second register expected.", "jälkimmäinen rekisteri on virheellinen."},       //  836, 877, 886
      {"invalid register {0}.", "epäkelpo rekisteri {0}."},           //  859
      {"end of line expected.", "rivi päättyy virheellisesti."},           //  908
      {"first register expected.","ensimmäinen rekisteri on virheellinen."},        //  920, 948
      {"address expected.", "osoiteosa on virheellinen."},              //  927, 968, 974
      {"invalid argument.", "epäkelpo argumentti."},               	//  939
      {"invalid address value.", "virheellinen osoiteosa."}, 		// 953
      {"address or register expected.", "osoiteosa tai rekisteri on virheellinen."},   //  956
      {"invalid addressing mode {0}.", "epäkelpo muistinoudon tyyppi {0}."},    //  980
      {"opcode missing.", "komento puuttuu."},                 //  991
      {"invalid value {0}.", "arvo {0} on virheellinen."},              // 1072, 1088
      {"invalid value for a DS {0}.", "virheellinen DS:n arvo: {0}."},     // 1082
      {"value expected.", "arvo puuttuu."},                // 1097, 1104
      
      
      // Class: Control
      {"No default STDIN file set.", "Oletus-STDIN-tiedostoa ei asetettu."},
      {"No default STDOUT file set.", "Oletus-STDOUT-tiedostoa ei asetettu."},
      {"No application to load.", "Ladattava sovellus puuttuu."},
      {"STDIN data file unreadable: {0}", 
       "STDIN-datatiedostoa ei voi lukea: {0}"}, 
      {"STDIN data file contains invalid data: {0}", 
       "STDIN-datatiedoston sisältö ei kelpaa: {0}"},
      {"STDIN files were null, data not inserted to the application.", 
      "STDIN-tiedostot olivat arvoltaan null, joten sovellukseen ei " +
       "liitetty tiedostodataa."},
      {"Application contained an odd definition key '{0}'.", 
      "Sovelluksessa oli määritelty tuntematon asetus '{0}'."},
      {"Trying to run an unsupported type of application. (The application " +
       "must be created using the same program.)", 
       "Tämä sovellus on epäyhteensopiva eikä sitä voi ajaa. Sovellus pitää " +
       "luoda samalla ohjelmalla kuin se ajetaan."},
      {"Cannot form a binary out of an unsupported type of an application. " +
       "(The application must be created using the same program.)", 
       "Tämä sovellus on epäyhteensopiva eikä siitä voi muodostaa " +
       "binääritiedostoa. Sovellus pitää luoda samalla ohjelmalla kuin " +
       "joka sitä tulkitsee binääritiedostoksi."},
      {"There is no application available to run from!", 
       "Vain ajettava sovellus puuttuu!.."},
      {"No STDOUT file set, not writing to it.", 
       "STDOUT-tiedostoa ei ole asetettu, joten siihen ei juuri kirjoiteta."},
      {"Memory size must be between 2^9 and 2^16, a change to 2^{0} failed.", 
       "Muistin koon tulee olla väliltä 2^9 - 2^16. Muutos arvoon 2^{0} " +
       "epäonnistui."},
      {"StdIn file contents are invalid; the file should contain only " +
       "integers and separators.", 
       "StdIn-tiedoston sisältö ei kelpaa; tiedoston tulisi sisältää vain " +
       "kokonaislukuja ja erotinmerkkejä."},
       {"Modified source was null.", "Muokatuksi lähdekoodiksi tarjottiin " +
	"null-arvoa."},
      {"No source file set, use openSource first.", 
       "Lähdekooditiedostoa ei ole asetettu; kutsu openSource-metodia ensin."},
      {"Cannot deduce the file to store the binary into; no source " +
       "file has been loaded.", 
       "Binäärin tallennustiedoston nimeä ei voi päätellä " +
       "lähdekooditiedostosta, jota ei ole avattu. Vaikeuksien vaikeus."},
      {"Cannot save binary to file; no application has been compiled or " +
       "loaded.", "Sovellusta ei ole käännetty eikä avattu, joten sen " +
       "binääriä ei voi tallentaa tiedostoon."},

      // Class: DebugInfo, no messages.

      // Class: FileHandler
      {"{0} in loadResourceBundle(): {1}", "Poikkeus {0} " +
       "loadResourceBundle()-metodissa: {1}"},
      {"No read access to {0}.", "Lukuoikeus tiedostoon {0} uupuu."},
      {"No write access to {0}.", "Kirjoitusoikeus tiedostoon {0} uupuu."},
      
      // Class: GUI 
      {"File", "Tiedosto"},
      {"Open", "Avaa"},
      {"Compile", "Käännä"},
      {"Run", "Aja"},
      {"Continue", "Jatka"},
      {"Continue without pauses", "Jatka tauoitta"},
      {"Stop", "Pysäytä"},
      {"Erase memory", "Tyhjennä muisti"},
      {"Exit", "Poistu ohjelmasta"},
      {"Options", "Asetukset"},
      {"Set memory size", "Aseta muistin koko"},
      {"Help", "Apua"},
      {"Manual", "Ohjeet"},
      {"About", "Tietoja ohjelmasta"},
      {"Set compiling options", "Muuta käännösasetuksia"},
      {"Set running options", "Muuta suoritusasetuksia"},
      {"Configure file system", "Aseta tiedostojärjestelmää"},
      {"Select default stdin file", "Valitse oletus-stdin-tiedosto"},
      {"Select default stdout file", "Valitse oletus-stdout-tiedosto"},
      {"Set language", "Aseta kieli"},
      {"Select from a file...", "Valitse tiedostosta..."},

      {"Line", "Rivi"},   
      {"Numeric value", "Numeromuoto"},    
      {"Symbolic content", "Symbolinen muoto"},

      {"Open a new file", "Avaa uusi tiedosto"},
      {"Compile the opened file", "Käännä avattu tiedosto"},
      {"Run the loaded program", "Suorita ladattu ohjelma"}, 
      {"Continue current operation", "Jatka nykyoperaatiota"},
      {"Continue current operation without pauses", 
       "Jatka nykyoperaatiota tauotta"},
      {"Stop current operation", "Keskeytä nykyoperaatio"},
      {"Enable/disable animated execution", "Animoitu suoritus päälle/pois"},
      {"Show/hide video graphics display", "Grafiikkatila näytä/piilota"},
      {"Enable/disable extra comments while execution", "Suorituksen ylimääräinen kommentointi päälle/pois"},
      {"Enable/disable line by line execution", "Riveittäin suoritus päälle/pois"},
      {"en/about.html", "fi/about_fi.html"},
      {"en/manual.html", "fi/manual_fi.html"},
      {"__ABOUT_FILENAME__", "fi/about_fi.html"},
      {"__MANUAL_FILENAME__", "fi/manual_fi.html"},
      
      {"Enter", "Syötä"}, // button used to enter a number to the KBD device.
      {"Symbol table", "Symbolitaulu"}, 
      {"Registers", "Rekisterit"}, 
      
      // Class: GUIBrain 
      {"Main path not found! (Trying to locate etc/settings.cfg.) " +
       "...exiting.",        //exception opening etc/settings.cfg
       "Asennuspolku ei löytynyt! (Yritettiin paikantaa " +
       "etc/settings.cfg:tä.) ...poistun."}, 
      {"I/O error while reading settings file: {0}", 
       "I/O-virhe asetustiedostoa luettaessa: {0}"}, 
      {"Parse error in settings file.", 
       "Jäsennysvirhe asetustiedostoa luettaessa."},
      {"Titokone out of memory: {0}", "Titokoneesta loppui muisti: {0}"},
      {"Opened a new k91 source file.", 
       "Avattiin uusi k91-lähdekooditiedosto."},
      {"File extension must be k91 or b91", "Tiedostopäätteen tulee " +
       "olla k91 tai b91."}, 
      {"Illegal input", "Syöte ei kelpaa"},   
      {"Illegal input. You must insert a number between {0}...{1}", 
      "Syöte ei kelpaa. Sen tulee olla luku väliltä {0}..{1}."},
      {"Error", "Virhe"},  
      {"Enter a number in the text field above.", 
       "Syötä luku ylläolevaan tekstikenttään."},
      {"Not a language file", "Kielitiedoston lataaminen epäonnistui."},    
      {"Cannot overwrite {0}", "Tiedostoa {0} ei voi ylikirjoittaa."},   
      {"Default stdin file set to {0}", 
       "Oletus-stdin-tiedosto on nyt {0}"},    
      {"Default stdout file set to {0}", 
       "Oletus-stdout-tiedosto on nyt {0}."},    
      {"Error while emptying {0}", 
       "Virhe tyhjättäessä tiedostoa {0}."},    
      {"Overwrite?", "Ylikirjoitetaanko?"},    
      {"Do you want to overwrite the file? Select {1} to append or {0} " +
       "to overwrite.", "Haluatko ylikirjoittaa tiedoston? Valitse {1} " +
       "liittämiseksi, tai {0} tiedoston ylikirjoittamiseksi."},
      {"Waiting for KBD input...", "Odotetaan KBD-syötettä..."},
      {"Execution aborted due to an error", "Suoritus keskeytyi virhetilanteen vuoksi"},
      {"Compilation aborted due to an error", "Kääntäminen keskeytyi virhetilanteen vuoksi"},
      {"Current operation aborted", "Meneillään ollut operaatio keskeytettiin"},
      {"B91 binary", "B91-binääri"},   
      {"K91 source", "K91-lähdekooditiedosto"},  
      {"Class file", "Luokkatiedosto"},
      {"Memory contents erased", "Muistin sisältö nollattu"},
      {"Execution finished", "Suoritus päättyi."},
      {"Compilation finished", "Käännös päättyi."},

      // Class: GUICompileSettingsDialog
      // (Set compiling options already defined in GUI.)
      {"Apply", "Muuta"},
      {"Close", "Sulje"},
      {"Compile line by line", "Käännä rivittäin"},
      {"Show comments", "Näytä kommentit"},
      {"Set compiling options", "Muuta käännösasetuksia"},
      // Pause whenever a comment occurs & show extra comments while compiling
      // represented in JOptionPane.

      // Class: GUIRunSettingsDialog
      // (Set running options already defined in GUI, as are Apply and Close.)
      {"Execute line by line", "Suorita rivittäin"},
      {"Show comments", "Näytä kommentit"},
      {"Show animation", "Näytä animointi"},
      // (execute code line by line, show extra comments while executing
      // and show animation while executing are represented in JOptionPane.

      // Class: GUIThreader, no messages.
      // Class: Interpreter, no messages.
      // Class: InvalidDefinitionException, no messages.
      // Class: InvalidSymbolException, no messages.

      // Class: JFileChooser    
      // Open already represented in GUI.     
      {"Cancel", "Peruuta"},    
      {"Look in:", "Työhakemisto:"},    
      {"File name:", "Tiedoston nimi:"},    
      {"Files of type:", "Näytä tiedostot tyyppiä:"},    
      {"Up one level", "Ylähakemistoon"},    
      {"Up", "Ylös"},    
      {"Desktop", "Työpöytä"},    
      {"Create new folder", "Luo uusi hakemisto"},    
      {"New folder", "Uusi hakemisto"},    
      {"List", "Lista"},    
      {"Details", "Yksityiskohtaiset tiedot"},    
      {"All files", "Kaikki tiedostot"},    
      {"Abort file selection", "Peruuta tiedoston valinta"},  
      {"Open the selected file", "Avaa valittu tiedosto"},
      
      // Class: JOptionPane    
      {"Yes", "Kyllä"},    
      {"No", "Ei"},
      {"Pause whenever a comment occurs" , 
       "Pysäytä kääntäminen kommentin ilmentyessä."},
      {"Show extra comments while compiling", 
       "Näytä lisäkommentteja kääntämisen kulusta."},
      {"Execute code line by line", 
       "Suorita koodia rivi kerrallaan."},
      {"Show extra comments while executing", 
       "Näytä lisäkommentteja suorituksen kulusta"},
      {"Show animation while executing", "Näytä animointi suorittaessa"},
      {"Apply" , "Aseta"},  
      {"Close" , "Sulje"}, 
      
      // Class: JTableX, no messages.

      // Class: Loader
      {"Null is an invalid parameter, instance of {0} required.", 
       "Null ei kelpaa parametriksi, vaaditaan luokan {0} ilmentymä."},
      {"Program loaded into memory. FP set to {0} and SP to {1}.", 
       "Ohjelma ladattu muistiin. FP asetettu {0}:ksi ja SP {1}:ksi."},
      
      // Class: LoadInfo, no messages.
      // Class: MemoryLine, no messages.
      // Class: Message, no messages. (Surprising, huh?)
      
      // Class: Processor
      {"Invalid operation code {0}", "Epäkelpo operaatiokoodi {0}"},
      {"Memory address out of bounds", 
       "Muistiosoite osoittaa muistin ulkopuolelle"},
      {"Invalid memory addressing mode", "Epäkelpo muistinosoitusmoodi"},
      {"Invalid memory access mode in branching command", 
       "Epäkelpo muistinosoitusmoodi haarautumiskäskyssä"},
      {"Invalid memory access mode in STORE", 
       "Epäkelpo muistinosoitusmoodi STORE-käskylle"},
      {"No keyboard data available", "Näppäimistödataa ei ole saatavilla"},
      {"No standard input data available", 
       "STDIN-levydataa ei ole saatavilla"},
      {"Invalid device number", "Epäkelpo laitenumero"},
      {"Integer overflow", "Kokonaisluvun ylivuoto"},
      {"Division by zero", "Nollalla jako"},
      {"Row number {0} is beyond memory limits.", 
       "Rivinumero {0} ylittää muistialueen rajat."}, // in memoryInput
      
      // Class: RandomAccessMemory
      {"Memory size cannot be negative.", 
       "Muistin koko ei voi olla negatiivinen."},
      {"Tried to set symbol table to null.", 
       "Symbolitaulua yritettiin asettaa null-arvoksi."}, 
      {"Trying to load a null memory line.", 
       "Yritettiin ladata null-muistirivi." },
      {"Address {0} too large, memory size {1} (indexing starts at 0).",
       "Osoite {0} on liian suuri, muistin koko on {1} (indeksointi " +
       "alkaa nollasta)."},
      {"Address {0} below zero.", "Osoite {0} on alle nollan."},
      {"Code area size cannot be negative.", 
       "Koodialueen koko ei voi olla negatiivinen."},
      {"Code area size cannot be bigger than the size of the whole memory.", 
       "Koodialueen koko ei voi ylittää muistin kokoa."},
      {"Data area size cannot be negative.", 
       "Data-alueen koko ei voi olla negatiivinen."},
      {"Data area size cannot be bigger than the size of the whole memory.",
       "Data-alueen koko ei voi ylittää muistin kokoa."},
      
      // Class: Registers
      {"Unknown registerId: {0}", "Tuntematon rekisteritunnus: {0}"},
      {"Unknown registerName: {0}", "Tuntematon rekisterin nimi: {0}"},
      
      // Class: ResourceLoadFailedException, no messages.
      // Class: RunDebugger, comments
      {"{0}{1} Indexing {2}, direct.", "{0}{1} indeksointi {2}, välitön"},
      {"{0}{1} Indexing {2}, direct addressing.", 
       "{0}{1} indeksointi {2}, suora muistiosoitus."},
      {"{0}{1} Indexing {2}, indirect addressing.", 
       "{0}{1} indeksointi {2}, epäsuora muistiosoitus."},
      // RunDebugger, external devices
      {"keyboard", "näppäimistöltä"},
      {"stdin","tiedostosta"},
      {"stdout","tiedostoon"},
      {"display","näytölle"},
       
       
      // Comment with value (=KBD, =CRT =STDIN =STDOUT)
      {"{0}{1} Indexing {2}, direct, value {3}.", 
       "{0}{1} indeksointi {2}, välitön, arvo {3}."},
      // Are these two needed?
      {"{0}{1} Indexing {2}, direct addressing, value {3}.", 
       "{0}{1} indeksointi {2}, suora muistiosoitus, arvo {3}."},
      {"{0}{1} Indexing {2}, indirect addressing {3}.", 
       "{0}{1} indeksointi {2}, epäsuora muistiosoitus {3}."},
      
      // Class: RunInfo, no messages.
      
      // Class: Settings.
      // General messages: (none)
      // Exception messages: 
      { "value", "arvo" },
      { "a linebreak", "rivinvaihdon" },
      { "Illegal {0} \"{1}\", contains {2}.", "Epäkelpo {0} \"{1}\", " +
	"sisältää {2}." },
      { "Illegal {0}: null. Try an empty string instead.", 
	"Epäkelpo {0}: null. Kokeile mieluummin tyhjää merkkijonoa."},
      { "Syntax error on line {0}, which was: \"{1}\".", 
	"Syntaksivirhe rivillä {0}, sisältö \"{1}\"."},
      // Log messages: 
      { "Settings successfully parsed, lines: {0}, unique keys found: {1}.", 
	"{0} riviä asetuksia jäsennetty onnistuneesti, yksilöllisiä " +
	"tunnisteita löytyi {1}."},

      // Class: Source, no messages.
      // Class: SymbolicInterpreter, no messages.
      
      // Class: SymbolTable
      {"SymbolName was null.", "SymbolName oli arvoltaan null."},
      {"Definition key was null.", "Asetuksen tunniste oli null."},
      {"Definition {0} not found.", "Asetusta {0} ei löytynyt."}

      // Class: Translator: no translateable strings set to avoid 
      // looping bugs.
      

      // Localizable bit ends. 
  };
}
