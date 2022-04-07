package version_6.copy;

import java.awt.Dimension;

import graphicLayer.GBounded;
import graphicLayer.GSpace;
import stree.parser.SNode;

public class SetDim implements Command{
	@Override
	public Reference run(Reference receiver, SNode method) {
		
		
		
		int width = 0;
		int height = 0;
		
		if(receiver.receiver instanceof GBounded) {
			width = ((GBounded)(receiver.getReceiver())).getWidth();
			height = ((GBounded)(receiver.getReceiver())).getHeight();
		}else {
			width = ((GSpace)(receiver.getReceiver())).getWidth();
			height = ((GSpace)(receiver.getReceiver())).getHeight();
		}
		
		try {
			width = Integer.valueOf(method.get(2).contents());
			height = Integer.valueOf(method.get(3).contents());
		}catch(NumberFormatException e) {
			receiver.hisEnv.ps.println(e.getMessage().split("\"")[1]+" n'est pas un nombre valide");
			return null;
		}
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