
package util;

import exceptions.SimulateurException;
import exceptions.timestamp.TimeStampDateException;
import exceptions.timestamp.TimeStampParseException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.management.timer.Timer.ONE_HOUR;
import static javax.management.timer.Timer.ONE_MINUTE;
import static javax.management.timer.Timer.ONE_SECOND;

public class TimeStamp {
    
    public static final String FORMAT_UNSIGNED_LONG_LONG = "\\d+" ;
    public static final String FORMAT_HMS = "(([01]\\d)|(2[0-3])):[0-5]\\d:[0-5]\\d" ;
    public static final String FORMAT_DATE = "\\d{4}-\\d{2}-\\d{2}" ;
    public static final String FORMAT_DATE_HEURE = FORMAT_DATE + "_" + FORMAT_HMS ;
            
    private static final SimpleDateFormat TIMESTAMP_CONSOLE_FORMAT = new SimpleDateFormat("[HH:mm:ss] ") ;
    
    private static boolean verifierMoisLong (String [] date) {
	boolean moisLong = false ;
	
	switch(date[1]) {
	    case "01" : 
	    case "03" : 
	    case "05" :
	    case "07" :
	    case "08" :
	    case "10" :
	    case "12" :
		moisLong = true ;
	}
	
	return moisLong && date[2].compareTo("31") <= 0 ;
    }
    
    private static boolean verifierMoisCourt (String [] date) {
	boolean moisCourt = false ;
	
	switch(date[1]) {
	    case "04" : 
	    case "06" :
	    case "09" :
	    case "11" :
		moisCourt = true ;
	}
	
	return moisCourt && date[2].compareTo("30") <= 0 ;
    }
    
    private static boolean verifierAnneeBissextile (int annee) {
	return annee % 4 == 0 && annee % 100 != 0 || annee % 4 == 0 && annee % 400 == 0 ;
    }
    
    private static boolean verifierMoisFevier (String [] date) {
	boolean moisFevrier = date[1].equals("02") ;
	int jour = Integer.parseUnsignedInt(date[2]) ;
	int annee = Integer.parseUnsignedInt(date[0]) ;
	return moisFevrier && (jour < 29
		&& !verifierAnneeBissextile(annee) || jour == 29 && verifierAnneeBissextile(annee)) ;
    }
    
    private static boolean verifierDate (String [] date) throws ParseException {
	    return
		    new SimpleDateFormat("yyyy").parse(date[0]).before(new Date(System.currentTimeMillis()))
		    && (verifierMoisLong(date) || verifierMoisCourt(date) || verifierMoisFevier(date)) ;
    }
    
    public static void verifierDate (long date, String nomFichierEntree, int numLigne) throws SimulateurException {
	if(!new Date(date).before(new Date(System.currentTimeMillis())))
		    throw new TimeStampDateException(nomFichierEntree, String.valueOf(date), numLigne);
    }
    
    public static void verifierDate (String [] evenements, String nomFichierEntree, int numLigne) throws SimulateurException {
	try {
	    String [] date = evenements[0].split("-") ;
	    date[2] = date[2].substring(0, 2) ;
	    if(!TimeStamp.verifierDate(date))
		    throw new TimeStampDateException(nomFichierEntree, evenements[0], numLigne);
	} catch (ParseException ex) {
	    throw new TimeStampParseException(nomFichierEntree, evenements[0], numLigne, ex.getErrorOffset());
	}
    }
    
    public static long convertirFormatHMSVersLong (String hms) {
        String [] split = hms.split(":");
        return    Long.parseUnsignedLong(split[0])*3600
                + Long.parseUnsignedLong(split[1])*60
                + Long.parseUnsignedLong(split[2]) ;
    }
    
    public static String convertirDureeVersString (long duree) {
        String H,M,S,MS ;
        long h,m,s,ms ;
        long reste ;
        boolean bh, bm, bs ;
        h = duree / ONE_HOUR ;
        reste = duree % ONE_HOUR ;
        m = reste / ONE_MINUTE ;
        reste %= ONE_MINUTE ;
        s = reste / ONE_SECOND ;
        ms = reste % ONE_SECOND ;
        H = (bh = h > 0) ? h + " heure" + (h > 1 ? "s" : "") : "" ;
        M = (bm = bh || m > 0) ? (bh && (s > 0 || ms > 0) ? ", " : bh ? " et " : "") + m + " minute" + (m > 1 ? "s" : "") : "" ;
        S = (bs = bm || s > 0) ? (bm && ms > 0 ? ", " : bm ? " et " : "") + s + " seconde" + (s > 1 ? "s" : "") : "" ;
        MS = ms > 0 ? (bs ? " et " : "") + ms + " milliseconde" + (ms > 1 ? "s" : "") : "" ;
        return H+M+S+MS ;
    }
    
    public static String getTimestampAffichageConsole () {
        return TIMESTAMP_CONSOLE_FORMAT.format(new Date(System.currentTimeMillis())) ;
    }
    
    public static void main(String[] args) {
	try {
	    System.out.println(verifierDate(new String[]{"2003", "04", "99"})) ;
	} catch (ParseException ex) {
	    Logger.getLogger(TimeStamp.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
}