# Team-Project-Phase 2

This phase was completed with the cooperation and discussion of Mahitha Kodali, Max Weinstein, Raphie Lubiniecki, Tommy Wei, Shawn Shu

Things changed from phase 1: added 'synchronized' keyword to saveUsers() and saveReservations() in FilePersistence.java.

To compile the code, first download the code to a local compiler, ideally Intellij or VsCode, then run the ServerUnitTest file in the test cases folder. Since this file passes, it means that our code works. 

We used GUI in our Client class to see if the Server and Client have been connected and 
respond to one another. If they are successfully connected, there will be a message printed on the terminal. You can test this but first run the Server class, then the Client class. Doing it in this particular order is the only way this will work because the Client connects to the Server and not the other way around. After running both classes, there will be a GUI window appearing on the screen. This is the main menu. There are three buttons on the main menu: login, create account, or delete account. By clicking on each of the buttons, the user will be led to a new page and prompted to enter their username, which is their email, as well as their password, if necessary. Each page has a "go back" button that allows the user to return to the main menu. The server side then checks the user's credentials, and a message will be displayed using simple GUI, indicating whether the user has provided the right credentials. If the user entered the right credentials on the login page, a reservation page will pop up, allowing the user to select a specific date and time. All selected reservations will be sent to the server and displayed on the screen. This is achieved through TextArea and ScrollPane. TextArea allows the display of multiple lines of text, while ScrollPane allows the user to scroll if information appears off-screen. 

There are two classes that we created during phase 2: Server.java and Client.java

Server.java is responsible for retrieving and storing information in the database. All information will be sent through PrintWriter to the client, which displays the information through complex GUI. In the server class, we used a switch case to determine which command we should proceed with. For instance, if a String from the client side contains "LOGIN", then the server will call the method login(). To test if the class works, run ServerUnitTest.java under the "TestCases" folder. Each action the client wants to take is sent to the server and the server calls the specific method depending on which action is wanted.

Client.java is where we implement the client. It works with the server through the following steps: 1) When the user enters information, the client sends the information to the server to verify or save it in the database; 2) The client receives updated information to display, which requires the implementation of complex GUI. The class implements the IClient interface and has the following methods: 1) connect()--connects to the server; 2) disconnect()--disconnects from the server; 3) setGUI()--initializes the GUI components, including frame and panel; 4) goback()--called when user clicks "Go back" button; 5) loginPage()--called when user clicks "Login" button; 6) createAccountPage()--called when user clicks "Create Account" button; 7) deleteAccountPage()--called when user clicks "Confirm Deletion"; 8) reservationPage()--called when user successfully logs in. 

Please take note that since our project is about Reserving a table and most restaurants don't need a fee for reserving, pricing and variable pricing is not applicable to our project. 

Things to do for phase 3: Ensure real-time tracking functions, and allows user to make a reservation up to 30 days. Make solid GUI for each of our pages. Make drop-down menus for users to time, partySizes and table, depending on hours of operation and availability.
