package internal.nodes;

import java.util.List;

import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import internal.SemanticException;
import internal.SemanticNameException;
import internal.scope.ParameterSymbol;
import internal.scope.Scope;
import provided.Token;
import provided.TokenType;

/**
 * A parameter used as part of a function definition
 */
public class FuncDefParamNode extends Node {
    private final String id;
    private final TypeNode type;

    protected FuncDefParamNode(String filename, int lineNumber, String id, TypeNode type) {
        super(filename, lineNumber);
        this.id = id;
        this.type = type;
        this.adopt();
    }

    public static FuncDefParamNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException {
        Token id = it.expect(TokenType.ID_KEYWORD);
        // Looks like we're skipping twice here?
        it.expect(TokenType.COLON);
        TypeNode type = TypeNode.parse(it);
        return new FuncDefParamNode(it.getCurrentFilename(), id.getLineNum(), id.getToken(), type);
    }

    @Override
    public String convertToJott() {
        return id + ": " + type.convertToJott();
    }

    @Override
    public boolean validateTree(Scope scope) {
        // System.out.println(this.convertToJott() + " :: " +
        // Integer.toString(this.getLineNumber()));
        if (!validateId(id)) {
            new SemanticNameException(id).report(this);
            return false;
        }

        try {
            scope.getCurrentScope().define(ParameterSymbol.from(this));
        } catch (SemanticException e) {
            e.report(this);
            return false;
        }

        return this.type.validateTree(scope);
    }

    @Override
    public List<Node> getChildren() {
        return List.of(type);
    }

    @Override
    public String getSymbol() {
        return this.id;
    }

    public TypeNode getType() {
        return this.type;
    }
}
