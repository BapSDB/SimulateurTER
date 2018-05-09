/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package one_event_by_line;

import configurateur.Configurateur;
import exceptions.OneEventByLineFichierIntrouvableException;
import exceptions.OneEventByLineFormatException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import simulateur.Simulateur;
import util.Util;

/**
 *
 * @author bleuzeb
 */
public class OneEventByLineJUnitTest {
    
    Simulateur simulateur ;
    
    public OneEventByLineJUnitTest() {
	simulateur = new Simulateur(new Configurateur()) ;
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
	Util.execCommande(new String[]{"cat", "test/one_event_by_line/ressources/OneEventByLineFormatCorrect.txt"});
    }
    
    @Test(expected = OneEventByLineFichierIntrouvableException.class)
    public void lireFormatOneEventByLineFichierIntrouvableException () throws OneEventByLineFichierIntrouvableException, OneEventByLineFormatException {
	simulateur.lireFormatOneEventByLine("test/one_event_by_line/ressources/OneEventByLineFichierIntrouvableException.txt") ;
	Util.execCommande(new String[]{"cat", "test/one_event_by_line/ressources/OneEventByLineFichierIntrouvableException.txt"});
    }
    
    @Test(expected = OneEventByLineFormatException.class)
    public void lireFormatOneEventByLineFormaException () throws OneEventByLineFichierIntrouvableException, OneEventByLineFormatException {
	simulateur.lireFormatOneEventByLine("test/one_event_by_line/ressources/OneEventByLineFormatException.txt") ;
	Util.execCommande(new String[]{"cat", "test/one_event_by_line/ressources/OneEventByLineFormatException.txt"});
    }
    
}
