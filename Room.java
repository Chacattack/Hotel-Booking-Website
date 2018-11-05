import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;

public class Room {
	private int roomNumber;
	private RoomType type;
	private LinkedHashMap<Integer, Booking> bookings = new LinkedHashMap<Integer, Booking>();
	private LinkedHashMap<Integer, Booking> tempBookings = new LinkedHashMap<Integer, Booking>();
	
	/**
	 * enumeration for the 3 types of rooms that are available
	 */
	public enum RoomType {
		SINGLE, DOUBLE, TRIPLE
	}
	
	/**
	 * Sets the room numbers
	 * @param n as an int
	 */
	public void setRoomNumber (int n) {
		this.roomNumber = n;
	}
	
	/**
	 * Sets the type of room
	 * @param t as a RoomType
	 */
	public void setRoomType (RoomType t) {
		this.type = t;
	}
	
	/**
	 * Returns the room number
	 * @return roomNumber as an int
	 */
	public int getRoomNumber() {
		return this.roomNumber;
	}
	
	/**
	 * Gets the type of room it is
	 * @return type as RoomType
	 */
	public RoomType getRoomType() {
		return this.type;
	}
	
	/**
	 * Gets a listing of booking ids associated with the room
	 * @return key set for bookings as a set
	 */
	public Set<Integer> listBookings(){
		return bookings.keySet();
	}
	
	/**
	 * This will check if the booking can be made and returns true if it can, false otherwise
	 * @param id of the booking as an int
	 * @param start date of the booking as a calendar
	 * @param nights the booking last as an int
	 * @return a boolean value stating whether the booking can be made
	 */
	public boolean checkBooking(int id, Calendar start, int nights) {
		//gets the keys for the bookings and tempbookings data structure
		Set<Integer> lb = listBookings();
		Set<Integer> lbt = tempBookings.keySet();
		
		//sets up new calendar to determine end date of booking wanting to be made
		Calendar end = new GregorianCalendar();
		end.set(2015, start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH));
		end.add(Calendar.DAY_OF_YEAR, nights-1);
		
		//looks up if the current booking is already inside the temp booking
		for (Integer i: lbt){
			Booking t = tempBookings.get(i);
			if (t.getBookingID() == id) {
				return false;
			}
		}
		
		//looks through the bookings,if there is one of the id it is skipped as irrelevant - only used in change
		//should a clash occur, booking cannot be made
		for (Integer i: lb){
			Booking b = bookings.get(i);
			if (b.getBookingID() == id){
				continue;
			}
			if (start.before(b.end()) && (b.start().before(end))) {
				return false;
			}
		}
		
		//sets up booking and places it in the tempbooking data structure waiting to be made if everything
		//in the booking is successful
		Booking nb = new Booking();
		nb.setBookingID(id);
		nb.setStart(start);
		nb.setStayTime(nights);
		tempBookings.put(id, nb);
		return true;
	}
	
	/**
	 * Completes the booking by moving everything from temp into the booking section
	 * then clearing tempBookings
	 */
	public void completeBooking(){
		bookings.putAll(tempBookings);
		tempBookings.clear();
	}
	
	/**
	 * Removes all bookings in the temp booking sections
	 */
	public void dontCompleteBooking(){
		tempBookings.clear();
	}
	
	/**
	 * Prints out details of the calendar given
	 * @param cal as a calendar
	 */
	public void print(Calendar cal){
		String month;
		switch(cal.get(Calendar.MONTH)){
			case 0: month = "Jan";
				break;
			case 1: month = "Feb";
				break;
			case 2: month = "Mar";
				break;
			case 3: month = "Apr";
				break;
			case 4: month = "May";
				break;
			case 5: month = "Jun";
				break;
			case 6: month = "Jul";
				break;
			case 7: month = "Aug";
				break;
			case 8: month = "Sep";
				break;
			case 9: month = "Oct";
				break;
			case 10: month = "Nov";
				break;
			default: month = "Dec";
				break;
		}
		System.out.print(" " + month + " " + cal.get(Calendar.DAY_OF_MONTH));
	}
	
	/**
	 * Prints out all bookings for a given room
	 */
	public void printBookings(){
		//Treemap is used to sort the bookings into calendar order
		TreeMap<Calendar, Integer> m = new TreeMap<Calendar, Integer>();
		Set<Integer> booking = listBookings();
		
		//loops through getting each booking and puts it into the treemap
		for (Integer id: booking){
			Booking b = bookings.get(id);
			m.put(b.start(), b.getLength());
		}
		
		//For each calendar in treemap it prints out the date and length of the booking
		Set<Calendar> cal = m.keySet();
		for(Calendar c: cal) {
			print(c);
			System.out.print(" " + m.get(c));
		}
	}
}
