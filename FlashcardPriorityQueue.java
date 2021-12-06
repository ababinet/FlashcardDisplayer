import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class FlashcardPriorityQueue implements PriorityQueue<Flashcard>{
  private Flashcard[] heap;
  private int size;
  private int maxSize;
  /**
  * Creates an empty priority queue.
  */
  public FlashcardPriorityQueue(){
    size = 0;
    maxSize = 10;
    heap = new Flashcard[10];
  }
  /**
  * Creates a priority queue filled with the flashcards from the given list
  */
  public FlashcardPriorityQueue(List<Flashcard> flashcardList){
    size = 0;
    maxSize = 10;
    heap = new Flashcard[10];
    for(int i = 0; i < flashcardList.size(); i++){
      add(flashcardList.get(i));
    }
  }
  //increase the size of the array if needed
  private void addSpace(){
    Flashcard[] temp = new Flashcard[maxSize*2];
    for(int i = 0; i < size; i ++){
      temp[i] = heap[i];
    }
    heap = temp;
    maxSize = maxSize*2;
  }
  /** Adds the given item to the queue. */
  public void add(Flashcard item){
    if (size == maxSize){ //check if there is enough space
      addSpace();
    }
    //check if it's the first item
    if (size == 0){
      heap[0] = item;
      size ++;
    }else{
      boolean found = false;
      int index = size;
      while(index > 0 && !found){
        int parent = (index-1)/5;
        //compare to parent
        if(heap[parent].compareTo(item) > 0){
          heap[index] = heap[parent];
          index = parent;
        }
        else{
          found = true;
        }
      }
      heap[index] = item;
      size ++;
    }
  }
  
  /** Removes the first item according to compareTo from the queue, and returns it.
  * Throws a NoSuchElementException if the queue is empty.
  */
  public Flashcard poll(){
    if(size == 0){
      throw new NoSuchElementException("The Flashcard deck is empty. There are no Flashcards to return");
    }else{ //if not empty
      Flashcard returnCard = heap[0];
      heap[0] = heap[size-1]; //put last card in first index
      heap[size-1]= null;
      size --;
      boolean found = false;
      int index = 0;
      //as long as the spot has not been found
      while(index < size && !found){
        found = true;
        boolean needToSwap = false;
        int swap = (5*index)+1; //first child is place holder
        for(int i = 1; i < 6; i++){ //go through all the children and find the smallest child
          if((5*index)+i < size){
            Flashcard child = heap[(5*index)+i];
            if(heap[index].compareTo(child) > 0 && child.compareTo(heap[swap]) <= 0){
              swap = 5*index+i;
              found = false;
              needToSwap = true;
            }
          }
        }
        //if a swap was found
        if(needToSwap){
          Flashcard tmp = heap[swap];
          heap[swap] = heap[index];
          heap[index] = tmp;
          index = swap;
        }
      }
      return returnCard;
    }
  }
  
  /** Returns the first item according to compareTo in the queue, without removing it.
    * Throws a NoSuchElementException if the queue is empty.
    */
  public Flashcard peek(){
    if (size == 0){
      throw new NoSuchElementException("The Flashcard deck is empty. There are no Flashcards to return");
    }else{
      return heap[0];
    }
  }
  
  /** Returns true if the queue is empty. */
  public boolean isEmpty(){
    if(size !=0){
      return false;
    }else{
      return true;
    }
  }
  
  /** Removes all items from the queue. */
  public void clear(){
    heap = new Flashcard[10];
    size = 0;
    maxSize = 10;
  }
  //Helper method for tests to print out all the cards in the queue
  private void printDeck(){
    for(int i = 0; i < size; i++){
      System.out.println(heap[i].getDueDate() + "," + heap[i].getFrontText() + "," + heap[i].getBackText());
    } 
  }
  //Helper method for tests to check if the structure of queue is correct
  private boolean checkStructure(){
    boolean correct = true;
    for(int i = 1; i < size; i++){
      if (heap[i].compareTo(heap[(i-1)/5]) < 0){
        correct = false;
      }
    }
    return correct;
  } 
  public List<Flashcard> deckToList(){
    List<Flashcard> list = new ArrayList<Flashcard>();
    for(int i = 0; i < size; i++){
      list.add(heap[i]);
    } 
    return list;
  }
  //When run from the command line with no arguments, displays tests of all of the methods in the FlashcardPriorityQueue class
  public static void main(String[] args){
    //set up the cards to use for the tests
    List<Flashcard> flashcardList = new ArrayList<Flashcard>();
    int year = 2110;
    for(int i = 0; i < 10; i++){
      year = year - 10;
      Flashcard card = new Flashcard(year + "-01-01T01:01:01", year + "front", year + "back");
      flashcardList.add(card);
    }
    FlashcardPriorityQueue testDeck = new FlashcardPriorityQueue(flashcardList);
    //Adding in a duplicate and adding when there is not enough room
    System.out.println("Original deck ordering:");
    System.out.println("Number of cards: " + testDeck.size);
    System.out.println("Max deck size: " + testDeck.maxSize);
    System.out.println("Structure of deck is correct: " + testDeck.checkStructure());
    testDeck.printDeck();
    Flashcard card = new Flashcard(2030 + "-01-01T01:01:01", 2030 + "front", 2030 + "back");
    testDeck.add(card);
    System.out.println("After duplicate:");
    System.out.println("Number of cards: " + testDeck.size);
    System.out.println("Max deck size: " + testDeck.maxSize);
    System.out.println("Structure of deck is correct: " + testDeck.checkStructure());
    testDeck.printDeck();
    //Removing an item
    Flashcard fcPeek = testDeck.peek();
    Flashcard fcPoll = testDeck.poll();
    System.out.println("After removing:");
    System.out.println("Number of cards: " + testDeck.size);
    System.out.println("Max deck size: " + testDeck.maxSize);
    System.out.println("Structure of deck is correct: " + testDeck.checkStructure());
    System.out.println("Removed flashcard was the smallest one: " + (fcPeek == fcPoll));
    testDeck.printDeck();
    //isEmpty and clear
    System.out.println("The deck is empty before clearing: " + testDeck.isEmpty());
    testDeck.clear();
    System.out.println("The deck is empty after clearing: " + testDeck.isEmpty());
  }
}