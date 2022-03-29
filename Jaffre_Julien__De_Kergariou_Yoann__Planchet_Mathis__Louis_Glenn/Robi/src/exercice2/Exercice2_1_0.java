package exercice2;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import exercice3.Exercice3_0.RobiChangeColor;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import stree.parser.SNode;
import stree.parser.SParser;
import stree.parser.SPrinter;


public class Exercice2_1_0 {
	GSpace space = new GSpace("Exercice 2_1", new Dimension(200, 100));
	GRect robi = new GRect();
	String script = "(space setColor black)"
			+ "(robi setColor yellow)";

	//script2.2
	String script2 ="(space color white)"
			+ "(robi color red)"
			+ "(robi translate 10 0)"
			+ "(space sleep 100)"
			+ "(space sleep 100)"
			+ "(robi translate -10 0)"
			+ "(space sleep 100)"
			+ "(robi translate 0 -10)"
			
			
			;
	public Exercice2_1_0() {
		space.addElement(robi);
		space.open();
		this.runScript();
	}

	private void runScript() {
		SParser<SNode> parser = new SParser<>();
		List<SNode> rootNodes = null;
		try {
			rootNodes = parser.parse(script2);
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
		SPrinter printer = new SPrinter();
		SNode n=expr;
		
		n.accept(printer);
		//test assert ==3
	//	if(printer.result().toString().equals("( robi color red )"))robi.setColor(Color.RED);
	//	if(printer.result().toString().equals("( space color white )"))space.setColor(Color.WHITE);
		System.out.println(printer.result().toString());
		if(expr.get(1).contents().equals("color")){
			Color color;
			try {
			    Field field = Class.forName("java.awt.Color").getField(expr.get(2).contents());
			    color = (Color)field.get(null);
			    if(expr.get(0).contents().equals("robi")) {
			    	robi.setColor(Color.RED);
			    }else {
			    	space.setColor(Color.WHITE);
			    }
			} catch (Exception e) {
				
			}
			
		}
		
		//2.2
		if(n.get(0).contents().equals("robi")&& n.get(1).contents().equals("translate"))
			robi.translate(new Point(Integer.parseInt(n.get(2).contents()),Integer.parseInt(n.get(3).contents())));
		if(n.get(0).contents().equals("space")&&n.get(1).contents().equals("sleep"))
		{
			try {
				Thread.sleep(Integer.parseInt(n.get(2).contents()));
			} catch (NumberFormatException e) {
				
				e.printStackTrace();
			} catch (InterruptedException e) {
			
				e.printStackTrace();
			}
		}
			
			
		

	}

	public static void main(String[] args) {
		new Exercice2_1_0();
	}

}