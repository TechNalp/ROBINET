package version_6.copy;

import java.lang.reflect.InvocationTargetException;

import graphicLayer.GElement;
import stree.parser.SNode;

public class NewElement implements Command {
	
	
	@Override
	public Reference run(Reference reference, SNode method) {
		try {
			@SuppressWarnings("unchecked")
			GElement e = ((Class<GElement>) reference.getReceiver()).getDeclaredConstructor().newInstance();
			Reference ref = new Reference(e);
			
			ref.setHisEnv(new Environment(null,reference.hisEnv.ps));
			
			ref.addCommand("setColor", new SetColor());
			ref.addCommand("translate", new Translate());
			ref.addCommand("setDim", new SetDim());
			ref.addCommand("add", new AddElement(ref.getHisEnv()));
			ref.addCommand("del", new DelElement(ref.getHisEnv()));
			
			ref.addCommand("addScript", new AddScript());
			
			return ref;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
}
