import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.lang.Integer;
/**
*FlashcardDisplayer.java
*Alia Babinet, 11-11-2020

This program takes in a file name of flashcards and quizzes the user on all the flashcards that are due.

How to Use:
In the command line, call the program followed by the flashcard text file you would like to use.
Proper formatting for the flashcard file is one card per line where each card is:
date,frontText,backText

Following this, you will be prompted with what you would like to do next. Your options are:
quiz: this will display all of the flashcards in the deck one at a time. First the front text will be displayed, then once you press enter, the back text will be displayed, you then have the option to press 1 if you got it right or 2 if you didn't
save: if you choose save you will be prompted to enter a file name to save the flashcard deck to 
exit: this will exit you out of the program
*/

public class FlashcardDisplayer{
  FlashcardPriorityQueue queue;
  /**
  * Creates a flashcard displayer with the flashcards in file.
  * File has one flashcard per line. On each line, the date the flashcard 
  * should next be shown is first (format: YYYY-MM-DDTHH-MM), followed by a comma, 
  * followed by the text for the front of the flashcard, followed by another comma,
  * followed by the text for the back of the flashcard. You can assume that the 
  * front/back text does not itself contain commas. (I.e., a properly formatted file
  * has exactly 2 commas per line.)
  */
  public FlashcardDisplayer(String file){
    List<Flashcard> fcList = load(file);
    queue = new FlashcardPriorityQueue(fcList);
  }
  //helper constructor that does nothing, but allows for their to be a FlashcardDisplayer object
  public FlashcardDisplayer(){
  }
  //reads the file and turns it into a list of flashcards to be used in the constructor
  private List<Flashcard> load(String fileName){
    List<Flashcard> fcList = new ArrayList<Flashcard>();
    File importFile = new File(fileName);

    Scanner s = null;
    try {
      s = new Scanner(importFile);
    } catch (FileNotFoundException e) {
      System.err.println(e);
      System.exit(1);
    }
    while(s.hasNextLine()){
      String[] nextLine = s.nextLine().split(",");
      Flashcard card = new Flashcard(nextLine[0], nextLine[1], nextLine[2]);
      fcList.add(card);
    }
    return fcList;
  }
  /*
  Creates the text file that the html will be in. Adapted from http://www.cs.carleton.edu/faculty/arafferty/cs201/assignments/hw09-wordCloud/Z05_CARR1695_05_SE_SUP2.pdf
  */
  public static boolean createTextFile(String fileName, List<Flashcard> fcList){
    boolean fileOpened = true;
    PrintWriter toFile = null;
    try{
      toFile = new PrintWriter(fileName);
    }catch(FileNotFoundException e){
      fileOpened = false;
    }
    if(fileOpened){
      for(Flashcard fc: fcList){
        toFile.println(fc.getDueDate() + "," + fc.getFrontText() + "," + fc.getBackText());
      }
      toFile.close();
    }
    return fileOpened;
  }
  /**
  * Writes out all flashcards to a file so that they can be loaded
  * by the FlashcardDisplayer(String file) constructor. Returns true
  * if the file could be written. The FlashcardDisplayer should still
  * have all of the same flashcards after this method is called as it
  * did before the method was called. However, it may be that flashcards
  * with the same exact next display date and time are removed in a different order.
  */
  public boolean saveFlashcards(String outFile){
    List<Flashcard> list = queue.deckToList();
    return createTextFile(outFile, list);
  }
  
  /**
  * Displays any flashcards that are currently due to the user, and 
  * asks them to report whether they got each card correct. If the
  * card was correct, it is added back to the deck of cards with a new
  * due date that is one day later than the current date and time; if
  * the card was incorrect, it is added back to the card with a new due
  * date that is one minute later than that the current date and time.
  */
  public void displayFlashcards(){
    LocalDateTime curTime = LocalDateTime.now();
    Scanner s = new Scanner(System.in);
    //checks if the card ontop is due
    while(queue.peek().getDueDate().compareTo(curTime) <= 0){
      Flashcard curCard = queue.poll();
      System.out.println("Card:");
      System.out.println(curCard.getFrontText());
      System.out.println("[Press return to see the back of the card]");
      boolean done = false;
      //waits for the user to press return
      do{    
        String input = s.nextLine();
        if(input.equals("")){
          done = true;
        }
      }
      while(!done);
      System.out.println(curCard.getBackText());
      System.out.println("Press 1 if you got the card correct and 2 if you got the card incorrect");
      done = false;
      int intInput = 0;
      String input = s.nextLine();
      intInput = Integer.parseInt(input);
      do{
        //if the user got it right
        if (intInput == 1){
          String newTime = curTime.plusDays(1).toString();
          Flashcard newCard = new Flashcard(newTime, curCard.getFrontText(), curCard.getBackText());
          queue.add(newCard);
          done = true;
        //if the user got it wrong
        }else if(intInput == 2){
          String newTime = curTime.plusMinutes(1).toString();
          Flashcard newCard = new Flashcard(newTime, curCard.getFrontText(), curCard.getBackText());
          queue.add(newCard);
          done = true;
        }else{
        }
      }while(!done);
    }
    s.close();
    System.out.println("No cards are left");
  }
  public static void main(String[] args){
    FlashcardDisplayer displayer = new FlashcardDisplayer();   
    //if the correct number of arguments are given
    if (args.length == 1){
      System.out.println("Welcome! One flashcard will displayed at a time. Once you have an answer, you can view the back to see if it is correct and then respond with 1 if you got it right, or 2 if you did not");    
      String file = args[0];
      FlashcardDisplayer tmpDisplayer = new FlashcardDisplayer(file);
      displayer = tmpDisplayer;
    }else if(args.length == 0 || args.length > 1){
      System.out.println("It appears an invalid number of command line arguments was given. Try again, with just the flashcard file name you would like to use.");
      System.exit(1);
    }
    boolean out = false;
    Scanner s = new Scanner(System.in);
    do{
      System.out.println("Enter a command: ");
      //get user input
      String input = s.nextLine();
      switch (input){
        case "quiz":
          displayer.displayFlashcards();
          break;
        case "save":
          System.out.println("Please enter the file name you would like to use (with .txt file signature): ");
          String file = s.nextLine();
          displayer.saveFlashcards(file);
          break;
        case "exit":
          System.out.println("Thank you for practicing! Have a good day :)");
          out = true;
          break;
        default:
          System.out.println("It seems you did not enter a valid operation. Please check the documentation at the top of the file");
      }
    }while (!out); 
    s.close();
  }
}
