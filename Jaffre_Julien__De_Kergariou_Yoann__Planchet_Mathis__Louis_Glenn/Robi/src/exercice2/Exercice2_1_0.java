package exercice2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import graphicLayer.GRect;
import graphicLayer.GSpace;

import stree.parser.SNode;
import stree.parser.SParser;


public class Exercice2_1_0 {
	
	private static Map<String,Object> elts = new HashMap<>(); //Contient la liste des élements (et leur nom) qui peuvent être affecté par une instruction
	
	GSpace space = new GSpace("Exercice 2_1", new Dimension(200, 100));
	GRect robi = new GRect();
	//String script = "(space setColor black) (robi setColor yellow)";
	String script = "(space color white)\n"
			+ "(robi color red)\n"
			+ "(robi translate 10 0)\n"
			+ "(space sleep 100)\n"
			+ "(robi translate 0 10)\n"
			+ "(space sleep 100)\n"
			+ "(robi translate -10 0)\n"
			+ "(space sleep 100)\n"
			+ "(robi translate 0 -10)\n";
	
	public Exercice2_1_0() {
		space.addElement(robi);
		space.open();
		Exercice2_1_0.elts.put("space", space); //On ajout space à la liste des éléments qui peuvent être affecté par une instruction
		Exercice2_1_0.elts.put("robi", robi); //On ajout robi à la liste des éléments qui peuvent être affecté par une instruction
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

		if(expr.isLeaf()) {
			System.err.print("Une S Expression n'est pas valide");
			System.exit(0);
		}
		
		
		String applyToStr = expr.get(0).contents(); // On récupère le nom de l'objet qui va "subir" l'instruction
		String cmd = expr.get(1).contents(); // On récupère la commande qu'il faudra appliquer
		
		Object applyTo = Exercice2_1_0.elts.get(applyToStr); // On récupère à partir de la hashmap l'objet associé au nom trouvé
		if(applyTo == null) {
			System.err.println(applyToStr+" n'est pas un nom d'objet valide");
			return;
		}
		
		if(applyTo instanceof GSpace) {
			GSpace gs = (GSpace)applyTo;
			if(cmd.equals("setColor") || cmd.equals("color")) {
				try {
					gs.setColor((Color)(Color.class.getDeclaredField(expr.get(2).contents()).get(null))); // Permet de convertir directement le nom de la couleur dans la commande en la valeur de l'attribut static ayant ce nom dans la classe Color
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e) {
					e.printStackTrace();
				}	
			}
			
			
			if(cmd.equals("sleep")) {
				try {
					Thread.sleep(Integer.valueOf(expr.get(2).contents()));
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if(cmd.equals("translate")) {
				System.err.println("Impossible de translater un GSpace");
			}
			
			
			
		}else if(applyTo instanceof GRect) {
			GRect gr = (GRect)applyTo;

			if(cmd.equals("setColor") || cmd.equals("color")) {
				try {
					gr.setColor((Color)(Color.class.getDeclaredField(expr.get(2).contents()).get(null))); // Permet de convertir directement le nom de la couleur dans la commande en la valeur de l'attribut static ayant ce nom dans la classe Color
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e) {
					e.printStackTrace();
				}
				
			}
			
			if(cmd.equals("translate")) {
				if(expr.size()-2<2) {
					System.err.println("Nombre d'argument invalide");
				}else {
					int dx = Integer.valueOf(expr.get(2).contents());
					int dy = Integer.valueOf(expr.get(3).contents());
					gr.translate(new Point(dx,dy));
				}
			}
			
			if(cmd.equals("sleep")) {
				System.err.println("Impossible de faire sleep sur un GRect");
			}
			
		}
		
		
		
	}

	public static void main(String[] args) {
		new Exercice2_1_0();
	}

}