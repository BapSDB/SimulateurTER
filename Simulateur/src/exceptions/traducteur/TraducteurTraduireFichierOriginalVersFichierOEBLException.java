/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions.traducteur;

import exceptions.SimulateurException;

/**
 *
 * @author bleuzeb
 */
public class TraducteurTraduireFichierOriginalVersFichierOEBLException extends SimulateurException {
    public TraducteurTraduireFichierOriginalVersFichierOEBLException(String nomFichierOriginal, String nomFichierOEBL, String nomFichierConfig) {
	super("Une erreur de lecure/écriture lors de la traduction des données est apparue dans le fichier " + nomFichierOriginal + " ou " + nomFichierOEBL + " ou " + nomFichierConfig);
	codeErreur = 5 ;
    }
}
