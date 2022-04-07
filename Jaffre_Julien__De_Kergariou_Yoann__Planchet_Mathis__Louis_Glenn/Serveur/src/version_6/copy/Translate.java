package version_6.copy;

import java.awt.Point;
import java.io.PrintStream;

import graphicLayer.GElement;
import stree.parser.SNode;

public class Translate implements Command{
	
	
	@Override
	public Reference run(Reference receiver, SNode method) {
		int dx=0;
		int dy=0;
		try {
			dx = Integer.valueOf(method.get(2).contents());
			dy = Integer.valueOf(method.get(3).contents());
		}catch(NumberFormatException e) {
			receiver.hisEnv.ps.println(e.getMessage().split("\"")[1]+" n'est pas un nombre valide");
			return null;
		}
		
		
		((GElement)receiver.getReceiver()).translate(new Point(dx,dy));
		return null;
	}
}