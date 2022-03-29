package exercice4;
//import exercice3_0.RobiChangeColor;
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.lang.reflect.Field;
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
	Command changeRC =new SetColor();//ref change couleur
	Command sleepcmd =new Sleep();
	
	//classe enfoui translate
	static public class Translate implements Command {

		@Override
		public Reference run(Reference receiver, SNode method) {
			//grect avant
			GElement robi = (GElement) receiver.receiver;
			//exception
			int x=Integer.parseInt(method.get(2).contents());
			int y=Integer.parseInt(method.get(3).contents());
			robi.translate(new Point(x,y));


			return receiver;
		}
	}

	//classe enfoui sleep 
	static public class Sleep implements Command {

		@Override
		public Reference run(Reference receiver, SNode method) {

			if(method.get(0).contents().equals("space")) {
				GSpace space = (GSpace) receiver.receiver;
				try {
					Thread.sleep(Integer.parseInt(method.get(2).contents()));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			return receiver;
		}
	}
	static public class SetColor implements Command {

		@Override
		public Reference run(Reference receiver, SNode method) {

				GElement robi = (GElement) receiver.receiver;
				Color color;
				try {
					Field field = Class.forName("java.awt.Color").getField(method.get(2).contents());
					color = (Color)field.get(null);
					robi.setColor(color);
					return receiver;
				} catch (Exception e) {
					return null; // Not defined
				}
			
		}
	}
	//classe enfoui commande
	public interface Command {
		// le receiver est l'objet qui va executer method
		// method est la s-expression resultat de la compilation
		// du code source a executer
		// exemple de code source : "(space setColor black)"
		abstract public Reference run(Reference receiver, SNode method);
	}
	//classe enfouie ref
	static public class Reference {
		Object receiver;
		Map<String, Command> primitives;
		public Reference(Object receiver) {
			this.receiver = receiver;
			primitives = new HashMap<String, Command>();
		} 
		public void addCommand(String selector,Command primitive) {
			primitives.put(selector, primitive);
		}
		public Command getCommandByName(String selector) {
			//pas dedans retourne null par defaut
			return primitives.get(selector);
		}
		@SuppressWarnings("unlikely-arg-type")
		public Reference run (SNode Method) {
			//pas dedans retourne null par defaut
			if(primitives.containsKey(Method.get(1).contents())) {
				Command cmd=(primitives.get((Method.get(1).contents())));	
				return cmd.run(this, Method);
			}
			
			return null;
		}
		public Object getReceiver() {return receiver; }

	}
	static public class Environment {
		HashMap<String, Reference> variables;
		public Environment() {
			variables = new HashMap<String, Reference>();
		} 
		public void addReference(String name,Reference ref) {
			variables.put(name,ref);
		}
		public Reference getReferenceByName(String name) {
			//retourne une ref de l'environnement //ou null
			return variables.get(name);
		}
	}


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

		//référence robot primitive
		robiRef.addCommand("setColor",changeRC);
		robiRef.addCommand("translate",new Translate());
		//reference space primitive
		spaceRef.addCommand("sleep",new Sleep());
		spaceRef.addCommand("setColor",changeRC);
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

}