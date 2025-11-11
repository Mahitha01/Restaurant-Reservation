# Team-Project-Phase 1

This phase was completed with the cooperation and discussion of Mahitha Kodali, Max Weinstein, Raphie Lubiniecki, Tommy Wei, Shawn Shu

For the files in Database2 folder, they don't need to be compiled. They were the original version of the database, until later we decided to use Hashmap. However, for test cases ReservableUnitTest.java, ReservationUnitTest.java, and ReservationDatabase.java, as well as all other files in Database2 folder, they were referenced and used to create the DatabaseManager.java and test cases in the database folder(under src). To compile the code, first download the code to a local compiler, ideally Intellij or Vscode, then run the test cases in the test cases folder. You can either run each UnitTest file on it's own, or the whole TestCases package all together. Since they all pass, it means that our code works. 

User, IUser, UserTest:
The User class defines the basic structure of a user. It implements the IUser interface and is tested by the UserTest class. Each user contians two fields: email and password. password is a unique identity that authenticates the user. The class also includes methods that allow users to view their existing reservations, or cancel their reservations, if needed. In testing, we created a new user object to check that all methods are functioning correctly and the stored information matches the expected outputs. 

Booking, IBooking, BookingTest, BookingUnitTest:
The Booking class represents a reservation made by a user. It extends the User class. The class includes fields such as partySize, bookingTime, and email and password. Each booking is assigned with a unique ID, which makes getting a specific reservation easier. The isAvailable method checks whether a specific table is available and returns a boolean. The class also implements the IBooking interface and is tested by BookingTest class, which verifies that newly created bookings are stored and return the intended information. 

FilePersistence, IPersistence, FilePersistenceTest.java:
In FilePersistence class, we imported the database.src.database1 package, which allows us to use serialization. The class implements IPersistence interface and uses two files: users.db for serialized user data and reservations.db for serialized booking data. The saveUsers and saveReservations methods write the data, and loadUsers and loadReservations methods deserialize data. In FilePersistenceTest.java, we called the methods to ensure that serialized data could be successfully saved and loaded. 

DatabaseManager, IDatabaseManager, DatabaseManagerUnitTest.java:
Our DatabaseManager class implements the IDatabaseManager interface and serves as a coordinator between the User classes, specifically dealing with account creation and authentication, and our data storage. It includes methods to create and delete users, authenticate logins, add reservations, and cancel existing ones. We used synchronization to ensure concurrency works without issue. So if a new reservation is made, our data is updated to avoid conflicts. We used DatabaseManagerUnitTest.java to test our methods. Our testing outcomes pass all the test cases, which means that database did perform as expected. 


