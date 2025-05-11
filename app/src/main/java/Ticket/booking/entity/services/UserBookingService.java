package Ticket.booking.entity.services;
import Ticket.booking.util.UserServiceUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import Ticket.booking.entity.User;
import Ticket.booking.entity.services.TrainService;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


public class UserBookingService {
        private User user;  //store user globally
private List<User> userList;
        private  ObjectMapper objectMapper= new ObjectMapper();
        private static final String USERS_PATH="app/src/main/java/ticket/booking/localdb/users.json";


    public UserBookingService(User user) throws IOException {
        this.user = user;
        loadUserListFromFile();
    }

    public UserBookingService() throws IOException {
        loadUserListFromFile();
    }

    private void loadUserListFromFile() throws IOException {
        userList = objectMapper.readValue(new File(USERS_PATH), new TypeReference<List<User>>() {});
    }
    public Boolean loginUser() {
        Optional<User> foundUser = userList.stream()
                .filter(user1 -> {
                    return user1.getName().equals(user.getName()) &&
                            UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
                })
                .findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User user1){
            try {
                userList.add(user1);
                saveUserListToFile();
                return Boolean.TRUE;
            }catch (IOException ex){
                return Boolean.FALSE;
            }
    }
    private void saveUserListToFile() throws IOException{
            File usersFile= new File(USERS_PATH);
            objectMapper.writeValue(usersFile, userList);
    }
    public void fetchBooking(){
            user.printTickets();
    }







//
//    public Boolean cancelBooking(String ticketId){
//        // todo: Complete this function
//        public Boolean cancelBooking(String ticketId){
//
//            Scanner s = new Scanner(System.in);
//            System.out.println("Enter the ticket id to cancel");
//            ticketId = s.next();
//
//            if (ticketId == null || ticketId.isEmpty()) {
//                System.out.println("Ticket ID cannot be null or empty.");
//                return Boolean.FALSE;
//            }
//
//            String finalTicketId1 = ticketId;  //Because strings are immutable
//            boolean removed = user.getTicketsBooked().removeIf(ticket -> ticket.getTicketId().equals(finalTicketId1));
//
//            String finalTicketId = ticketId;
//            user.getTicketsBooked().removeIf(Ticket -> Ticket.getTicketId().equals(finalTicketId));
//            if (removed) {
//                System.out.println("Ticket with ID " + ticketId + " has been canceled.");
//                return Boolean.TRUE;
//            }else{
//                System.out.println("No ticket found with ID " + ticketId);
//                return Boolean.FALSE;
//            }
//        }
//


//
//    public Boolean cancelBooking(String ticketId){
//// complete this function
//
//        public Boolean cancelBooking(String ticketId) {
//            return bookingList.removeIf(booking -> booking.getTicketId().equals(ticketId)) ? saveBookings() : Boolean.FALSE;
//        }
//
//        private Boolean saveBookings() {
//            try {
//                saveBookingListToFile();
//                return Boolean.TRUE;
//            } catch (IOException e) {
//                e.printStackTrace();
//                return Boolean.FALSE;
//            }
//        }
//
//
//        public Boolean cancelBooking(String ticketId) {
//            // Find the booking with the given ticket ID
//            Optional<Booking> bookingToCancel = bookingList.stream()
//                    .filter(booking -> booking.getTicketId().equals(ticketId))
//                    .findFirst();
//
//            if (bookingToCancel.isPresent()) {
//                bookingList.remove(bookingToCancel.get());
//
//                // Optionally save updated booking list to file or database
//                try {
//                    saveBookingListToFile();  // You should have this method
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return Boolean.FALSE;
//                }
//
//                return Boolean.TRUE;
//            } else {
//                // No booking found with given ticketId
//                return Boolean.FALSE;
//            }
//        }



//        return Boolean.FALSE;
//    }
//
//}


public List<Train> getTrains(String source, String destination){
    try{
        TrainService trainService = new TrainService();
        return trainService.searchTrains(source, destination);
    }catch(IOException ex){
        return new ArrayList<>();
    }
}

public List<List<Integer>> fetchSeats(Train train){
    return train.getSeats();
}

public Boolean bookTrainSeat(Train train, int row, int seat) {
    try{
        TrainService trainService = new TrainService();
        List<List<Integer>> seats = train.getSeats();
        if (row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()) {
            if (seats.get(row).get(seat) == 0) {
                seats.get(row).set(seat, 1);
                train.setSeats(seats);
                trainService.addTrain(train);
                return true; // Booking successful
            } else {
                return false; // Seat is already booked
            }
        } else {
            return false; // Invalid row or seat index
        }
    }catch (IOException ex){
        return Boolean.FALSE;
    }
}
}


