package internal.nodes;

import java.util.List;

import internal.ExecutionException;
import internal.ParseHaltException;
import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import internal.SemanticException;
import internal.eval.TypedValue;
import internal.scope.Scope;

public class WhileLoopNode extends Node {
    private final ExprNode expression;
    private final BodyNode body;

    protected WhileLoopNode(String filename, int lineNumber, ExprNode expression, BodyNode body) {
        super(filename, lineNumber);
        this.expression = expression;
        this.body = body;
        this.adopt();
    }

    @Override
    public String convertToJott() {
        return "While [" + this.expression.convertToJott() + "]{\n" + this.body.convertToJott() + "}\n";
    }

    @Override
    public boolean validateTree(Scope scope) {
        return this.expression.validateTree(scope) && this.body.validateTree(scope);
    }

    public static WhileLoopNode parse(PeekingArrayIterator it)
            throws ParseHaltException, ParseUnexpectedTokenException {
        it.expect("While");
        it.expect("[");
        ExprNode expression = ExprNode.parse(it);
        it.expect("]");
        it.expect("{");
        BodyNode body = BodyNode.parse(it);
        it.expect("}");
        return new WhileLoopNode(it.getCurrentFilename(), it.getCurrentLine(), expression, body);
    }

    @Override
    public List<Node> getChildren() {
        return List.of(expression, body);
    }

    @Override
    public TypedValue evaluate(Scope scope) throws SemanticException, ExecutionException {
        while (this.expression.evaluate(scope).getBoolean()) {
            TypedValue bodyResult = this.body.evaluate(scope);
            if (bodyResult.hasValue()) {
                return bodyResult;
            }
        }
        return new TypedValue();
    }

}
