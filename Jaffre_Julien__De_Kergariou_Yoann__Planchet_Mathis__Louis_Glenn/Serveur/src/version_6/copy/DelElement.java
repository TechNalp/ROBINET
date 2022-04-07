package version_6.copy;


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
		Boolean useOfGlobalEnv = false;
		
		if(ref == null) {
			ref = receiver.getGlobalEnv().getReferenceByName(nameRefToDel);
			if(ref == null) {
				receiver.hisEnv.ps.println(nameRefToDel + "n'existe pas ou ne peut-être supprimé depuis cet élément");
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