package version_4;

import stree.parser.SNode;

interface Command {
	abstract public Reference run(Reference receiver, SNode method)throws Exception;
}