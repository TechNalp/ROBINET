package version_5.copy;

import stree.parser.SNode;

interface Command {
	abstract public Reference run(Reference receiver, SNode method)throws Exception;
}