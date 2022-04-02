package version_6.copy;

import java.lang.reflect.InvocationTargetException;

import graphicLayer.GString;
import stree.parser.SNode;

public class NewString implements Command{

	@Override
	public Reference run(Reference reference, SNode method) {
		try {
			@SuppressWarnings("unchecked")
			GString s = ((Class<GString>) reference.getReceiver()).getDeclaredConstructor().newInstance();
			String str = method.get(2).contents();
			if(str.startsWith("\"") && str.endsWith("\"")) {
				str = str.substring(1,str.length()-1);
			}
			s.setString(str);
			Reference ref = new Reference(s);
			ref.addCommand("translate", new Translate());
			ref.addCommand("setColor", new SetColor());
			
			return ref;
			
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
