package version_6.copy;

import stree.parser.SNode;
import version_6.copy.Sleep.StopException;

public interface Command {
	abstract public Reference run(Reference receiver, SNode method) throws StopException;
}