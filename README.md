# The-Gold-Rush



The Gold Rush Game								                                      
Jaoiher Boulila

What was implemented in The Gold Rush Game?

-Basic Rules
The project TheGoldRush is based on the game MineSweeper and is inspired by the Gold Rush of 1848.
The playing field is divided into squares. By clicking on a square, it opens up. Some squares contain mines. If you click on a square with a mine, the game ends. If the clicked square does not have a mine, there are two possibilities. If a number appears, it represents the number of adjacent squares containing mines, from 1 to 8. If no number appears, it means that all 8 adjacent squares are mine-free. The player's objective is to collect all the gold by opening all the mine-free cells. Additionally, they must locate all the mines. The player aims to achieve the best possible score based on the time taken and the amount of gold collected. Each cell provides a different amount of gold. The user can choose the character, and the level of difficulty, request hints, and buy more hints. Every character has different functionalities.

Setup and Launch:
- Open IntelliJ IDEA.
-	Navigate to File.
-	Open the TheGoldRushGame.java file.
-	Click the play button to launch the game.

Functionalities Implemented:
-	Basic Mouse Interaction: Players can navigate between scenes, open tiles, and pause the game by left-clicking.
-	Pause: Users can pause the game, preventing any further moves. They can resume by clicking the same button labeled "resume." Additionally, the user can pause the game using a keyboard input (backspace key).
-	Mine Generation: The number and location of mines are randomly generated for each game.
-	Timer: A timer is implemented.
-	Mouse Press Event: The event occurs only when the mouse is pressed.
-	Restart: Users can restart the game by pressing the restart button.
-	Character Selection: Users can choose their character by swapping between options and selecting their desired character.
-	Hints: Users can request hints by clicking and dragging a "question mark" onto the cell they want to uncover. The number of available hints varies based on the chosen difficulty level. 
-	More Hints: The user can buy more Hints
-	Flag Placement: Users can place flags using the right mouse button.
-	Game End Screen: After completing the game, a screen announces either victory or defeat, and the player can start a new game.
-	Score: Updating and calculating the score based on collected gold and time taken. In the "YOU WIN" screen, players will be able to view their score. Additionally, the record, gold collected, and best time will be updated.
- Grid Size for Difficulty Levels: Each difficulty level, in addition to determining a different number of available hints, will determine a different number of cells. In the medium mode, the grid has a medium size(98) and a medium number of bombs (20). In the hard mode, the grid has a larger size (162) and there’s a higher number of bombs(30).
-	Character Selection: Players can choose a character only if they have collected enough gold. Each character has a different cost. Every Character has a different functionality! 
-	Different Functionalities: Each character has different abilities. There are four characters. Selecting the first one allows you to play normally. Purchasing the second character allows you to play with more hints (the number depends on the chosen difficulty). Buying the third character allows you to stop the timer for 5 seconds, 5 times. Buying the fourth character allows you to defuse bombs by shouting (in 5 seconds).

