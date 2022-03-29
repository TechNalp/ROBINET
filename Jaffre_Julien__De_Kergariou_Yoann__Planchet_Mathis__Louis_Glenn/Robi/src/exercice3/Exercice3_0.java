package exercice3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import graphicLayer.GRect;
import graphicLayer.GSpace;
import stree.parser.SNode;
import stree.parser.SParser;

public class Exercice3_0 {
	GSpace space = new GSpace("Exercice 3", new Dimension(200, 100));
	GRect robi = new GRect();
	String script = "" +
	"   (space setColor black) " +
	"   (robi setColor yellow)" +
	"   (space sleep 1000)" +
	"   (space setColor white)\n" + 
	"   (space sleep 1000)" +
	"	(robi setColor red) \n" + 
	"   (space sleep 1000)" +
	"	(robi translate 100 0)\n" + 
	"	(space sleep 1000)\n" + 
	"	(robi translate 0 50)\n" + 
	"	(space sleep 1000)\n" + 
	"	(robi translate -100 0)\n" + 
	"	(space sleep 1000)\n" + 
	"	(robi translate 0 -40)";

	public Exercice3_0() {
		space.addElement(robi);
		space.open();
		this.runScript();
	}

	private void runScript() {
		SParser<SNode> parser = new SParser<>();
		List<SNode> rootNodes = null;
		try {
			rootNodes = parser.parse(script);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Iterator<SNode> itor = rootNodes.iterator();
		while (itor.hasNext()) {
			this.run(itor.next());
		}
	}

	private void run(SNode expr) {
		Command cmd = getCommandFromExpr(expr);
		if (cmd == null)
			throw new Error("unable to get command for: " + expr);
		cmd.run();
	}

	Command getCommandFromExpr(SNode expr) {
		SNode objet = expr.get(0);
		SNode fonction = expr.get(1);
		
		if(objet.contents().compareTo("space") == 0) {
			if(fonction.contents().compareTo("setColor") == 0) {
				try {
					SNode parametre = expr.get(2);
					
					SpaceChangeColor c = new SpaceChangeColor((Color)(Color.class.getDeclaredField(parametre.contents()).get(null)));
					
					return c;
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				SNode parametre = expr.get(2);
				
				SpaceSleep s = new SpaceSleep(Integer.parseInt(parametre.contents()));
				
				return s;
			}
		}
		else {
			if(fonction.contents().compareTo("setColor") == 0) {
				try {
					SNode parametre = expr.get(2);
					
					RobiChangeColor c = new RobiChangeColor((Color)(Color.class.getDeclaredField(parametre.contents()).get(null)));
					
					return c;
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				SNode parametre1 = expr.get(2);
				SNode parametre2 = expr.get(3);
				
				RobiTranslate t = new RobiTranslate(Integer.parseInt(parametre1.contents()), Integer.parseInt(parametre2.contents()));
				
				return t;
			}
		}
		
		return null;
	}

	public static void main(String[] args) {
		new Exercice3_0();
	}

	public interface Command {
		abstract public void run();
	}

	public class SpaceChangeColor implements Command {
		Color newColor;

		public SpaceChangeColor(Color newColor) {
			this.newColor = newColor;
		}

		@Override
		public void run() {
			space.setColor(this.newColor);
		}

	}
	
	public class SpaceSleep implements Command {
		int newSleep;
		
		public SpaceSleep(int newSleep) {
			this.newSleep = newSleep;
		}
		
		@Override
		public void run() {
			try {
				Thread.sleep(this.newSleep);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public class RobiChangeColor implements Command {
		Color newColor;

		public RobiChangeColor(Color newColor) {
			this.newColor = newColor;
		}

		@Override
		public void run() {
			robi.setColor(this.newColor);
		}

	}
	
	public class RobiTranslate implements Command {
		int newX;
		int newY;

		public RobiTranslate(int newX, int newY) {
			this.newX = newX;
			this.newY = newY;
		}

		@Override
		public void run() {
			robi.translate(new Point(this.newX, this.newY));
		}

	}
}