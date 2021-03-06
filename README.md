# Nicole's CPSC 210 Project
## Budget/Expense Tracker

**What will the application do?** <br>
The purpose of this project is to aid the user in managing their finances. 
They will be able to track and visualize their monthly spending,
set goals, record savings, and be able to track reoccurring bills (e.g. hydro, rent, credit cards).


**Who will use it?**<br>
The target audience for this project is anyone who is in need of budget 
management and planning. I would also like to focus the implementation of this 
project to the average college/university student.


**Why is this project of interest to me?**   <br>
As a university student, being on a budget and managing my finances
is extremely important for day-to-day life. This project gives me the 
opportunity to implement something beyond just an Excel spreadsheet and 
will help me and (hopefully) other people to visualize and track their finances.

## User Story
- As a user, I would like to add an expense to the monthly tracker 
- As a user, I would like to delete an expense from the monthly tracker
- As a user, I would like to add all the expense values together in the monthly tracker 
- As a user, I would like to view expenses in the monthly tracker
- As a user, I would like to save the expenses in the monthly tracker to a file
- As a user, I would like the option to load the monthly tracker from a previously saved file
- As a user, I would like to specify the month the monthly tracker relates to

## Phase 4: Task 2
In addition to the user having access at any point to print the log, the log will be 
printed to the console when the session is terminated. Here is an example of the output 
below:

Tue Nov 23 17:00:01 PST 2021
Expense Added To List: 1000.0, rent

Tue Nov 23 17:00:01 PST 2021
Month Set: January

Tue Nov 23 17:00:11 PST 2021
Expense Added To List: 150.0, hydro

Tue Nov 23 17:00:11 PST 2021
Month Set: January

Tue Nov 23 17:00:25 PST 2021
Expense Added To List: 100.0, gas

Tue Nov 23 17:00:25 PST 2021
Month Set: January

Tue Nov 23 17:00:28 PST 2021
Cleared All Expenses From List.

## Phase 4: Task 3
If I had more time to work on this project, I would like to include more classes to 
reorganize the messy MonthlyTrackerUI class. The UML diagram looks simple, but what 
lies in the code does not have much organization. Therefore, I would likely do 
the following:
- Make use of abstractions where needed
- Create additional classes to clean up and organize the MonthlyTrackerUI class
- Remove repetition in some areas of code
- Create more exceptions to prevent any user errors



