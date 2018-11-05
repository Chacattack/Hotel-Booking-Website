import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Set;

public class Hotel {
	private String name;
	private LinkedHashMap<Integer, Room> rooms = new LinkedHashMap<Integer, Room>();
	
	/**
	 * Sets the name for the hotel
	 * @param n as string of hotel name
	 */
	public void setName (String n) {
		this.name = n;
	}
	
	/**
	 * Gets name of hotel
	 * @return hotel name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Creates a new room given number and type of room
	 * @param number for the room as an int
	 * @param type of room as a string
	 */
	public void newRoom(int number, String type) {
		Room newRoom = new Room();
		newRoom.setRoomNumber(number);
		newRoom.setRoomType(getRoomType(type));
		rooms.put(number, newRoom);
	}
	
	/**
	 * Gets a set of room numbers
	 * @return set of all rooms numbers
	 */
	public Set<Integer> listRooms(){
		return rooms.keySet();
	}
	
	/**
	 * Prints out bookings for a given room
	 * @param room number as int
	 */
	public void printBookings(int room){
			Room r = rooms.get(room);
			r.printBookings();
	}
	
	/**
	 * Checks if a booking can be made
	 * @param id of the booking as an int
	 * @param startDate for the booking as a calendar 
	 * @param nights staying as an int
	 * @param roomType of the room as a string
	 * @return the room number if booking can made, otherwise -1 as int
	 */
	public int checkValid(int id, Calendar startDate, int nights, String roomType){
		Set<Integer> keys = listRooms();
		for (Integer i: keys){
			Room r = rooms.get(i);
			if (getRoomType(roomType).equals(r.getRoomType())){
				if (r.checkBooking(id, startDate, nights)){
					return i;
				}
			}
		}
		return -1;
	}
	
	/**
	 * Looks up the booking for a given id in the room and returns if found
	 * @param id of the booking as an int
	 * @param delete as boolean indicating whether the booking as to be deleted
	 * @return boolean value as to whether successful lookup
	 */
	public boolean findBooking(int id, boolean delete){
		boolean flag = false;
		Set<Integer> keys = listRooms();
		for (Integer i : keys){
			Room r = rooms.get(i);
			if (r.listBookings().contains(id)){
				if (delete){
					r.listBookings().remove(id);
				}
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * Gets keys from each room and accepts bookings that are in temp booking
	 */
	public void makeBookings(){
		Set<Integer> keys = listRooms();
		for (Integer i: keys){
			Room r = rooms.get(i);
			r.completeBooking();
		}
	}
	
	/**
	 * Gets keys from rooms and deletes the temporary storage for bookings
	 */
	public void clearBookings(){
		Set<Integer> keys = listRooms();
		for (Integer i: keys){
			Room r = rooms.get(i);
			r.dontCompleteBooking();
		}
	}
	
	/**
	 * Reads in string and returns the room type for the string
	 * @param s as a String of the room type
	 * @return RoomType associated with the string
	 */
	private Room.RoomType getRoomType(String s){
		Room.RoomType r = Room.RoomType.SINGLE;
		switch(s){
			case "single": r = Room.RoomType.SINGLE;
				break;
			case "double": r = Room.RoomType.DOUBLE;
				break;
			case "triple": r = Room.RoomType.TRIPLE;
				break;
			case "1": r = Room.RoomType.SINGLE;
				break;
			case "2": r = Room.RoomType.DOUBLE;
				break;
			case "3": r = Room.RoomType.TRIPLE;
				break;
		}
		return r;
	}
}