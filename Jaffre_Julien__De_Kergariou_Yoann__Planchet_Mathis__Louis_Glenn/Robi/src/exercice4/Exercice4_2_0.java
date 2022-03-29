package exercice4;

 import java.awt.Color;

/*
	(space setColor black)  
	(robi setColor yellow) 
	(space sleep 2000) 
	(space setColor white)  
	(space sleep 1000) 	
	(space add robi (GRect new))
	(robi setColor green)
	(robi translate 100 50)
	(space del robi)
	(robi setColor red)		  
	(space sleep 1000)
	(robi translate 100 0)
	(space sleep 1000)
	(robi translate 0 50)
	(space sleep 1000)
	(robi translate -100 0)
	(space sleep 1000)
	(robi translate 0 -40) ) 
	
	
(space add robi (rect.class new))
(robi translate 130 50)
(robi setColor yellow)
(space add momo (oval.class new))
(momo setColor red)
(momo translate 80 80)
(space add pif (image.class new alien.gif))
(pif translate 100 0)
(space add hello (label.class new "Hello world"))
(hello translate 10 10)
(hello setColor black)

*/


import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import graphicLayer.GBounded;
import graphicLayer.GElement;
import graphicLayer.GImage;
import graphicLayer.GOval;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import graphicLayer.GString;
import stree.parser.SNode;
import stree.parser.SParser;
import tools.Tools;



class NewElement implements Command {
	public Reference run(Reference reference, SNode method) {
		try {
			@SuppressWarnings("unchecked")
			GElement e = ((Class<GElement>) reference.getReceiver()).getDeclaredConstructor().newInstance();
			Reference ref = new Reference(e);
			ref.addCommand("setColor", new SetColor());
			ref.addCommand("translate", new Translate());
			ref.addCommand("setDim", new SetDim());
			return ref;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
}


public class Exercice4_2_0 {
	// Une seule variable d'instance
	Environment environment = new Environment();

	public Exercice4_2_0() {
		GSpace space = new GSpace("Exercice 4", new Dimension(200, 100));
		space.open();

		Reference spaceRef = new Reference(space);
		Reference rectClassRef = new Reference(GRect.class);
		Reference ovalClassRef = new Reference(GOval.class);
		Reference imageClassRef = new Reference(GImage.class);
		Reference stringClassRef = new Reference(GString.class);

		spaceRef.addCommand("setColor", new SetColor());
		spaceRef.addCommand("sleep", new Sleep());

		spaceRef.addCommand("add", new AddElement());
		spaceRef.addCommand("del", new DelElement());
		
		rectClassRef.addCommand("new", new NewElement());
		ovalClassRef.addCommand("new", new NewElement());
		imageClassRef.addCommand("new", new NewImage());
		stringClassRef.addCommand("new", new NewString());

		environment.addReference("space", spaceRef);
		environment.addReference("rect.class", rectClassRef);
		environment.addReference("oval.class", ovalClassRef);
		environment.addReference("image.class", imageClassRef);
		environment.addReference("label.class", stringClassRef);
		
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
				new Interpreter().compute(environment, itor.next());
			}
		}
	}

	public static void main(String[] args) {
		new Exercice4_2_0();
	}
	
	public class Interpreter{
		public void compute(Environment env, SNode method) {
			
		}
		
	}
	
	
	
}





interface Command {
	abstract public Reference run(Reference receiver, SNode method);
}

class Reference{
	Object receiver;
	Map<String, Command> primitives;
	
	public Reference(Object receiver) {
		this.receiver = receiver;
		primitives = new HashMap<String,Command>();
	}
	
	public Object getReceiver() {
		return this.receiver;
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

class Environment{
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



class SetColor implements Command{
	
	@Override
	public Reference run(Reference receiver, SNode method) {
		try {
			((GElement)(receiver.getReceiver())).setColor((Color)(Color.class.getDeclaredField(method.get(2).contents()).get(null)));
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
}


class Translate implements Command{
	@Override
	public Reference run(Reference receiver, SNode method) {
		int dx = Integer.valueOf(method.get(2).contents());
		int dy = Integer.valueOf(method.get(3).contents());
		((GElement)receiver.getReceiver()).translate(new Point(dx,dy));
		return null;
	}
}

class setDim implements Command{
	@Override
	public Reference run(Reference receiver, SNode method) {
		int width = Integer.valueOf(method.get(2).contents());
		int height = Integer.valueOf(method.get(3).contents());
		((GBounded)(receiver.getReceiver())).setDimension(new Dimension(width,height));
		return null;
	}
	
}
