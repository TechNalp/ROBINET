package exercice2;

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
import stree.parser.SPrinter;

/*
	public class Exercice2_1_0 {
		GSpace space = new GSpace("Exercice 2_1", new Dimension(200, 100));
		GRect robi = new GRect();
		String script = "(space setColor black) (robi setColor yellow)";
	
		public Exercice2_1_0() {
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
			// A compléter...
			
			SNode objet = expr.get(0);
			SNode parametre = expr.get(2);
			
			if(objet.contents().compareTo("space") == 0) {
				try {
					space.setColor((Color)(Color.class.getDeclaredField(parametre.contents()).get(null)));
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				try {
					robi.setColor((Color)(Color.class.getDeclaredField(parametre.contents()).get(null)));
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	
		public static void main(String[] args) {
			new Exercice2_1_0();
		}
	
	}
*/

public class Exercice2_1_0 {
	GSpace space = new GSpace("Exercice 2_1", new Dimension(200, 100));
	GRect robi = new GRect();
	String script = "(space color white) (robi color red) (robi translate 10 0) (space sleep 1000) (robi translate 0 10) (robi translate -10 0) (space sleep 1000) (robi translate 0 -10)";

	public Exercice2_1_0() {
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
		SNode objet = expr.get(0);
		SNode fonction = expr.get(1);
		
		if(objet.contents().compareTo("space") == 0) {
			SNode parametre = expr.get(2);
			if(fonction.contents().compareTo("color") == 0) {
				try {
					space.setColor((Color)(Color.class.getDeclaredField(parametre.contents()).get(null)));
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				try {
					Thread.sleep(Integer.parseInt(parametre.contents()));
				} catch (NumberFormatException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else {
			if(fonction.contents().compareTo("color") == 0) {
				SNode parametre = expr.get(2);
				try {
					robi.setColor((Color)(Color.class.getDeclaredField(parametre.contents()).get(null)));
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				SNode parametre1 = expr.get(2);
				SNode parametre2 = expr.get(3);
				
				int x = Integer.parseInt(parametre1.contents());
				int y = Integer.parseInt(parametre2.contents());
				
				robi.translate(new Point(x, y));
			}
		}
	}

	public static void main(String[] args) {
		new Exercice2_1_0();
	}

}