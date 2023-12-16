
package dice.game;
import java.util.*;


public class DiceGame {


    public static void main(String[] args) {
        playGame(); // Entry point of the Game
    }

    // Main method to handle the game logic
    public static void playGame() {
        // Initialize game variables
        int player1TurnScore;
        int FinalscorePlayer1 = 0;
        int player2TurnScore = 0;
        int FinalscorePlayer2 = 0;
        int round = 1;
        boolean[] categoriesSelectedByPlayer1 = new boolean[7];
        boolean[] categoriesSelectedByPlayer2 = new boolean[7];
        String[] playerOne = new String[7];
        String[] playerTwo = new String[7];
        Scanner scanner = new Scanner(System.in);

        System.out.println("Strategic dice game");
        try {
            System.out.print("Enter 1 to play the game or 0 to exit: ");
            int start = scanner.nextInt();

            if (start == 1) {
                // Display initial scores
                displayScores(playerOne, playerTwo, FinalscorePlayer1 + "", FinalscorePlayer2 + "");

                // Loop through rounds (maximum 7 rounds)
                while (round <= 7) {
                    System.out.println("\n----------");
                    System.out.println("Round " + round + ":");
                    System.out.println("----------");

                    // Player 1's turn
                    player1TurnScore = playTurn(1, scanner, categoriesSelectedByPlayer1);
                    FinalscorePlayer1 += player1TurnScore;
                    // Update playerOne array with the current turn score
                    for (int i = 0; i < playerOne.length; i++) {
                        if (playerOne[i] == null && categoriesSelectedByPlayer1[i]) {
                            playerOne[i] = player1TurnScore + " ";
                        }
                    }
                    // Display scores after Player 1's turn
                    displayScores(playerOne, playerTwo, FinalscorePlayer1 + "", FinalscorePlayer2 + "");

                    // Player 2's turn
                    player2TurnScore = playTurn(2, scanner, categoriesSelectedByPlayer2);
                    FinalscorePlayer2 += player2TurnScore;
                    // Update playerTwo array with the current turn score
                    for (int i = 0; i < playerTwo.length; i++) {
                        if (playerTwo[i] == null && categoriesSelectedByPlayer2[i]) {
                            playerTwo[i] = player2TurnScore + " ";
                        }
                    }
                    // Display scores after Player 2's turn
                    displayScores(playerOne, playerTwo, FinalscorePlayer1 + "", FinalscorePlayer2 + "");

                    // Reset turn scores and move to the next round
                    player1TurnScore = 0;
                    player2TurnScore = 0;
                    round++;
                }
                // Determine the winner and display the result
                String winner = (FinalscorePlayer1 > FinalscorePlayer2) ? "Player 1" : "Player 2";
                System.out.println("Game over! " + winner + " wins!");
            } else if (start == 0) {
                System.out.println("End of game");
            } else {
                System.out.println("Wrong input. ");
                playGame(); // Recursively call playGame to start over
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number (1 or 0).");
            scanner.nextLine(); // Consume the invalid input
            playGame(); // Recursively call playGame to start over
        } finally {
            scanner.close();
        }
    }

    public static int playTurn(int startingPlayer, Scanner scanner, boolean[] categoriesSelected) {
        int[] dice = new int[5];
        int rollsLeft = 3;
        int currentPlayer = startingPlayer;
        int score = 0;
        int remaining = 5;
        int selectedCategory = 0;
        int selectedDiceCount = 0;
        boolean Firstdefer = false;
        boolean Seconddefer = false;
        int sequenceScore = 0;
        StringBuilder storedDices = new StringBuilder();

        // Loop through the three throws
        while (rollsLeft > 0) {
            if (rollsLeft == 3) {
                // First throw of the turn
                String choice = "";
                System.out.println("First throw of this turn, player " + currentPlayer + " to throw 5 dice.");
                System.out.println("Throw 5 dice, enter 't' to throw > ");
                // Wait for user input to roll the dice
                while (!choice.equalsIgnoreCase("t")) {
                    choice = scanner.next();
                    if (!choice.equalsIgnoreCase("t")) {
                        System.out.println("Invalid input. Please enter 't' to throw > ");
                    }
                }
                // generate 5 random dices
                for (int i = 0; i < 5; i++) {
                    dice[i] = (int) (Math.random() * 6) + 1;
                }
                // Display the values of the rolled dice
                System.out.print("Throw: ");
                for (int i = 0; i < 5; i++) {
                    System.out.print("[ " + dice[i] + " ]");
                }
                System.out.println();

                String choice2 = "";
                System.out.println("Enter 's' to select category (number of die/dice) or 'd' to defer  >");
                // Wait for user input to select category or defer
                while (!choice2.equalsIgnoreCase("s") && !choice2.equalsIgnoreCase("d")) {
                    choice2 = scanner.next();
                    if (!choice2.equalsIgnoreCase("s") && !choice2.equalsIgnoreCase("d")) {
                        System.out.println("Invalid input. Please enter 's' to select category or 'd' to defer.");
                    }
                }
                // Process user's choice
                if (choice2.equals("s")) {
                    // Select category to play
                    System.out.println("Select category to play: ");
                    System.out.println("Ones (1), Twos (2), Threes (3), Fours (4), Fives (5), Sixes (6), Sequences20 (7) > ");
                    boolean validCategorySelected = false;
                    // Wait for a valid category selection
                    while (!validCategorySelected) {
                        try {
                            selectedCategory = scanner.nextInt();
                            scanner.nextLine();
                            switch (selectedCategory) {
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                case 5:
                                case 6:
                                    // Handle standard categories (Ones to Sixes)
                                    // Check if the category has not been selected before
                                    if (!categoriesSelected[selectedCategory - 1]) {
                                        validCategorySelected = true;
                                        // Update selectedDice array based on the chosen category
                                        for (int i = 0; i < 5; i++) {
                                            if (dice[i] == selectedCategory) {
                                                selectedDiceCount++;
                                            }
                                        }
                                        // Mark the selected category as chosen
                                        categoriesSelected[selectedCategory - 1] = true;
                                        // Display the dice values set aside for the selected category
                                        for (int i = 0; i < 5; i++) {
                                            if (dice[i] == selectedCategory) {
                                                storedDices.append("[ ").append(dice[i]).append(" ]");
                                            }
                                        }
                                        System.out.println("That throw had " + selectedDiceCount + " dice with the value "
                                                + selectedCategory + ". Setting aside " + selectedDiceCount + " dice: " + storedDices.toString());
                                        remaining = remaining - selectedDiceCount;
                                    } else {
                                        System.out.println("Category " + selectedCategory + " has already been selected. Choose a different category >");
                                    }
                                    break;
                                case 7:
                                    // Handle special category (Sequences20)
                                    if (!categoriesSelected[selectedCategory - 1]) {
                                        validCategorySelected = true;
                                        categoriesSelected[selectedCategory - 1] = true;
                                        // continue the turn is handleSequeence20 of Sequences20 and end the turn
                                        sequenceScore = handleSequences20(scanner, dice, storedDices, rollsLeft);
                                        rollsLeft = 0;
                                    } else {
                                        System.out.println("Category " + selectedCategory + " has already been selected. Choose a different category >");
                                    }
                                    break;
                                default:
                                    System.out.println("Invalid category selection. Please enter a number between 1 and 7.");
                                    break;
                            }
                        } catch (InputMismatchException ex) {
                            System.out.println("Invalid input. Please enter a valid number.");
                            // Consume the invalid input
                            scanner.nextLine();
                        }
                    }
                } else if (choice2.equalsIgnoreCase("d")) {
                    // Defer the turn
                    System.out.println("Player " + currentPlayer + " chose to defer this turn.");
                    Firstdefer = true;
                }
                rollsLeft--;
            } else if (rollsLeft == 2) { //repeat the above code for second and third throw
                // Second throw of the turn
                if (remaining != 0) { //if no dices remain to be thrown end the turn
                    String choice3 = "";
                    System.out.println("\nSecond throw of this turn, Player " + currentPlayer + " to throw " + remaining + " dice.");
                    System.out.println("Throw " + remaining + " dice, enter 't' to throw > ");
                    selectedDiceCount = 0;

                    while (!choice3.equalsIgnoreCase("t")) {
                        choice3 = scanner.next();
                        if (!choice3.equalsIgnoreCase("t")) {
                            System.out.println("Invalid input. Please enter 't' to throw > ");
                        }
                    }

                    if (choice3.equalsIgnoreCase("t")) {
                        if (Firstdefer != true) {
                            System.out.println(checkThrow(dice, selectedCategory, remaining));
                            for (int i = 0; i < remaining; i++) {
                                if (dice[i] == selectedCategory) {
                                    selectedDiceCount++;
                                }
                            }
                            remaining = remaining - selectedDiceCount;
                        } else {
                            for (int i = 0; i < 5; i++) {
                                dice[i] = (int) (Math.random() * 6) + 1;
                            }
                            System.out.print("Throw: ");
                            for (int i = 0; i < 5; i++) {
                                System.out.print("[ " + dice[i] + " ]");
                            }
                            System.out.println();

                            String choice4 = "";
                            System.out.println("Enter 's' to select category (number of die/dice) or 'd' to defer  >");
                            while (!choice4.equalsIgnoreCase("s") && !choice4.equalsIgnoreCase("d")) {
                                choice4 = scanner.next();
                                if (!choice4.equalsIgnoreCase("s") && !choice4.equalsIgnoreCase("d")) {
                                    System.out.println("Invalid input. Please enter 's' to select category or 'd' to defer.");
                                }
                            }

                            if (choice4.equals("s")) {
                                // Select category to play
                                System.out.println("Select category to play: ");
                                System.out.println("Ones (1), Twos (2), Threes (3), Fours (4), Fives (5), Sixes (6), Sequences20 (7) > ");
                                boolean validCategorySelected = false;
                                while (!validCategorySelected) {
                                    try {
                                        selectedCategory = scanner.nextInt();
                                        scanner.nextLine();
                                        switch (selectedCategory) {
                                            case 1:
                                            case 2:
                                            case 3:
                                            case 4:
                                            case 5:
                                            case 6:
                                                if (!categoriesSelected[selectedCategory - 1]) {
                                                    validCategorySelected = true;
                                                    for (int i = 0; i < 5; i++) {
                                                        if (dice[i] == selectedCategory) {
                                                            selectedDiceCount++;
                                                        }
                                                    }
                                                    categoriesSelected[selectedCategory - 1] = true;
                                                    for (int i = 0; i < 5; i++) {
                                                        if (dice[i] == selectedCategory) {
                                                            storedDices.append("[ ").append(dice[i]).append(" ]");
                                                        }
                                                    }
                                                    System.out.println("That throw had " + selectedDiceCount + " dice with the value "
                                                            + selectedCategory + ". Setting aside " + selectedDiceCount + " dice: " + storedDices.toString());
                                                    remaining = remaining - selectedDiceCount;
                                                } else {
                                                    System.out.println("Category " + selectedCategory + " has already been selected. Choose a different category >");
                                                }
                                                break;
                                            case 7:
                                                if (!categoriesSelected[selectedCategory - 1]) {
                                                    validCategorySelected = true;
                                                    categoriesSelected[selectedCategory - 1] = true;
                                                    sequenceScore = handleSequences20(scanner, dice, storedDices, rollsLeft);
                                                    rollsLeft = 0;
                                                } else {
                                                    System.out.println("Category " + selectedCategory + " has already been selected. Choose a different category >");
                                                }
                                                break;
                                            default:
                                                System.out.println("Invalid category selection. Please enter a number between 1 and 7.");
                                                break;
                                        }
                                    } catch (InputMismatchException ex) {
                                        System.out.println("Invalid input. Please enter a valid number.");
                                        scanner.nextLine();
                                    }
                                }
                            } else if (choice4.equals("d")) {
                                System.out.println("Player " + currentPlayer + " chose to defer this turn.");
                                Seconddefer = true;
                            }
                        }
                        rollsLeft--;
                    }
                } else {
                    rollsLeft = 0;
                }
            } else if (rollsLeft == 1) {
                // Last throw of the turn
                if (remaining != 0) {
                    String choice5 = "";
                    System.out.println("\nLast throw of this turn, Player " + currentPlayer + " to throw " + remaining + " dice.");
                    System.out.println("Throw " + remaining + " dice, enter 't' to throw  > ");
                    selectedDiceCount = 0;

                    while (!choice5.equalsIgnoreCase("t")) {
                        choice5 = scanner.next();
                        if (!choice5.equalsIgnoreCase("t")) {
                            System.out.println("Invalid input. Please enter 't' to throw > ");
                        }
                    }

                    if (choice5.equalsIgnoreCase("t")) {
                        if (Seconddefer != true) {
                            System.out.println(checkThrow(dice, selectedCategory, remaining));
                            for (int i = 0; i < remaining; i++) {
                                if (dice[i] == selectedCategory) {
                                    selectedDiceCount++;
                                }
                            }
                            remaining = remaining - selectedDiceCount;
                        } else {
                            System.out.println("This is your Last throw for this turn ");
                            for (int i = 0; i < 5; i++) {
                                dice[i] = (int) (Math.random() * 6) + 1;
                            }
                            System.out.print("Throw: ");
                            for (int i = 0; i < 5; i++) {
                                System.out.print("[ " + dice[i] + " ]");
                            }
                            System.out.println();

                            String choice6 = "";
                            System.out.println("Enter 's' to select category >");

                            while (!choice6.equalsIgnoreCase("s")) {
                                choice6 = scanner.next();
                                if (!choice6.equalsIgnoreCase("s")) {
                                    System.out.println("Invalid input. Please enter 's' to select category");
                                }
                            }
                            if (choice6.equals("s")) {
                                // Must select category to play
                                System.out.println("Select category to play: ");
                                System.out.println("Ones (1), Twos (2), Threes (3), Fours (4), Fives (5), Sixes (6), Sequences20 (7) > ");
                                boolean validCategorySelected = false;
                                while (!validCategorySelected) {
                                    try {
                                        selectedCategory = scanner.nextInt();
                                        scanner.nextLine();
                                        switch (selectedCategory) {
                                            case 1:
                                            case 2:
                                            case 3:
                                            case 4:
                                            case 5:
                                            case 6:
                                                if (!categoriesSelected[selectedCategory - 1]) {
                                                    validCategorySelected = true;
                                                    for (int i = 0; i < 5; i++) {
                                                        if (dice[i] == selectedCategory) {
                                                            selectedDiceCount++;
                                                        }
                                                    }
                                                    categoriesSelected[selectedCategory - 1] = true;
                                                    for (int i = 0; i < 5; i++) {
                                                        if (dice[i] == selectedCategory) {
                                                            storedDices.append("[ ").append(dice[i]).append(" ]");
                                                        }
                                                    }
                                                    System.out.println("That throw had " + selectedDiceCount + " dice with the value " + selectedCategory
                                                            + ". Setting aside " + selectedDiceCount + " dice: " + storedDices.toString());
                                                    remaining = remaining - selectedDiceCount;
                                                } else {
                                                    System.out.println("Category " + selectedCategory + " has already been selected. Choose a different category >");
                                                }
                                                break;
                                            case 7:
                                                if (!categoriesSelected[selectedCategory - 1]) {
                                                    validCategorySelected = true;
                                                    categoriesSelected[selectedCategory - 1] = true;
                                                    sequenceScore = handleSequences20(scanner, dice, storedDices, rollsLeft);
                                                    rollsLeft = 0;
                                                } else {
                                                    System.out.println("Category " + selectedCategory + " has already been selected. Choose a different category >");
                                                }
                                                break;
                                            default:
                                                System.out.println("Invalid category selection. Please enter a number between 1 and 7.");
                                                break;
                                        }
                                    } catch (InputMismatchException ex) {
                                        System.out.println("Invalid input. Please enter a valid number.");
                                        scanner.nextLine();
                                    }
                                }
                            }
                        }
                        rollsLeft--;
                    }
                } else {
                    rollsLeft = 0;
                }
            }
        }
        remaining = 5 - remaining; //calculate the dice amount set aside with the remaining and multiply with the selected category to get the score
        if (selectedCategory > 0 && selectedCategory < 7) {
            System.out.println("dices set aside in this turn: " + remaining);
            score = remaining * selectedCategory;
        } else {
            score = sequenceScore;
        }
        return score;  //return the score of the player in this turn
    }

    //Handles the sequence category (Sequences20)
    public static int handleSequences20(Scanner scanner, int[] dice, StringBuilder storedDices, int rollsLeft) {
        int[] selectedDiceLabels = new int[5];
        int[] diceAside = new int[5];
        int setAsideCount = 0;
        int remainingCount = 0;
        int score = 0;

        System.out.println("Sequence 20 selected");

        // Loop through the rolls
        for (int j = 1; j <= rollsLeft; j++) {
            if (j == 1) {//first throw and selection of the sequence
                // Display the thrown dices for the first roll
                System.out.println("0. none");
                for (int i = 0; i < 5; i++) {
                    System.out.println((i + 1) + ". [" + dice[i] + "]");
                }

                // Update the diceAside array based on the sequence made
                diceAside = makeSequence(dice, selectedDiceLabels, diceAside);

                // Update setAsideCount and remainingCount based on the throw
                setAsideCount += (int) Arrays.stream(selectedDiceLabels).filter(value -> value != 0).count();
                remainingCount = 5 - setAsideCount;
            }

            if (j > 1) {//second and third throw 
                // Display information for subsequent throws
                System.out.println("\nThrow " + j + " of this turn, Player to throw " + remainingCount + " dice.");
                String choice = "";
                System.out.println("Throw " + remainingCount + " dice, enter 't' to throw  > ");

                // Wait for user input to roll the dice
                while (!choice.equalsIgnoreCase("t")) {
                    choice = scanner.next();
                    if (!choice.equalsIgnoreCase("t")) {
                        System.out.println("Invalid input. Please enter 't' to throw > ");
                    }
                }

                // Process the subsequent throw
                if (choice.equalsIgnoreCase("t")) {
                    // Regenerate new numbers for the remaining throw
                    int[] remainingDice = generateRandomDiceNumbers(remainingCount);

                    // Display the remaining throw of dice
                    System.out.println("Throw: ");
                    System.out.println("0. none");
                    for (int i = 0; i < remainingCount; i++) {
                        System.out.println((i + 1) + ". [" + remainingDice[i] + "]");
                    }
                    // Initialize selected dice labels array
                    for (int i = 0; i < 5; i++) {
                        selectedDiceLabels[i] = 0;
                    }

                    diceAside = makeSequence(remainingDice, selectedDiceLabels, diceAside);
                    setAsideCount += (int) Arrays.stream(selectedDiceLabels).filter(value -> value != 0).count();
                    remainingCount = 5 - setAsideCount;
                }
                scanner.nextLine();
            }

            // Check for a correct sequence
            if ((setAsideCount == 5) && (isConsecutiveSequence(diceAside))) {
                // A correct sequence has been established
                System.out.println("\nCongratulations! A correct sequence has been established.");
                System.out.println("Player scores 20 for the sequence category.\n");
                score = 20;
                break; // Exit the loop if a correct sequence is found
            } else if ((setAsideCount == 5) && (!isConsecutiveSequence(diceAside))) {
                // 5 dice have been set aside, but the sequence is not correct, end the turn
                System.out.println("\nA correct sequence has not been established.");
                System.out.println("Player scores 0 for the sequence category.\n");
                score = 0;
                break;
            }
        }

        // If 5 dice haven't been set aside or the sequence is not correct, end the turn
        if ((setAsideCount != 5) && (!isConsecutiveSequence(diceAside))) {
            System.out.println("\nA correct sequence has not been established.");
            System.out.println("Player scores 0 for the sequence category.\n");
            score = 0;
        }
        return score; //score 20 for succesfull sequence or 0 for unsuccessful
    }

//function to sort the selected dices
    private static String sortStoredDices(int[] selectedDiceLabels, int[] throwValues) {
        // Create a list to store selected dice values
        List<Integer> selectedValues = new ArrayList<>();

        // Iterate through selected dice labels and add corresponding values to the list
        for (int label : selectedDiceLabels) {
            int index = label - 1;
            if (index >= 0 && index < throwValues.length) {
                selectedValues.add(throwValues[index]);
            }
        }

        // Sort the selected values
        selectedValues.sort(Comparator.naturalOrder());

        // Format the sorted values in the desired way
        StringBuilder formattedDice = new StringBuilder();
        for (int value : selectedValues) {
            formattedDice.append(value).append(" ");
        }

        // Return the formatted string, trimmed to remove trailing whitespace
        return formattedDice.toString().trim();
    }

//generate random numbers for the throws
    private static int[] generateRandomDiceNumbers(int count) {
        int[] diceNumbers = new int[count];
        for (int i = 0; i < count; i++) {
            diceNumbers[i] = (int) (Math.random() * 6) + 1;
        }
        return diceNumbers;
    }

    public static StringBuilder storedDices = new StringBuilder();

//user makes the sequence based on the throws
    private static int[] makeSequence(int[] dice, int[] selectedDiceLabels, int[] diceAside) {
        Scanner scanner = new Scanner(System.in);
        // Get user input for the sequence
        String input = getUserInput(scanner, diceAside);
        // Process the user input
        processInput(input, selectedDiceLabels, diceAside, dice);
        // Return the array representing dice set aside
        return diceAside;
    }

    private static String getUserInput(Scanner scanner, int[] diceAside) {
        String input;
        String[] checkInput;
        // Repeat until a valid input is provided
        do {
            // Prompt the user to enter dice labels separated by a space
            System.out.print("Enter which dice you wish to set aside using the number labels separated by a space (e.g., 1 3 5), or enter 0 for none > ");
            input = scanner.nextLine();
            // Split the input into an array of strings
            checkInput = input.trim().split("\\s+");
        } while (isValidInput(input, diceAside, checkInput));

        return input;
    }

    private static boolean isValidInput(String input, int[] diceAside, String[] checkInput) {
        boolean result = false;
        int count = 0;

        // Count the number of 0s in the diceAside array
        for (int i : diceAside) {
            if (i == 0) {
                count++;
            }
        }

        // Check for duplicate entries in the input
        boolean duplicates = hasDuplicates(input);

        // Iterate through the checkInput array
        for (int i = 0; i < checkInput.length; i++) {
            try {
                int currentLabel = Integer.parseInt(checkInput[i]);
                // Check for specific conditions and print error messages
                if (currentLabel == 0 && checkInput.length > 1) {
                    System.out.println("Select 0 by itself");
                    result = true;
                    break;
                } else if (currentLabel > count) {
                    System.out.println("Invalid input: Not enough available dice to set aside.");
                    result = true;
                    break;
                } else if (duplicates) {
                    System.out.println("Invalid input: Include no duplicates.");
                    result = true;
                    break;
                } else if (currentLabel < 0) {
                    System.out.println("Invalid input: Dice labels must be positive.");
                    result = true;
                    break;
                }
            } catch (NumberFormatException e) {
                return true; // Invalid integer, return true
            }
        }

        return result;
    }

//check the input for dublicates
    private static boolean hasDuplicates(String input) {
        // Split the input string into an array of numbers
        String[] numbers = input.trim().split("\\s+");
        // Use a Set to keep track of unique numbers
        Set<String> uniqueNumbers = new HashSet<>();
        // Iterate through the numbers
        for (String number : numbers) {
            // If adding the number to the set fails (i.e., it's already present), it's a duplicate
            if (!uniqueNumbers.add(number)) {
                return true; // Duplicate found
            }
        }
        return false; // No duplicates
    }

    private static void processInput(String input, int[] selectedDiceLabels, int[] diceAside, int[] dice) {
        // Check if the user chose not to keep any dice
        if (input.equals("0")) {
            System.out.println("You have not selected to keep any dice from that throw");
        } else {
            // Update the selected dice labels based on the user input
            updateSelectedDiceLabels(input, selectedDiceLabels);
            // Display the selected dice and update the dice aside array
            displaySelectedDice(selectedDiceLabels, diceAside, dice);
        }
    }

    private static void updateSelectedDiceLabels(String input, int[] selectedDiceLabels) {
        // Split the input string into an array of selected dice labels
        String[] inputLabels = input.trim().split("\\s+");

        // Iterate through the input labels and update the selected dice labels array
        for (int i = 0; i < inputLabels.length; i++) {
            // Parse the label from the input and update the array
            int label = Integer.parseInt(inputLabels[i]);
            selectedDiceLabels[i] = label;
        }
    }

    private static void displaySelectedDice(int[] selectedDiceLabels, int[] diceAside, int[] dice) {
        System.out.println("You have selected the following dice to keep:");

        // Sort the stored dice values based on selected dice labels
        String sortedDices = sortStoredDices(selectedDiceLabels, dice);
        String[] inputDice = sortedDices.split(" ");

        int idx = 0;
        // Iterate through the diceAside array to update it with selected dice values
        for (int i = 0; i < diceAside.length; i++) {
            if (idx == inputDice.length) {
                break;
            }
            if (diceAside[i] == 0) {
                int diceA = -1;
                try {
                    // Parse the input dice value
                    diceA = Integer.parseInt(inputDice[idx]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid...");
                }
                if (diceA != -1) {
                    diceAside[i] = diceA;
                    idx++;
                }
            }
        }

        // Sort the updated diceAside array
        Arrays.sort(diceAside);

        // Display the selected dice values
        for (int i : diceAside) {
            if (i != 0) {
                System.out.print("[" + i + "]");
            }
        }
    }

    private static boolean isConsecutiveSequence(int[] dice) {
        // Sort the dice array in ascending order
        Arrays.sort(dice);
        // Check if the sorted dice values form a consecutive sequence
        for (int i = 0; i < dice.length - 1; i++) {
            if (dice[i] + 1 != dice[i + 1]) {
                return false;
            }
        }

        return true;
    }

//Checks and processes the player's dice throw based on the selected category and remaining dice.
    public static String checkThrow(int[] dice, int selectedCategory, int remaining) {
        // StringBuilder to store the selected dice for display.
        StringBuilder storedDices = new StringBuilder();

        if (remaining == 0) {
            // No remaining dice with the selected category.
            return "No remaining dice with your category.";
        } else if (remaining > 0 && remaining < 5) {
            // Process the throw when there are remaining dice less than 5.
            for (int i = 0; i < remaining; i++) {
                if (dice[i] == 0) {
                    // If the dice value is 0, generate a random value for it.
                    dice[i] = (int) (Math.random() * 6) + 1;
                }
            }

            // Display the values of the rolled dice.
            System.out.print("Throw: ");
            for (int i = 0; i < remaining; i++) {
                System.out.print("[ " + dice[i] + " ]");
            }
            System.out.println();

            int selectedDiceCount = 0;
            // Set aside dice that match the selected category.
            for (int i = 0; i < remaining; i++) {
                if (dice[i] == selectedCategory) {
                    selectedDiceCount++;
                    storedDices.append("[ ").append(dice[i]).append(" ]");
                }
            }

            // Return the outcome of the throw, including the selected dice.
            return "That throw had " + selectedDiceCount + " dice with the value " + selectedCategory
                    + ". Setting aside " + selectedDiceCount + " dice: " + storedDices.toString();
        } else if (remaining == 5) {
            // Process the initial throw of 5 dice.
            for (int i = 0; i < 5; i++) {
                dice[i] = (int) (Math.random() * 6) + 1;
            }

            // Display the values of the rolled dice.
            System.out.print("Throw: ");
            for (int i = 0; i < 5; i++) {
                System.out.print("[ " + dice[i] + " ]");
            }
            System.out.println();

            int selectedDiceCount = 0;
            // Set aside dice that match the selected category.
            for (int i = 0; i < 5; i++) {
                if (dice[i] == selectedCategory) {
                    selectedDiceCount++;
                    storedDices.append("[ ").append(dice[i]).append(" ]");
                }
            }

            // Return the outcome of the throw, including the selected dice.
            return "That throw had " + selectedDiceCount + " dice with the value " + selectedCategory
                    + ". Setting aside " + selectedDiceCount + " dice: " + storedDices.toString();
        }

        // Return a message for an invalid input or unexpected scenario.
        return "Invalid input or unexpected scenario.";
    }

    //Prints a separator line to the console.     
    public static void printSeparatorLine() {
        System.out.println("----------------------------------------------");
    }

    // Displays the scores for both players in a tabular format.
    public static void displayScores(String[] playerOne, String[] playerTwo, String FinalScore1, String FinalScore2) {
        String[] categories = {"Ones", "Twos", "Threes", "Fours", "Fives", "Sixes", "Sequence 20"};
        printSeparatorLine();
        System.out.println("|                    | PLAYER 1  | PLAYER 2  |");
        printSeparatorLine();
        for (int i = 0; i < 7; i++) {
            System.out.printf("| %-18s | %-9s | %-9s |\n", categories[i], getScore(playerOne, i), getScore(playerTwo, i));
            printSeparatorLine();
        }
        System.out.printf("| %-18s | %-9s | %-9s |\n", "TOTAL", FinalScore1, FinalScore2);
        printSeparatorLine();
    }

    // Gets the score for a specific category from the player's score array.
    public static String getScore(String[] Scorecontainer, int category) {
        return (Scorecontainer[category] == null) ? "" : Scorecontainer[category];
    }
}
