package panels.text;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * authors: Logan/sam from assignment 3
 * Class used to limit the number of characters allowed in the JTextAreas
 * in TextPanel, from 
 * http://stackoverflow.com/questions/3519151/how-to-limit-the-number-of-characters-in-jtextfield
 */
public class JTextFieldLimit extends PlainDocument {
  
	private static final long serialVersionUID = 1L;
	private int limit;
	
	//sets the text limit
	JTextFieldLimit(int limit) {
		super();
		this.limit = limit;
	}
	
	//inserts a string to the plaindocument
	public void insertString( int offset, String  str, javax.swing.text.AttributeSet attr ) throws BadLocationException {
		if (str == null) return;

		if ((getLength() + str.length()) <= limit) {
			super.insertString(offset, str, attr);
		}
	}
}