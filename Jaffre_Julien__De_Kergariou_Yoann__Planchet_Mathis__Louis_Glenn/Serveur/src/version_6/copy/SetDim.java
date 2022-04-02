package version_6.copy;

import java.awt.Dimension;

import graphicLayer.GBounded;
import graphicLayer.GSpace;
import stree.parser.SNode;

public class SetDim implements Command{
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