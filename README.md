# Tutor_Matching_Service

Welcome to our Tutor Matching Service project! To compile and run our project, run the Server class and then the Client class. Then you will be presented with the account menu where you can either create an account or login to an existing account. After inputting your information, from there, the program will display the page of your selected role, either a student or tutor. You can then choose from the variety of menu options from editing your profile, sending messages, blocking users, filtering words, and many more!

Pranav Sanghi - Submitted Vocareum workspace.

Isha Singhal - Submitted Report on Brightspace.

Pranav Sanghi - Submitted Presentation on Brightspace.

The User class is a parent class, while the Student and Tutor classes are the child classes. These classes help us manage the Client and handle the information given by the users. The Message class handles messaging features such as reading and writing to the conversation file. The GUI class manages all of the GUI-content that is displayed with the information passed between the ClientMain and ServerMain classes. The ClientMain class tries to connect to the ServerMain and then calls the methods of the GUI class to then display the prompts to the user. The GUI class handles all the Client interactions within the program. The ServerMain processes the information given by the Client and sends the appropriate response back to the Client. We tested these classes using self-generated test cases, five of which we discussed in the Tests.md file in this repository. We ensured that all of the features we worked to implement were functional and successful throughout the program, also able to handle any errors that it could face. 
