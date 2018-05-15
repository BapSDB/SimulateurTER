/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traducteur.switch2;

import exceptions.EntreeSortieException;
import exceptions.FichierIntrouvableException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import traducteur.TraducteurSwitch2;
import util.Util;

/**
 *
 * @author bleuzeb
 */
public class TraducteurSwitch2JUnitTest {
    
    TraducteurSwitch2 traducteurSwitch2 ;
    
    public TraducteurSwitch2JUnitTest() {
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
    public void traduireFichierOriginalVersFichierOEBLCorrect () throws FichierIntrouvableException, EntreeSortieException {
	traducteurSwitch2 = new TraducteurSwitch2("src/ressources/Switch2.switch") ;
	traducteurSwitch2.traduireFormatOriginalVersFormatOEBL();
	Util.execCommande(new String[]{"cat", traducteurSwitch2.getNomFichierOEBL()});
    }
}
