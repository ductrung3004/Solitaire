import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.*;

import javax.swing.JComponent;

/* This is GUI component with a embedded
 * data structure. This structure is a mixture
 * of a queue and a stack
 */
class CardStack extends JComponent
{
	protected final int NUM_CARDS = 52;
	protected Deque<Card> v;
	protected boolean playStack = false;
	protected int SPREAD = 25;
	protected int _x = 0;
	protected int _y = 0;

	public CardStack(boolean isDeck)
	{
		this.setLayout(null);
		v = new LinkedList<Card>();
		if (isDeck){
			// set deck position
			for (Card.Suit suit : Card.Suit.values())
			{
				for (Card.Value value : Card.Value.values())
				{
					v.add(new Card(suit, value));
				}
			}
		}
		else{
			playStack = true;
		}
	}

	public boolean empty()
	{
		if (v.isEmpty())
			return true;
		else
			return false;
	}

	public void putFirst(Card c)
	{
		v.addFirst(c);
	}

	public Card getFirst()
	{
		if (!empty())
			return v.getFirst();
		else
			return null;
	}

	// analogous to peek()
	public Card getLast()
	{
		if (!empty())
			return v.getLast();
		else
			return null;
	}

	// queue-like functionality
	public Card popFirst()
	{
		if (!empty())
			return v.removeFirst();
		else
			return null;

	}

	public void push(Card c){
		v.add(c);
	}

	public Card pop() {
		if (!empty())
			return v.removeLast();
		else
			return null;
	}

	// shuffle the cards
	public void shuffle() {
		Vector<Card> v = new Vector<Card>();
		while (!empty()) {
			v.add(pop());
		}
		while (!v.isEmpty()) {
			Card c = v.elementAt((int) (Math.random() * v.size()));
			this.push(c);
			v.removeElement(c);
		}

	}

	public int showSize()
	{
		//System.out.println("Stack Size: " + v.size());
		return v.size();
	}


	public void makeEmpty()
	{
		while (!this.empty())
		{
			this.popFirst();
		}
	}

	@Override
	public boolean contains(Point p)
	{
		Rectangle rect = new Rectangle(_x, _y, Card.CARD_WIDTH + 10, Card.CARD_HEIGHT * 9);
		return (rect.contains(p));
	}

	public void setXY(int x, int y)
	{
		_x = x;
		_y = y;
		// System.out.println("CardStack SET _x: " + _x + " _y: " + _y);
		setBounds(_x, _y, Card.CARD_WIDTH + 10, Card.CARD_HEIGHT * 3);
	}

	public Point getXY()
	{
		// System.out.println("CardStack GET _x: " + _x + " _y: " + _y);
		return new Point(_x, _y);
	}

	@Override
	protected void paintComponent(Graphics g) {
		removeAll();
		if (playStack)
		{
			Iterator<Card> iter = v.iterator();
			Point prev = new Point(); // positioning relative to the container
			Point prevWhereAmI = new Point();// abs positioning on the board
			if (iter.hasNext()) {
				Card c = iter.next();
				// this origin is point(0,0) inside the cardstack container
				prev = new Point();// c.getXY(); // starting deck pos
				add(Solitaire.moveCard(c, prev.x, prev.y));
				// setting x & y position
				c.setWhereAmI(getXY());
				prevWhereAmI = getXY();
			}
			else {
				removeAll();
			}

			for (; iter.hasNext();)
			{
				Card c = iter.next();
				c.setXY(new Point(prev.x, prev.y + SPREAD));
				add(Solitaire.moveCard(c, prev.x, prev.y + SPREAD));
				prev = c.getXY();
				// setting x & y position
				c.setWhereAmI(new Point(prevWhereAmI.x, prevWhereAmI.y + SPREAD));
				prevWhereAmI = c.getWhereAmI();
			}

		}
	}
}// END CardStack