import java.util.Calendar;
import java.util.GregorianCalendar;

public class Booking {
	private int bookingID;
	private Calendar dateFrom = new GregorianCalendar();
	private Calendar dateTo = new GregorianCalendar();
	private int stayTime;
	
	/**
	 * Sets the booking id for the booking
	 * @param id of the booking as an int
	 */
	public void setBookingID(int id) {
		this.bookingID = id;
	}
	
	/**
	 * Gets the bookingID of the booking
	 * @return bookingID as an int
	 */
	public int getBookingID() {
		return this.bookingID;
	}
	
	/**
	 * Sets the start date of the booking
	 * @param start date as a calendar
	 */
	public void setStart(Calendar start) {
		this.dateFrom = start;
		this.dateTo.set(2015, dateFrom.get(Calendar.MONTH), dateFrom.get(Calendar.DAY_OF_MONTH));
	}
	
	/**
	 * Gets the start date of the booking
	 * @return the start date as a calendar
	 */
	public Calendar start() {
		return this.dateFrom;
	}
	
	/**
	 * Gets the end date of the booking
	 * @return the end date as a calendar
	 */
	public Calendar end() {
		return this.dateTo;
	}
	
	/**
	 * Sets the amount of days for the booking
	 * @param the length of the stay as an int
	 */
	public void setStayTime(int stay) {
		this.stayTime = stay;
		this.dateTo.add(Calendar.DAY_OF_YEAR, stay-1);
	}
	
	/**
	 * Gets the length of the booking
	 * @return the length of the booking as an int
	 */
	public int getLength() {
		return this.stayTime;
	}
}
