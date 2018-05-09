/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package one_event_by_line;

import configurateur.Configurateur;
import exceptions.one_event_by_line.OneEventByLineFichierIntrouvableException;
import exceptions.one_event_by_line.OneEventByLineFormatException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.ExpectedSystemExit ;
import simulateur.Simulateur;
import util.Util;


public class OneEventByLineJUnitTest {
    
    Simulateur simulateur ;
    
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();
    
    public OneEventByLineJUnitTest() {
	simulateur = new Simulateur(new Configurateur("test/fichier_config/ressources/fichier_config.txt")) ;
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void lireFormatOneEventByLineFormatCorrect () throws OneEventByLineFichierIntrouvableException, OneEventByLineFormatException {
	simulateur.lireFormatOneEventByLine("test/one_event_by_line/ressources/OneEventByLineFormatCorrect.txt") ;
	Util.execCommande(new String[]{"cat", "cat","test/one_event_by_line/ressources/fichier_tabulaire.csv"});
    }
    
    @Test(expected = OneEventByLineFichierIntrouvableException.class)
    public void lireFormatOneEventByLineFichierIntrouvableException () throws OneEventByLineFichierIntrouvableException, OneEventByLineFormatException {
	simulateur.lireFormatOneEventByLine("test/one_event_by_line/ressources/OneEventByLineFichierIntrouvableException.txt") ;
	Util.execCommande(new String[]{"cat", "cat","test/one_event_by_line/ressources/fichier_tabulaire.csv"});
    }
    
    @Test(expected = OneEventByLineFormatException.class)
    public void lireFormatOneEventByLineFormaException () throws OneEventByLineFichierIntrouvableException, OneEventByLineFormatException {
	simulateur.lireFormatOneEventByLine("test/one_event_by_line/ressources/OneEventByLineFormatException.txt") ;
	Util.execCommande(new String[]{"cat","test/one_event_by_line/ressources/fichier_tabulaire.csv"});
    }
    
}
