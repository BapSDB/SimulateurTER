/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions.oebl;

import exceptions.SimulateurException;

/**
 *
 * @author bap
 */
public class OEBLEcrireDonneesFormatCSVException extends SimulateurException {
    public OEBLEcrireDonneesFormatCSVException(String nomFichierOEBL) {
	super("Une erreur est apparue lors de l'écriture des données dans le fichier \"One-Event-By-Line\" " + nomFichierOEBL);
	codeErreur = 14 ;
    };
}
