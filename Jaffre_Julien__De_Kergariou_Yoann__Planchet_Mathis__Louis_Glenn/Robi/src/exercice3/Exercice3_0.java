package exercice3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import graphicLayer.GRect;
import graphicLayer.GSpace;
import stree.parser.SNode;
import stree.parser.SParser;

public class Exercice3_0 {
	GSpace space = new GSpace("Exercice 3", new Dimension(200, 100));
	GRect robi = new GRect();
	String script = "" +
	"   (space setColor black) " +
	"   (robi setColor yellow)" +
	"   (space sleep 1000)" +
	"   (space setColor white)\n" + 
	"   (space sleep 1000)" +
	"	(robi setColor red) \n" + 
	"   (space sleep 1000)" +
	"	(robi translate 100 0)\n" + 
	"	(space sleep 1000)\n" + 
	"	(robi translate 0 50)\n" + 
	"	(space sleep 1000)\n" + 
	"	(robi translate -100 0)\n" + 
	"	(space sleep 1000)\n" + 
	"	(robi translate 0 -40)";

	public Exercice3_0() {
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
		Command cmd = getCommandFromExpr(expr);
		if (cmd == null)
			throw new Error("unable to get command for: " + expr);
		cmd.run();
	}

	Command getCommandFromExpr(SNode expr) {
		
		//space change couleur
		if(expr.get(0).contents().equals("space")&& expr.get(1).contents().equals("setColor")) {
			Color color;
			try {
			    Field field = Class.forName("java.awt.Color").getField(expr.get(2).contents());
			    color = (Color)field.get(null);
			    return new SpaceChangeColor(color);
			} catch (Exception e) {
			    return null; // Not defined
			}
			//space pause
		}else if(expr.get(0).contents().equals("robi")&& expr.get(1).contents().equals("setColor")) {
			Color color;
			try {
			    Field field = Class.forName("java.awt.Color").getField(expr.get(2).contents());
			    color = (Color)field.get(null);
			    return new RobiChangeColor(color);
			} catch (Exception e) {
			    return null; // Not defined
			}
		}else if(expr.get(0).contents().equals("space")&& expr.get(1).contents().equals("sleep")) {
			//espace pause
			//manque gestion exception not format number
			return new SpaceSleep(Integer.parseInt(expr.get(2).contents()));
		} else if(expr.get(0).contents().equals("robi")&& expr.get(1).contents().equals("translate")) {
			//déplacement robot
			//manque gestion exception not format number
			return new RobiTranslate(new Point(Integer.parseInt(expr.get(2).contents()),
					Integer.parseInt(expr.get(3).contents())
					)	);
		}
				
			//commande inconnue
		return null;
	}

	public static void main(String[] args) {
		new Exercice3_0();
	}

	public interface Command {
		abstract public void run();
	}
	//classe enfouie space change couleur
	public class SpaceChangeColor implements Command {
		Color newColor;

		public SpaceChangeColor(Color newColor) {
			this.newColor = newColor;
		}

		@Override
		public void run() {
			space.setColor(newColor);
		}

	}
	//classe enfouie robot change couleur
	public class RobiChangeColor implements Command {
		Color newColor;

		public RobiChangeColor(Color newColor) {
			this.newColor = newColor;
		}

		@Override
		public void run() {
			robi.setColor(newColor);
		}

	}
	//classe enfouie pause temp
		public class SpaceSleep implements Command {
			int timeSleep;
			
			public SpaceSleep(int newtimeSleep) {
				this.timeSleep = newtimeSleep;
			}

			@Override
			public void run() {
				try {
					Thread.sleep(timeSleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
		//classe enfouie robi déplacement
		public class RobiTranslate implements Command {
			Point newPoint;

			public RobiTranslate(Point newPoint) {
				this.newPoint = newPoint;
			}

			@Override
			public void run() {
				robi.translate(newPoint);
			}

		}
		
}