import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Flashcard implements Comparable<Flashcard>{
  private String front;
  private String back;
  private String dueDate;
  /**
  * Creates a new flashcard with the given dueDate, text for the front
  * of the card (front), and text for the back of the card (back).
  * dueDate must be in the format YYYY-MM-DDTHH:MM. For example,
  * 2020-05-04T13:03 represents 1:03PM on May 4, 2020. It's
  * okay if this method crashes if the date format is incorrect.
  * In the format above, the time may or may not include milliseconds. 
  * The parse method in LocalDateTime can deal with this situation
  *  without any changes to your code.
  */
  public Flashcard(String dueDate, String front, String back){
    this.front = front;
    this.back = back;
    this.dueDate = dueDate;
  }

  public int compareTo(Flashcard fc){
    return this.getDueDate().compareTo(fc.getDueDate());
  }
  
  /**
  * Gets the text for the front of this flashcard.
  */
  public String getFrontText(){
    return front;
  }
  
  /**
  * Gets the text for the Back of this flashcard.
  */
  public String getBackText(){
    return back;
  }
  
  /**
  * Gets the time when this flashcard is next due.
  */
  public LocalDateTime getDueDate(){
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME; 
    LocalDateTime time = LocalDateTime.parse(dueDate, formatter);
    return time;
  }
}
