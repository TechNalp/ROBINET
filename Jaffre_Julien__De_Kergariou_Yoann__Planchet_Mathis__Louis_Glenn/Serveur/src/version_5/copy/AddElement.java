package version_5.copy;

import graphicLayer.GContainer;
import graphicLayer.GElement;
import stree.parser.SNode;
//ajoute un élément à un environnement

public class AddElement implements Command{
	Environment env;
	public AddElement(Environment env) {
		this.env = env;
	}
	@Override
	public Reference run(Reference receiver, SNode method) throws Exception {
		String nameNewRef = method.get(2).contents();
		SNode s2 = method.get(3);
		
		Reference classRef = this.env.getReferenceByName(s2.get(0).contents());
		
		Command cmd = classRef.getCommandByName(s2.get(1).contents());
		
		Reference newRef = cmd.run(classRef, method.get(3));
		
		if(env.addReference(nameNewRef, newRef)) {
			((GContainer)(receiver.getReceiver())).addElement((GElement)newRef.getReceiver());
			((GContainer)receiver.getReceiver()).repaint();
		}
		
		return newRef;
	}
	
}