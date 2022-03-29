package exercice4;

import java.awt.Color;

// 
//	(space setColor black)  
//	(robi setColor yellow) 
//	(space sleep 2000) 
//	(space setColor white)  
//	(space sleep 1000) 	
//	(robi setColor red)		  
//	(space sleep 1000)
//	(robi translate 100 0)
//	(space sleep 1000)
//	(robi translate 0 50)
//	(space sleep 1000)
//	(robi translate -100 0)
//	(space sleep 1000)
//	(robi translate 0 -40) ) 
//

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import graphicLayer.GRect;
import graphicLayer.GSpace;
import stree.parser.SNode;
import stree.parser.SParser;
import tools.Tools;

public class Exercice4_1_0 {
	// Une seule variable d'instance
	Environment environment = new Environment();

	public Exercice4_1_0() {
		// space et robi sont temporaires ici
		GSpace space = new GSpace("Exercice 4", new Dimension(200, 100));
		GRect robi = new GRect();

		space.addElement(robi);
		space.open();

		Reference spaceRef = new Reference(space);
		Reference robiRef = new Reference(robi);

		
		// Initialisation des references : on leur ajoute les primitives qu'elles
		// comprenent
		spaceRef.addCommand("setColor", new GSpaceSetColor());
		spaceRef.addCommand("sleep", new GSpaceSleep());
		
		robiRef.addCommand("setColor", new GRectSetColor());
		robiRef.addCommand("translate", new GRectTranslate());
		
	

		// Enrigestrement des references dans l'environement par leur nom
		environment.addReference("space", spaceRef);
		environment.addReference("robi", robiRef);

		this.mainLoop();
	}

	private void mainLoop() {
		while (true) {
			// prompt
			System.out.print("> ");
			// lecture d'une serie de s-expressions au clavier (return = fin de la serie)
			String input = Tools.readKeyboard();
			// creation du parser
			SParser<SNode> parser = new SParser<>();
			// compilation
			List<SNode> compiled = null;
			try {
				compiled = parser.parse(input);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// execution des s-expressions compilees
			Iterator<SNode> itor = compiled.iterator();
			while (itor.hasNext()) {
				this.run((SNode) itor.next());
			}
		}
	}

	private void run(SNode expr) {
		// quel est le nom du receiver
		String receiverName = expr.get(0).contents();
		// quel est le receiver
		Reference receiver = environment.getReferenceByName(receiverName);
		// demande au receiver d'executer la s-expression compilee
		receiver.run(expr);
	}

	public static void main(String[] args) {
		new Exercice4_1_0();
	}
	
	public interface Command {
		abstract public Reference run(Reference receiver, SNode method);
	}
	
	public class Reference{
		Object receiver;
		Map<String, Command> primitives;
		
		public Reference(Object receiver) {
			this.receiver = receiver;
			primitives = new HashMap<String,Command>();
		}
		
		public Command getCommandByName(String selector) {
			return this.primitives.get(selector);
		}
		
		public void addCommand(String selector,Command primitive) {
			if(!this.primitives.containsKey(selector)) {
				this.primitives.put(selector, primitive);
			}
		}
		
		public Reference run(SNode method) {
			return this.getCommandByName(method.get(1).contents()).run(this, method);
			
		}
		
	}
	
	public class Environment{
		HashMap<String,Reference> variables;
		
		public Environment(){
			this.variables = new HashMap<String,Reference>();
		}
		
		public void addReference(String name, Reference ref) {
			if(!this.variables.containsKey(name)) {
				this.variables.put(name, ref);
			}
		}
		
		public Reference getReferenceByName(String name) {
			return this.variables.get(name);
		}
		
	}


	public class GSpaceSetColor implements Command{
		@Override
		public Reference run(Reference receiver, SNode method) {
			try {
				((GSpace)(receiver.receiver)).setColor((Color)(Color.class.getDeclaredField(method.get(2).contents()).get(null)));
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {

				e.printStackTrace();
			}
			return receiver;
		}
		
	}
	
	public class GRectSetColor implements Command{
		
		@Override
		public Reference run(Reference receiver, SNode method) {
			try {
				((GRect)(receiver.receiver)).setColor((Color)(Color.class.getDeclaredField(method.get(2).contents()).get(null)));
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
			return receiver;
		}
	}
	
	
	public class GSpaceSleep implements Command{
		
		@Override
		public Reference run(Reference receiver, SNode method) {
				try {
					Thread.sleep(Integer.valueOf(method.get(2).contents()));
				} catch (NumberFormatException | InterruptedException e) {
					e.printStackTrace();
				}
			return receiver;
		}
	}
	
	public class GRectTranslate implements Command{
		@Override
		public Reference run(Reference receiver, SNode method) {
			int dx = Integer.valueOf(method.get(2).contents());
			int dy = Integer.valueOf(method.get(3).contents());
			((GRect)receiver.receiver).translate(new Point(dx,dy));
			return receiver;
		}
	}
	
	
}


