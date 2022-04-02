package version_6.copy;

import java.util.HashSet;
import java.util.Set;

import graphicLayer.GContainer;
import graphicLayer.GElement;
import stree.parser.SNode;

public class DelElement implements Command{
	
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