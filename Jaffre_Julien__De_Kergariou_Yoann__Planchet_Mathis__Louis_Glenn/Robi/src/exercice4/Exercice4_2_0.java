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
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import exercice4.Exercice4_1_0.Command;
import exercice4.Exercice4_1_0.Environment;
import exercice4.Exercice4_1_0.Reference;
import exercice4.Exercice4_1_0.SetColor;
import exercice4.Exercice4_1_0.Sleep;
import exercice4.Exercice4_1_0.Translate;
import graphicLayer.GElement;
import graphicLayer.GImage;
import graphicLayer.GOval;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import graphicLayer.GString;
import stree.parser.SNode;
import stree.parser.SParser;
import tools.Tools;

class DelElement implements Command {

	@Override
	public Reference run(Reference receiver, SNode method) {
			//setdim pas dans gelement cast +pour chacun element
			GSpace space = (GSpace) receiver.receiver;
		//	space.removeElement(receiver.ge);
		
		
		return null;
	}
}
class SetDim implements Command {

	@Override
	public Reference run(Reference receiver, SNode method) {
			//setdim pas dans gelement cast +pour chacun element
			GElement elementtmp = (GElement) receiver.receiver;
			if(elementtmp instanceof GRect) {
				GRect element=(GRect) elementtmp;
			element.setDimension(new Dimension(
					
					Integer.parseInt(method.get(2).contents()),
					Integer.parseInt(method.get(3).contents())
					));
			}
		
		
		return null;
	}
}


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
		} catch (Exception ex) {
			ex.printStackTrace();
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

	//	spaceRef.addCommand("add", new AddElement());
	//	spaceRef.addCommand("del", new DelElement());
		
		rectClassRef.addCommand("new", new NewElement());
		ovalClassRef.addCommand("new", new NewElement());
	//	imageClassRef.addCommand("new", new NewImage());
	//	stringClassRef.addCommand("new", new NewString());

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
	//class enfouie
	class Interpreter {
		 void compute(Environment env,SNode method) {
			 //demander addref bon ou alors rename de la cle
			if(method.get(1).contents().equals("add")) {
				Command cmd=new NewElement();
				//recupere la reference de l'espace
				GSpace sp=(GSpace)env.getReferenceByName(method.get(0).contents()).receiver;
				//reference vers le nouveau element
				SNode n2=method.get(3);
				Reference r=cmd.run(env.getReferenceByName(n2.get(0).contents()),method);
				env.addReference(method.get(2).contents(),r);
				sp.addElement((GElement)r.receiver);
				sp.repaint();
			}else {
			
				env.getReferenceByName(method.get(0).contents()).run(method);
				
			}
			
			
		}
	}
	public static void main(String[] args) {
		new Exercice4_2_0();
	}

}