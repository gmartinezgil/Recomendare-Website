package mx.com.recomendare.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import mx.com.recomendare.web.commons.models.LocationModel;

public final class Util {
    private static final DateFormat MX_DATE_FORMAT = DateFormat.getDateInstance(DateFormat.DEFAULT, new Locale("es", "MX"));
    //private static final NumberFormat MX_CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));

    /**
     * 
     */
    public static String getRandomUserKey() {
    	Random random = new Random();
		return String.valueOf(random.nextInt(100000000));
	}
    
    /**
     * 
     * @param numberOfStars
     * @return
     */
    public static String getStarsFromRating(final int numberOfStars) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < numberOfStars; i++) {
            sb.append('*');
        }
        return sb.toString();
    }

    /**
     * 
     * @param fileType
     * @return
     */
    public static boolean isPictureFileFormatSupported(final String fileType) {
        if (fileType != null) {
            if (fileType.equals("image/gif") || fileType.equals("image/jpeg") || fileType.equals("image/png")) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] getContentAsBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
            throw new IOException("File is too big!!!");
        }

        // create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        //close the input stream and return bytes
        is.close();
        return bytes;
    }

    /**
     * 
     * @param dateValue
     * @param location
     * @return
     */
    public static String getDateFormat(final Date dateValue, final LocationModel location) {
    	if(location != null)  {
    		String languageCode = location.getLanguageCode();
    		String countryCode = location.getCountryCode();
    		return DateFormat.getDateInstance(DateFormat.DEFAULT, new Locale(languageCode, countryCode)).format(dateValue);
    	}
        return MX_DATE_FORMAT.format(dateValue);
    }

    /**
     * 
     * @param currencyValue
     * @param location
     * @return
     */
    public static String getCurrencyFormat(final double currencyValue, final String languageCode, final String countryCode) {
		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale(languageCode, countryCode));
		Currency currency = nf.getCurrency();
		return nf.format(currencyValue) + " " +currency.getCurrencyCode();
    }
    
    /**
     * 
     * @param timeUpdated
     * @param timeInMiliseconds
     * @return
     */
    public static boolean hasPassedTime(final Date timeUpdated, final long timeInMiliseconds) {
        return (new Date().getTime() - timeUpdated.getTime()) >= timeInMiliseconds;
    }

    /**
     * 
     * @param currentDate
     * @param timeZoneId
     * @return
     */
    public static Date getDateInTimeZone(Date currentDate, String timeZoneId) {
    	if(timeZoneId != null && timeZoneId.trim().length() > 0)  {
    		TimeZone tz = TimeZone.getTimeZone(timeZoneId);
        	Calendar mbCal = new GregorianCalendar(tz);
        	mbCal.setTimeInMillis(currentDate.getTime());

        	Calendar cal = Calendar.getInstance();
        	cal.set(Calendar.YEAR, mbCal.get(Calendar.YEAR));
        	cal.set(Calendar.MONTH, mbCal.get(Calendar.MONTH));
        	cal.set(Calendar.DAY_OF_MONTH, mbCal.get(Calendar.DAY_OF_MONTH));
        	cal.set(Calendar.HOUR_OF_DAY, mbCal.get(Calendar.HOUR_OF_DAY));
        	cal.set(Calendar.MINUTE, mbCal.get(Calendar.MINUTE));
        	cal.set(Calendar.SECOND, mbCal.get(Calendar.SECOND));
        	cal.set(Calendar.MILLISECOND, mbCal.get(Calendar.MILLISECOND));
        	return cal.getTime();
    	}
    	else  {
    		return currentDate;
    	}
    }

    /**
     * 
     * @param latitude1
     * @param longitude1
     * @param latitude2
     * @param longitude2
     * @return
     */
    public static double calculateDistanceBetweenTwoPoints(final float latitude1, final float longitude1, final float latitude2, final float longitude2) {
        double theta = longitude1 - longitude2;
        double dist = Math.sin(deg2rad(latitude1)) * Math.sin(deg2rad(latitude2)) + Math.cos(deg2rad(latitude1)) * Math.cos(deg2rad(latitude2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609344;//the last parameter is for kilometers...instead of miles...
        return dist;
    }

    /**
     * Converts decimal degrees to radians
     * 
     * @param deg
     * @return
     */
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /**
     * Converts radians to decimal degrees
     * 
     * @param rad
     * @return
     */
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}//END OF FILE