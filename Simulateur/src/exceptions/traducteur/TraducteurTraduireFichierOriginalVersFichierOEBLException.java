/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions.traducteur;

import exceptions.EntreeSortieException;

/**
 *
 * @author bleuzeb
 */
public class TraducteurTraduireFichierOriginalVersFichierOEBLException extends EntreeSortieException {
    public TraducteurTraduireFichierOriginalVersFichierOEBLException(String nomFichierOriginal, String nomFichierOEBL) {
	super("Une erreur de lecure/écriture lors de la traduction des données est apparue dans le fichier " + nomFichierOriginal + " ou " + nomFichierOEBL + ".") ;
	codeErreur = 14 ;
    }
}
