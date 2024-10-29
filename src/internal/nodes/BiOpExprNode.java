package internal.nodes;

import java.util.List;

import internal.ParseHaltException;
import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import internal.scope.Scope;
import provided.Token;
import provided.TokenType;

/**
 * An expression which performs an operation on two operands
 * Node: <rel_op> | <math_op>
 */
public class BiOpExprNode extends ExprNode {
    private final OperandNode a, b;
    private final String op;

    protected BiOpExprNode(int lineNumber, OperandNode a, String op, OperandNode b) {
        super(lineNumber);
        this.a = a;
        this.op = op;
        this.b = b;
        this.adopt();
    }

    public static BiOpExprNode parse(PeekingArrayIterator it, OperandNode a)
            throws ParseUnexpectedTokenException, ParseHaltException {
        Token op = it.expect(TokenType.REL_OP, TokenType.MATH_OP);
        OperandNode b = OperandNode.parse(it);
        return new BiOpExprNode(op.getLineNum(), a, op.getToken(), b);
    }

    @Override
    public String convertToJott() {
        return a.convertToJott() + " " + op + " " + b.convertToJott();
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
        return List.of(this.a, this.b);
    }
}
