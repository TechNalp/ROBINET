package version_6.copy;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

import javax.imageio.ImageIO;

import graphicLayer.GImage;
import stree.parser.SNode;

public class NewImage implements Command{
	
	
	@Override
	public Reference run(Reference reference, SNode method) {
		try {
			BufferedImage rawImage = ImageIO.read(new File(method.get(2).contents()));
			@SuppressWarnings("unchecked")
			GImage i = ((Class<GImage>) reference.getReceiver()).getDeclaredConstructor(Image.class).newInstance(rawImage);
			Reference ref = new Reference(i);
			
			ref.setHisEnv(new Environment(null,reference.hisEnv.ps));
			
			ref.addCommand("setColor", new SetColor());
			ref.addCommand("translate", new Translate());
			ref.addCommand("add", new AddElement(ref.getHisEnv()));
			ref.addCommand("del", new DelElement(ref.getHisEnv()));
			
			ref.addCommand("addScript", new AddScript());
			
			return ref;
			
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
