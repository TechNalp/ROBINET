package exercice6;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
import stree.parser.SPrinter;
import stree.parser.SSyntaxError;
import tools.Tools;



class NewElement implements Command {
	public Reference run(Reference reference, SNode method) {
		try {
			@SuppressWarnings("unchecked")
			GElement e = ((Class<GElement>) reference.getReceiver()).getDeclaredConstructor().newInstance();
			Reference ref = new Reference(e);
			
			ref.setHisEnv(new Environment(null));
			
			ref.addCommand("setColor", new SetColor());
			ref.addCommand("translate", new Translate());
			ref.addCommand("setDim", new SetDim());
			ref.addCommand("add", new AddElement(ref.getHisEnv()));
			ref.addCommand("del", new DelElement(ref.getHisEnv()));
			ref.addCommand("addScript", new AddScript());
			
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
			
			ref.setHisEnv(new Environment(null));
			
			ref.addCommand("setColor", new SetColor());
			ref.addCommand("translate", new Translate());
			ref.addCommand("add", new AddElement(ref.getHisEnv()));
			ref.addCommand("del", new DelElement(ref.getHisEnv()));
			ref.addCommand("addScript", new AddScript());
			
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
			
			ref.setHisEnv(new Environment(null));
			
			ref.addCommand("translate", new Translate());
			ref.addCommand("setColor", new SetColor());
			ref.addCommand("addScript", new AddScript());
			
			return ref;
			
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}





public class Exercice6_0 {
	
	static Environment environment = new Environment(null);
	
	public Exercice6_0(){
		GSpace space = new GSpace("Exercice 4", new Dimension(200, 100));
		space.open();
		
		
		Reference spaceRef = new Reference(space);
		Reference rectClassRef = new Reference(GRect.class);
		Reference ovalClassRef = new Reference(GOval.class);
		Reference imageClassRef = new Reference(GImage.class);
		Reference stringClassRef = new Reference(GString.class);

		spaceRef.setHisEnv(new Environment(Exercice6_0.environment));
		
		spaceRef.addCommand("setColor", new SetColor());
		spaceRef.addCommand("sleep", new Sleep());
		spaceRef.addCommand("setDim", new SetDim());
		spaceRef.addCommand("addScript", new AddScript());

		spaceRef.addCommand("add", new AddElement(spaceRef.getHisEnv()));
		spaceRef.addCommand("del", new DelElement(spaceRef.getHisEnv()));
		
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
			}catch(SSyntaxError e) {
				System.err.println("Erreur de parsing : "+e.getMessage());
				continue;
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
		
		/*SParser<SNode> parser = new SParser<>();
		
		List<String> lp = new ArrayList<String>();
		lp.add("test");
		lp.add("lol");
		
		List<String> lp2 = new ArrayList<String>();
		
		lp2.add("new");
		lp2.add("add");
		/*try {
			Script test = new Script("lol", parser.parse(script),lp);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		/*String script = "( space add robi (Rect new ) )";
		
		try {
			List<SNode> compiled = parser.parse(script);
			new Script("space",compiled,lp).executeScript(lp2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		//new Exercice6_0();
	}
	
	
	
	
}


class Interpreter{
	public void compute(Environment env, SNode method) {
		if(method.size() <=0) {
			System.err.println("Expression vide");
			return;
		}
		String receiverName = method.get(0).contents();
		Reference receiver = env.getReferenceByName(receiverName);
		if(receiver != null) {
			receiver.run(method);
		}else {
			System.err.println(receiverName + " est inconnu");
		}
	}
	
}

class Script{
	
	String script = "";
	LinkedHashMap<String,Integer> params;
	
	String self = "";
	
	int paramsNumber = 0;
	
	public Script(String self, List<SNode> script, List<String> params) {
		
		this.self = self;
		
		this.paramsNumber = 0;
		
		Iterator<SNode> itor = script.iterator();
		
		SPrinter printer = new SPrinter();
		
		while(itor.hasNext()) {
			itor.next().accept(printer);
		}
		
		this.script = printer.result().toString();
		
		if(params.size() > 0 && params != null) {
			
			this.params = new LinkedHashMap<>();
			int i = 0;
			
			for(String s : params) {
				this.params.put(s, i);
				i++;
			}
			this.paramsNumber = params.size();
			
		}
		
	}
	
	
	
	public void executeScript(List<String> paramValue) {
		System.err.println(paramValue.size());
		if((paramValue.size() != this.paramsNumber) || (this.paramsNumber != 0 && paramValue == null) ) {
			System.err.println("Nombre de parametre du script invalide");
			return;
		}
		
		
		String copy = this.script;
		
		copy = copy.replaceAll("self", this.self);
		
		if(this.paramsNumber!=0) {
			for(String param : this.params.keySet()) {
				copy = copy.replaceAll("\\W"+param, " "+paramValue.get(this.params.get(param)));
			}
		}
		
		System.err.println(copy +" "+ this.paramsNumber);
		
		SParser<SNode> parser = new SParser<>();
		
		try {
			for(SNode node : parser.parse(copy)) {
				new Interpreter().compute(Exercice6_0.environment,node);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}


interface Command {
	abstract public Reference run(Reference receiver, SNode method);
}

class Reference{

	
	Object receiver;
	Map<String, Command> primitives;

	Map<String, Script> scripts;
	
	Environment hisEnv;
	
	public Reference(Object receiver) {
		this.receiver = receiver;
		primitives = new HashMap<String,Command>();
		scripts = new HashMap<String,Script>();
		this.hisEnv = null;
		
	}
	
	public void setHisEnv(Environment env) {
		this.hisEnv = env;
	}
	
	public Environment getHisEnv() {
		return this.hisEnv;
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
	
	public Script getScriptByName(String name) {
		return this.scripts.get(name);
	}
	
	public Reference run(SNode method) {
		if(method.size()<2) {
			System.err.println("Commande non indiquée");
			return null;
		}
		Command cmd = this.getCommandByName(method.get(1).contents());
		if(cmd != null) {
			return cmd.run(this, method);
		}else {
			
			Script sc = this.getScriptByName(method.get(1).contents());
			if(sc == null) {
				System.err.println(method.get(1).contents() +" ne correspond à aucune commande ou script");
				return null;
			}
			List<String> paramsValue = new ArrayList<>();
			
			System.err.println(sc.paramsNumber);
			
			for(int i=0; i<sc.paramsNumber;i++) {
				paramsValue.add(method.get(i+2).contents());
			}
			sc.executeScript(paramsValue);
			return null;
		}
	}
	
	public void addScript(String self, String scriptName,List<SNode>script, List<String> params) {
		this.scripts.put(scriptName, new Script(self,script,params));
	}
	
	public void executeScript(String scriptName, List<String> params) {
		this.scripts.get(scriptName).executeScript(params);
	}
	
}

class Environment{
	HashMap<String,Reference> variables;
	
	Environment pere;
	
	
	public Environment(Environment pere){
		this.variables = new HashMap<String,Reference>();
		this.pere = pere;
	}
	
	public void setDadEnv(Environment env) {
		this.pere = env;
	}
	
	public Environment getDadEnv() {
		return this.pere;
	}
	
	public boolean addReference(String name, Reference ref) {
		if(!this.variables.containsKey(name)) {
			this.variables.put(name, ref);
			return true;
		}else{
			System.err.println("La référence "+name+" existe déjà");
			return false;
		}
	}
	
	public Reference getReferenceByName(String name) {
		Reference ref = this.variables.get(name);
		if(ref != null) {
			return ref;
		}
		if(!name.contains(".")) {
			return null;
		}
		String[] pathElt = name.split("\\.");
		
		Environment currentEnv = this;
		Reference currentRef = null;
		for (String s : pathElt) {
			currentRef = currentEnv.getReferenceByName(s);
			if(currentRef == null) {
				return null;
			}
			currentEnv = currentRef.getHisEnv();
		}
		
		return currentRef;
		
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
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			e.printStackTrace();
		}catch(NoSuchFieldException e) {
			System.err.println(e.getMessage()+" n'est pas une couleur valide");
			return null;
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
		
		Reference classRef = Exercice6_0.environment.getReferenceByName(s2.get(0).contents());
		
		Command cmd = classRef.getCommandByName(s2.get(1).contents());
		
		Reference newRef = cmd.run(classRef, method.get(3));
		if(newRef == null) {
			System.err.println("Impossible de créer l'objet" + method.get(2).contents());
			return null;
		}
		
		newRef.getHisEnv().setDadEnv(env);
			
		
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
		Boolean useOfGlobalEnv = false;
		
		if(ref == null) {
			ref = Exercice6_0.environment.getReferenceByName(nameRefToDel);
			if(ref == null) {
				System.err.println(nameRefToDel + "n'existe pas ou ne peut-être supprimé depuis cet élément");
				return null;
			}
			useOfGlobalEnv = true;
		}
		
		ref.getHisEnv().setDadEnv(null);
		
		ref.setHisEnv(null);
		
		
		if(receiver.getReceiver() instanceof GContainer) {
			((GContainer)(receiver.getReceiver())).removeElement((GElement)ref.getReceiver());
			((GContainer)(receiver.getReceiver())).repaint();
		}
		
		
		if(!useOfGlobalEnv) {
			this.env.removeReference(nameRefToDel);
		}else {
			String[] path = nameRefToDel.split("\\.");
			this.env.removeReference(path[path.length-1]);
		}
		
		return null;
	}
}


class AddScript implements Command{


	@Override
	public Reference run(Reference receiver, SNode method) {
		
		List<String> param = new ArrayList<>();
		
		for(SNode node : method.get(3).get(0).children().subList(1, method.get(3).get(0).children().size())) {
			param.add(node.contents());
		}
		
		List<SNode> script = new ArrayList<>();
		
		for(SNode node : method.get(3).children().subList(1, method.get(3).children().size())) {
			script.add(node);
		}
		
		receiver.addScript(method.get(0).contents(), method.get(2).contents(),script, param);
		return null;
	}
	
	
	
}





