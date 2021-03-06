package org.basex.query.expr;

import static org.basex.query.QueryText.*;

import org.basex.query.*;
import org.basex.query.value.item.*;
import org.basex.query.value.node.*;
import org.basex.query.value.type.*;
import org.basex.query.var.*;
import org.basex.util.*;
import org.basex.util.hash.*;

/**
 * Arithmetic expression.
 *
 * @author BaseX Team 2005-17, BSD License
 * @author Christian Gruen
 */
public final class Arith extends Arr {
  /** Calculation operator. */
  private final Calc calc;

  /**
   * Constructor.
   * @param info input info
   * @param expr1 first expression
   * @param expr2 second expression
   * @param calc calculation operator
   */
  public Arith(final InputInfo info, final Expr expr1, final Expr expr2, final Calc calc) {
    super(info, SeqType.AAT_ZO, expr1, expr2);
    this.calc = calc;
  }

  @Override
  public Expr optimize(final CompileContext cc) throws QueryException {
    final Expr ex1 = exprs[0], ex2 = exprs[1];
    final SeqType st1 = ex1.seqType(), st2 = ex2.seqType();
    final Type t1 = st1.type, t2 = st2.type;
    final boolean nums = t1.isNumberOrUntyped() && t2.isNumberOrUntyped();
    final Type t = calc == Calc.IDIV ? AtomType.ITR : nums ? Calc.type(t1, t2) : AtomType.AAT;
    exprType.assign(t, st1.oneNoArray() && st2.oneNoArray() ? Occ.ONE : Occ.ZERO_ONE);

    Expr expr = this;
    if(oneIsEmpty()) {
      expr = cc.emptySeq(this);
    } else if(allAreValues(false)) {
      expr = cc.qc.value(this);
    } else if(nums && st1.oneNoArray() && st2.oneNoArray()) {
      expr = calc.optimize(ex1, ex2);
      if(expr == null || !expr.seqType().type.eq(t)) expr = this;
    }
    return cc.replaceWith(this, expr);
  }

  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Item it1 = exprs[0].atomItem(qc, info);
    if(it1 == null) return null;
    final Item it2 = exprs[1].atomItem(qc, info);
    return it2 == null ? null : calc.ev(it1, it2, info);
  }

  @Override
  public Arith copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return copyType(new Arith(info, exprs[0].copy(cc, vm), exprs[1].copy(cc, vm), calc));
  }

  @Override
  public boolean equals(final Object obj) {
    return this == obj || obj instanceof Arith && calc == ((Arith) obj).calc && super.equals(obj);
  }

  @Override
  public void plan(final FElem plan) {
    addPlan(plan, planElem(OP, calc.name), exprs);
  }

  @Override
  public String description() {
    return '\'' + calc.name + "' operator";
  }

  @Override
  public String toString() {
    return toString(' ' + calc.name + ' ');
  }
}
