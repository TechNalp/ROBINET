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

import graphicLayer.GElement;
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
		
		spaceRef.addCommand("setColor", new SpaceChangeColor());
		spaceRef.addCommand("sleep", new SpaceSleep());
		
		robiRef.addCommand("setColor", new RobiChangeColor());
		robiRef.addCommand("translate", new RobiTranslate());

		// Initialisation des references : on leur ajoute les primitives qu'elles
		// comprenent
		//
		// <A VOUS DE CODER>
		//

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
		// le receiver est l'objet qui va executer method
		// method est la s-expression resultat de la compilation
		// d u code source a executer
		// exemple de code source : "(space setColor black)"
		
		abstract public Reference run(Reference receiver, SNode method);
	}
	
	public class SpaceChangeColor implements Command {
		@Override
		public Reference run(Reference receiver, SNode method) {
			SNode param = method.get(2);
			
			try {
				((GSpace)receiver.receiver).setColor((Color)(Color.class.getDeclaredField(param.contents()).get(null)));
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return receiver;
		}
	}
	
	public class SpaceSleep implements Command {
		@Override
		public Reference run(Reference receiver, SNode method) {
			SNode param = method.get(2);
			
			try {
				Thread.sleep(Integer.parseInt(param.contents()));
			} catch (NumberFormatException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return receiver;
		}
		
		
	}
	
	public class RobiChangeColor implements Command {
		@Override
		public Reference run(Reference receiver, SNode method) {
			SNode param = method.get(2);
			
			try {
				((GRect)receiver.receiver).setColor((Color)(Color.class.getDeclaredField(param.contents()).get(null)));
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return receiver;
		}
	}
	
	public class RobiTranslate implements Command {
		@Override
		public Reference run(Reference receiver, SNode method) {
			SNode param1 = method.get(2);
			SNode param2 = method.get(3);
			
			((GRect)receiver.receiver).translate(new Point(Integer.parseInt(param1.contents()), Integer.parseInt(param2.contents())));
			
			return null;
		}

	}
	
	public class Reference {
		Object receiver;
		Map<String, Command> primitives;
		
		public Reference(Object receiver) {
			this.receiver = receiver;
			primitives = new HashMap<String, Command>();
		}
		
		Command getCommandByName(String selector) {
			return this.primitives.get(selector);
		}
		
		void addCommand(String selector, Command primitive) {
			this.primitives.putIfAbsent(selector, primitive);
		}
		
		Reference run(SNode methode) {
			SNode fonc = methode.get(1);
			Command com;
			
			com = this.primitives.get(fonc.contents());
			
			com.run(this, methode);
			
			return this;
		}
	}
	
	public class Environment {
		HashMap<String, Reference> variables;
		
		public Environment() {
			variables = new HashMap<String, Reference>();
		}
		
		void addReference(String name, Reference nameRef) {
			this.variables.putIfAbsent(name, nameRef);
		}
		
		Reference getReferenceByName(String name) {
			return this.variables.get(name);
		}
	}
}