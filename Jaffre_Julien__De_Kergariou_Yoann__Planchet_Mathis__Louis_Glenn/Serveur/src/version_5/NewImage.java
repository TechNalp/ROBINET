package version_5;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.imageio.ImageIO;

import graphicLayer.GImage;
import stree.parser.SNode;

public class NewImage implements Command{
	public Reference run(Reference reference, SNode method) {
		try {
			BufferedImage rawImage = ImageIO.read(new File(method.get(2).contents()));
			@SuppressWarnings("unchecked")
			GImage i = ((Class<GImage>) reference.getReceiver()).getDeclaredConstructor(Image.class).newInstance(rawImage);
			Reference ref = new Reference(i);
			ref.addCommand("setColor", new SetColor());
			ref.addCommand("translate", new Translate());
			
			return ref;
			
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
