package exercice3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
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
		if(expr == null || expr.isLeaf()) {
			return null;
		}
		
		String applyToStr = expr.get(0).contents();
		String cmd = expr.get(1).contents();	
		
		if(applyToStr.equals("space")) {
			if(cmd.equals("setColor")) {
				try {
					return new SpaceChangeColor((Color)(Color.class.getDeclaredField(expr.get(2).contents()).get(null)));
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e) {
					e.printStackTrace();
				}
			}else if(cmd.equals("sleep")) {
				return new Sleep(Integer.valueOf(expr.get(2).contents()));
			}
			
		}
		
		if(applyToStr.equals("robi")) {
			if(cmd.equals("setColor")) {
				try {
					return new RobiChangeColor((Color)(Color.class.getDeclaredField(expr.get(2).contents()).get(null)));
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(cmd.equals("translate")) {
				if(expr.size()-2<2) {
					return null;
				}
				
				return new RobiTranslate(Integer.valueOf(expr.get(2).contents()),Integer.valueOf(expr.get(3).contents()));
			}
		}
		
		return null;
		
	}

	public static void main(String[] args) {
		new Exercice3_0();
	}

	public interface Command {
		abstract public void run();
	}

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
	
	public class RobiChangeColor implements Command{
		
		Color newColor;
		
		public RobiChangeColor(Color newColor) {
			this.newColor = newColor;
		}
		
		@Override
		public void run() {
			robi.setColor(this.newColor);
			
		}
		
	}
	
	public class Sleep implements Command{
		int timeToSleep;
		
		public Sleep(int timeToSleep) {
			this.timeToSleep = timeToSleep;
		}
		
		@Override
		public void run() {
			try {
				Thread.sleep(this.timeToSleep);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public class RobiTranslate implements Command{

		int dx;
		int dy;
		
		public RobiTranslate(int dx,int dy) {
			this.dx = dx;
			this.dy = dy;
		}
		
		@Override
		public void run() {
			robi.translate(new Point(this.dx,this.dy));
		}
		
	}
	
}