package org.basex.query.expr;

import org.basex.query.value.node.*;
import org.basex.util.*;

/**
 * Expression information, used for debugging and logging.
 *
 * @author BaseX Team 2005-17, BSD License
 * @author Christian Gruen
 */
public abstract class ExprInfo {
  /**
   * Returns a string description of the expression. This method is only
   * called by error messages. Contrary to the {@link #toString()} method,
   * arguments are not included in the output.
   * @return result of check
   */
  public String description() {
    final TokenBuilder tb = new TokenBuilder();
    boolean sep = false;
    for(final byte b : Token.token(Util.className(this))) {
      if(Character.isLowerCase(b)) {
        sep = true;
      } else if(sep) {
        tb.add(' ');
      }
      tb.add(Character.toLowerCase(b));
    }
    return tb.toString();
  }

  /**
   * Returns a string representation of the expression that can be embedded in error messages.
   * Defaults to {@link #toString()}.
   * @return class name
   */
  public String toErrorString() {
    return toString();
  }

  @Override
  public abstract String toString();

  /**
   * Creates an expression tree.
   * @param e root element
   */
  public abstract void plan(FElem e);

  /**
   * Creates a new element node to be added to the expression tree.
   * @param atts optional attribute names and values
   * @return tree node
   */
  protected FElem planElem(final Object... atts) {
    final FElem el = new FElem(Util.className(this));
    final int al = atts.length;
    for(int a = 0; a < al - 1; a += 2) {
      if(atts[a + 1] != null) el.add(planAttr(atts[a], atts[a + 1]));
    }
    return el;
  }

  /**
   * Adds trees of the specified expressions to the root node.
   * @param plan root node
   * @param child new element
   * @param exprs expressions
   */
  protected static void addPlan(final FElem plan, final FElem child, final Object... exprs) {
    plan.add(child);
    for(final Object expr : exprs) {
      if(expr instanceof ExprInfo) {
        ((ExprInfo) expr).plan(child);
      } else if(expr instanceof ExprInfo[]) {
        for(final ExprInfo ex : (ExprInfo[]) expr) {
          if(ex != null) ex.plan(child);
        }
      } else if(expr instanceof byte[]) {
        child.add((byte[]) expr);
      } else if(expr != null) {
        child.add(expr.toString());
      }
    }
  }

  /**
   * Adds trees of the specified expressions to the root node.
   * @param plan root node
   * @param child new element
   * @param expr expressions
   */
  protected static void addPlan(final FElem plan, final FElem child, final ExprInfo... expr) {
    addPlan(plan, child, (Object) expr);
  }

  /**
   * Creates a new attribute to be added to the expression tree.
   * @param name name of attribute
   * @param value value of attribute
   * @return tree node
   */
  protected static FAttr planAttr(final Object name, final Object value) {
    return new FAttr(Util.inf(name), Util.inf(value));
  }
}
