
package oebl;

import exceptions.SimulateurException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;


public class OneEventByLineJUnitTest {
    
    //Configurateur configurateur ;
    //Simulateur simulateur ;
    
    public OneEventByLineJUnitTest() throws SimulateurException {
	//configurateur = new Configurateur("test/fichier_config/ressources/fichier_config.txt") ;
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /*@Test
    public void lireFormatOneEventByLineFormatCorrect () throws FichierIntrouvableException, LireDonneesException, EntreeSortieException {
	new Simulateur(configurateur, "test/one_event_by_line/ressources/OneEventByLineFormatCorrect.txt").lireFormatOneEventByLine();
	Util.execCommande(new String[]{"cat", "cat","test/one_event_by_line/ressources/fichier_tabulaire.csv"});
    }
    
    @Test(expected = OneEventByLineFichierIntrouvableException.class)
    public void lireFormatOneEventByLineFichierIntrouvableException () throws FichierIntrouvableException, LireDonneesException, EntreeSortieException {
	new Simulateur(configurateur, "test/one_event_by_line/ressources/OneEventByLineFichierIntrouvableException.txt").lireFormatOneEventByLine();
	Util.execCommande(new String[]{"cat", "cat","test/one_event_by_line/ressources/fichier_tabulaire.csv"});
    }
    
    @Test(expected = OneEventByLineFormatException.class)
    public void lireFormatOneEventByLineFormaException () throws FichierIntrouvableException, LireDonneesException, EntreeSortieException {
	new Simulateur(configurateur, "test/one_event_by_line/ressources/OneEventByLineFormatException.txt").lireFormatOneEventByLine();
	Util.execCommande(new String[]{"cat","test/one_event_by_line/ressources/fichier_tabulaire.csv"});
    }*/
    
}
