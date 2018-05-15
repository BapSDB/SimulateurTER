
package simulateur;

import exceptions.SimulateurException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStamp {
    
    private static final DateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("HH:mm:ss");
    
    private Date timestamp ;

    public TimeStamp(String timestamp) throws TimeStampParseException {
        try {
            this.timestamp = TIMESTAMP_FORMAT.parse(timestamp) ;
        } catch (ParseException ex) {
            throw new TimeStampParseException(ex.getErrorOffset());
        }
    }

    @Override
    public String toString() {
        return TIMESTAMP_FORMAT.format(timestamp) ;
    }
    
}

class TimeStampParseException extends SimulateurException {
    
    private static final int CODE_ERREUR = 12 ;
    
    public TimeStampParseException(int errorOffset) {
        super(String.valueOf(errorOffset));
    }
    
}
