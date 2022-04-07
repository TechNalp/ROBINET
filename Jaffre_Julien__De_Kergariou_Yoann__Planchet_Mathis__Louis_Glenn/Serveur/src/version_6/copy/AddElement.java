package version_6.copy;



import graphicLayer.GContainer;
import graphicLayer.GElement;
import stree.parser.SNode;
import version_6.copy.Sleep.StopException;

public class AddElement implements Command{
	Environment env;
	
	public AddElement(Environment env) {
		this.env = env;
	}
	@Override
	public Reference run(Reference receiver, SNode method) throws StopException {
		if(!(receiver.getReceiver() instanceof GContainer)) {
			receiver.hisEnv.ps.println(method.get(0).contents()+"n'est pas un container");
			return null;
		}
		String nameNewRef = method.get(2).contents();
		SNode s2 = method.get(3);
		
		Reference classRef = receiver.getGlobalEnv().getReferenceByName(s2.get(0).contents());
		
		Command cmd = classRef.getCommandByName(s2.get(1).contents());
		
		Reference newRef = cmd.run(classRef, method.get(3));
		if(newRef == null) {
			receiver.hisEnv.ps.println("Impossible de créer l'objet" + method.get(2).contents());
			return null;
		}
		
		newRef.getHisEnv().setDadEnv(env);
			
		
		if(env.addReference(nameNewRef, newRef)) {
			((GContainer)(receiver.getReceiver())).addElement((GElement)newRef.getReceiver());
			((GContainer)receiver.getReceiver()).repaint();
		}
		
		return newRef;
	}
	
}