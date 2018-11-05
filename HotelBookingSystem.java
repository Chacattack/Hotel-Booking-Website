import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.Set;
import java.io.FileReader;

public class HotelBookingSystem {
	static private String input[];
	static private LinkedHashMap<String, Hotel> hotels = new LinkedHashMap<String, Hotel>();
	static private boolean flag = false;
	
	/**
	 * Main method used to break up the input string and decide which method to call
	 * @precondition: input is in the designated format dictated in assignment 1 spec.
	 * @param args as input from stdin
	 */
	public static void main(String[] args) {
	    Scanner sc = null;
	    try { 
	    	sc = new Scanner(new FileReader(args[0]));	
	    	while (sc.hasNextLine()) {
	    		input = sc.nextLine().split(" ");
	    		if (input[0].equals("Hotel")){
	    			createHotel();
	    		} else {
	    			if (flag){
	    				System.out.println();
	    			} else {
	    				flag = true;
	    			}
	    			if (input[0].equals("Booking")) {
		    			makeBooking(Integer.parseInt(input[1]));
		    		} else if (input[0].equals("Change")) {
		    			if (!bookingExists(Integer.parseInt(input[1]))){
		    				System.out.print("Change rejected");
		    			} else {
		    				makeBooking(Integer.parseInt(input[1]));
		    			}
		    		} else if (input[0].equals("Cancel")) {
		    			cancelBooking(Integer.parseInt(input[1]));
		    		} else if (input[0].equals("Print")) {
		    			printHotel(input[1]);
		    		} else {
		    			System.err.println("Encountered bad input");
		    		}
	    		}
	    	}
	    }
	    catch (FileNotFoundException e) {}
	    finally {
	    	if (sc != null){
	    		sc.close();
	    	}
	    }
	}
	
	/**
	 * This method will print out all rooms for a hotel as well as all bookings within it
	 * @param name of the hotel as a string
	 */
	private static void printHotel(String name) {
		Hotel hotel = hotels.get(name);
		Set<Integer> keys = hotel.listRooms();
		int i = 1;
		for (Integer room : keys){
			System.out.print(hotel.getName() + " " + room.toString());
			hotel.printBookings(room.intValue());
			//to ensure extra lines aren't printed
			if (i++ == keys.size()){
				break;
			}
			System.out.println();
		}
	}
	
	/**
	 * This method will cancel a booking
	 * @param id of the booking as an int
	 */
	private static void cancelBooking(int id) {
		boolean flag = false;
		Set<String> hotel = listHotels();
		for (String s : hotel){
			Hotel h = hotels.get(s);
			flag = h.findBooking(id, true);
			if (flag){
				break;
			}
		}
		if (flag == true){
			System.out.print("Cancel " + id);
		} else {
			System.out.print("Cancel rejected");
		}
	}
	
	/**
	 * This method will change a current booking that is made, updating it to the new request
	 * @param id of the booking as an int
	 * @param r as an ArrayList of all hotels
	 * @param hotelName as a string representing the hotel name associated with the booking
	 */
	private static void changeBooking(int id, ArrayList<Integer> r, String hotelName) {
		if (!r.isEmpty()){
			Hotel h = hotels.get(hotelName);
			Set<String> ht = listHotels();
			boolean flag = false;
			//we need to change the booking, hence we need to find and delete the old booking
			for (String s : ht){
				Hotel m = hotels.get(s);
				flag = m.findBooking(id, true);
				if (flag){
					break;
				}
			}
			h.makeBookings();
			System.out.print("Change " + id + " " + hotelName);
			Collections.sort(r);
			for (int room: r){
				System.out.print(" " + room);
			}
		} else {
			Set<String> hotel = listHotels();
			for (String name : hotel){
				Hotel k = hotels.get(name);
				k.clearBookings();
			}
			System.out.print("Change rejected");
		}
		
	}
	
	/**
	 * This method looks and sees if a booking exists within the system
	 * @param id of the booking as an int
	 * @return boolean value of whether it is found
	 */
	private static boolean bookingExists(int id){
		Set<String> hotel = listHotels();
		for (String name : hotel){
			Hotel h = hotels.get(name);
			if (h.findBooking(id, false)){
				return true;
			}
		}
		return false;
	}

	/**
	 * This method will make a booking based on the input given in the string
	 * @param id of the booking as an int
	 */
	private static void makeBooking(int id) {
		String hotelName = "h";
		ArrayList<Integer> r = new ArrayList<Integer>();
		int month = getMonth(input[2]);
		Calendar bookDate = new GregorianCalendar(2015, month, Integer.parseInt(input[3]));
		Set<String> hotel = listHotels();
		//will check each hotel to see if booking can be made
		for (String name : hotel){
			int x = 5;	//5 is the number in the string where we worry about room type and number of rooms
			Hotel h = hotels.get(name);
			hotelName = h.getName();
			loop:
			//will check each booking request for the id and whether it can be made
			while (x < input.length){
				int y = 0;
				while (y < Integer.parseInt(input[x+1])){
					int booking = h.checkValid(id, bookDate, Integer.parseInt(input[4]), input[x]);
					if (booking != -1){
						r.add(booking);
						y++;				
					} else {
						h.clearBookings();
						r.clear();		
						break loop;
					}
				}
				x += 2;
			}
			if (x >= input.length){
				break;
			}
		}
		if (input[0].equals("Change")){
			changeBooking(id, r, hotelName);
		} else {
			bookingMessage(r, hotelName);
		}
	}
	
	/**
	 * This will print out the message when a booking is made
	 * @param r as an ArrayList holding all hotels
	 * @param hotelName as a String of the hotel to make the booking at
	 */
	private static void bookingMessage (ArrayList<Integer> r, String hotelName){
		//if the list isn't empty, that means bookings can be made
		if (!r.isEmpty()){
			Hotel h = hotels.get(hotelName);
			h.makeBookings();
			System.out.print("Booking " + input[1] + " " + hotelName);
			
			//sorts to print in order
			Collections.sort(r);
			for (int room: r){
				System.out.print(" " + room);
			}
		} else {
			//the list is empty meaning we must clear our temporary bookings made
			Set<String> hotel = listHotels();
			for (String name : hotel){
				Hotel k = hotels.get(name);
				k.clearBookings();
			}
			System.out.print("Booking rejected");
		}
	}
	
	/**
	 * Creates a hotel
	 */
	private static void createHotel(){
		//if hotel is already created just make new room for it, otherwise create hotel and room and adds hotel to map
		if (hotels.containsKey(input[1])){
			Hotel hotel = hotels.get(input[1]);
			hotel.newRoom(Integer.parseInt(input[2]), input[3]);
		} else {
			Hotel newHotel = new Hotel();
			newHotel.setName(input[1]);
			newHotel.newRoom(Integer.parseInt(input[2]), input[3]);
			hotels.put(input[1], newHotel);
		}		
	}
	
	/**
	 * Gets a set of all the hotels currently created
	 * @return set of all hotels
	 */
	private static Set<String> listHotels(){
		return hotels.keySet();
	}
	
	/**
	 * Changes the given month from a string to int 
	 * @param m month as a string
	 * @return int representation of the month
	 */
	private static int getMonth(String m) {
		int r = 0;
		switch(m){
			case "Jan": r = 0;
				break;
			case "Feb": r = 1;
				break;
			case "Mar": r = 2;
				break;
			case "Apr": r = 3;
				break;
			case "May": r = 4;
				break;
			case "Jun": r = 5;
				break;
			case "Jul": r = 6;
				break;
			case "Aug": r = 7;
				break;
			case "Sep": r = 8;
				break;
			case "Oct": r = 9;
				break;
			case "Nov": r = 10;
				break;
			case "Dec": r = 11;
				break;
		}
		return r;
	}
}