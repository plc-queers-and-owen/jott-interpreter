package internal.nodes;

import java.util.List;

import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import internal.eval.Type;
import internal.scope.Scope;

/**
 * The specified return type in a function definition
 */
public class FunctionReturnNode extends Node {
    private Type type;

    protected FunctionReturnNode(int lineNumber, Type type) {
        super(lineNumber);
        this.type = type;
        this.adopt();
    }

    public static FunctionReturnNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException {
        int lineNumber = it.getCurrentLine();
        Type type = Type.fromString(it.peek().getToken());

        // If we get null we don't have a valid type so we error
        if (type == null) {
            throw new ParseUnexpectedTokenException(
                    new String[] { "String", "Boolean", "Integer", "Double", "Void" },
                    it.peek().getToken(), it.getContext());
        }

        // Skip since we only peeked earlier
        it.skip();

        return new FunctionReturnNode(lineNumber, type);
    }

    @Override
    public String convertToJott() {
        return type.toString();
    }

    @Override
    public boolean validateTree(Scope scope) {
        return true;
    }

    @Override
    public void execute() {

    }

    @Override
    public List<Node> getChildren() {
        return List.of();
    }
}
