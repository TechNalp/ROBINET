package exercice6;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import graphicLayer.GBounded;
import graphicLayer.GElement;
import graphicLayer.GImage;
import graphicLayer.GOval;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import graphicLayer.GString;
import graphicLayer.GContainer;

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
			ref.addCommand("add", new AddElement(Exercice6_0.environment));
			ref.addCommand("del", new DelElement(Exercice6_0.environment));
			return ref;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
}

class NewImage implements Command{
	public Reference run(Reference reference, SNode method) {
		try {
			BufferedImage rawImage = ImageIO.read(new File(method.get(2).contents()));
			@SuppressWarnings("unchecked")
			GImage i = ((Class<GImage>) reference.getReceiver()).getDeclaredConstructor(Image.class).newInstance(rawImage);
			Reference ref = new Reference(i);
			ref.addCommand("setColor", new SetColor());
			ref.addCommand("translate", new Translate());
			ref.addCommand("add", new AddElement(Exercice6_0.environment));
			ref.addCommand("del", new DelElement(Exercice6_0.environment));
			
			return ref;
			
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}


class NewString implements Command{

	@Override
	public Reference run(Reference reference, SNode method) {
		try {
			@SuppressWarnings("unchecked")
			GString s = ((Class<GString>) reference.getReceiver()).getDeclaredConstructor().newInstance();
			String str = method.get(2).contents();
			if(str.startsWith("\"") && str.endsWith("\"")) {
				str = str.substring(1,str.length()-1);
			}
			s.setString(str);
			Reference ref = new Reference(s);
			ref.addCommand("translate", new Translate());
			ref.addCommand("setColor", new SetColor());
			
			return ref;
			
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}





public class Exercice6_0 {
	
	static Environment environment = new Environment();
	
	public Exercice6_0(){
		GSpace space = new GSpace("Exercice 4", new Dimension(200, 100));
		space.open();
		
		Reference spaceRef = new Reference(space);
		Reference rectClassRef = new Reference(GRect.class);
		Reference ovalClassRef = new Reference(GOval.class);
		Reference imageClassRef = new Reference(GImage.class);
		Reference stringClassRef = new Reference(GString.class);

		spaceRef.addCommand("setColor", new SetColor());
		spaceRef.addCommand("sleep", new Sleep());
		spaceRef.addCommand("setDim", new SetDim());

		spaceRef.addCommand("add", new AddElement(environment));
		spaceRef.addCommand("del", new DelElement(environment));
		
		rectClassRef.addCommand("new", new NewElement());
		ovalClassRef.addCommand("new", new NewElement());
		imageClassRef.addCommand("new", new NewImage());
		stringClassRef.addCommand("new", new NewString());

		environment.addReference("space", spaceRef);
		environment.addReference("Rect", rectClassRef);
		environment.addReference("Oval", ovalClassRef);
		environment.addReference("Image", imageClassRef);
		environment.addReference("Label", stringClassRef);
		
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
		new Exercice6_0();
	}
	
	public class Interpreter{
		public void compute(Environment env, SNode method) {
			String receiverName = method.get(0).contents();
			Reference receiver = env.getReferenceByName(receiverName);
			if(receiver != null) {
				receiver.run(method);
			}
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
	
	public boolean addReference(String name, Reference ref) {
		if(!this.variables.containsKey(name)) {
			this.variables.put(name, ref);
			return true;
		}else{
			System.err.println("La r�f�rence "+name+" existe d�j�");
			return false;
		}
	}
	
	public Reference getReferenceByName(String name) {
		Reference ref = this.variables.get(name);
		if(ref == null) {
			System.err.println(name+" n'existe pas");
		}
		return ref;
	}
	
	public void removeReference(String name) {
		this.variables.remove(name);
	}
	
}


class Sleep implements Command{

	@Override
	public Reference run(Reference receiver, SNode method) {
		try {
			Thread.sleep(Integer.valueOf(method.get(2).contents()));
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}

class SetColor implements Command{
	
	@Override
	public Reference run(Reference receiver, SNode method) {
		try {
			if(receiver.getReceiver() instanceof GElement){
				((GElement)(receiver.getReceiver())).setColor((Color)(Color.class.getDeclaredField(method.get(2).contents()).get(null)));
			}else {
				((GSpace)(receiver.getReceiver())).setColor((Color)(Color.class.getDeclaredField(method.get(2).contents()).get(null)));
			}
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

class SetDim implements Command{
	@Override
	public Reference run(Reference receiver, SNode method) {
		int width = Integer.valueOf(method.get(2).contents());
		int height = Integer.valueOf(method.get(3).contents());
		if(receiver.receiver instanceof GBounded) {
			((GBounded)(receiver.getReceiver())).setWidth(width);
			((GBounded)(receiver.getReceiver())).setHeight(height);
			((GBounded)(receiver.getReceiver())).repaint();
		}else {
			((GSpace)(receiver.getReceiver())).changeWindowSize(new Dimension(width,height));
			((GSpace)(receiver.getReceiver())).repaint();
		}
		
		return null;
	}
	
}



class AddElement implements Command{
	Environment env;
	public AddElement(Environment env) {
		this.env = env;
	}
	@Override
	public Reference run(Reference receiver, SNode method) {
		if(!(receiver.getReceiver() instanceof GContainer)) {
			System.err.println(method.get(0).contents()+"n'est pas un container");
			return null;
		}
		String nameNewRef = method.get(2).contents();
		SNode s2 = method.get(3);
		
		Reference classRef = this.env.getReferenceByName(s2.get(0).contents());
		
		Command cmd = classRef.getCommandByName(s2.get(1).contents());
		
		Reference newRef = cmd.run(classRef, method.get(3));
		if(newRef == null) {
			System.err.println("Impossible de créer l'objet" + method.get(2).contents());
			return null;
		}
		
		nameNewRef = method.get(0).contents()+"."+nameNewRef;
		
		if(env.addReference(nameNewRef, newRef)) {
			((GContainer)(receiver.getReceiver())).addElement((GElement)newRef.getReceiver());
			((GContainer)receiver.getReceiver()).repaint();
		}
		
		return newRef;
	}
	
}

class DelElement implements Command{
	
	Environment env;
	
	public DelElement(Environment env) {
		this.env = env;
	}

	@Override
	public Reference run(Reference receiver, SNode method) {
		String nameRefToDel = method.get(2).contents();
		
		Reference ref = this.env.getReferenceByName(nameRefToDel);
		if(ref == null) {
			return null;
		}
		
		
		
		if(receiver.getReceiver() instanceof GContainer) {
			((GContainer)(receiver.getReceiver())).removeElement((GElement)ref.getReceiver());
			((GContainer)(receiver.getReceiver())).repaint();
		}
		
		Set<String> toDelete = new HashSet<>();
		for (String s : this.env.variables.keySet()) {
			if(s.startsWith(nameRefToDel)) {
				toDelete.add(s);
			}
		}
		
		for(String s : toDelete) {
			this.env.removeReference(s);
		}
		
		toDelete.clear();
		
		return null;
	}
}





