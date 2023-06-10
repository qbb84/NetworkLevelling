
To fork and integrate this project, you will need to:

Store Important Info in a Database: I currently store important information in configuration files. You will need to update the project to store this data in a database instead, to improve scalability and performance.

Use Server APIs: This project has been developed as a standalone system. You will need to integrate it with your server's API to ensure it works smoothly with the rest of your system.

Adapt and Expand Boosters System: The current system makes it very easy to create other types of boosters by extending the NetworkStatistic class, and how some larger servers handle player stats. For example, if you want to create a booster that affects player coins, you can create a PlayerCoins class that extends NetworkStatistic. You will need to understand this system and use it to create any additional boosters required for your server.

Create More Custom Events if Necessary: This project includes a number of custom events, primarily related to the boosters. If you need additional custom events, use the provided examples as a guideline for creating your own.

Understand the Command and Configuration Files: The current commands and configuration files were created for testing purposes on a development server only, most of the command class is just spaghetti code so adapt willingly.

The boosters should run fine on the main thread as generally the time complexity of my implementation is linear O(n), though, being more proactive I have the thread running asynchronously. 

Update and Maintain Documentation: This code is documented with comments. As you make changes to the codebase, be sure to update these comments and add your own where necessary. 
