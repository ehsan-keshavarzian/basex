package org.basex.query.func.util;

import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.func.*;
import org.basex.query.func.fn.*;
import org.basex.query.iter.*;
import org.basex.query.value.*;
import org.basex.query.value.item.*;
import org.basex.query.value.type.*;
import org.basex.util.*;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-17, BSD License
 * @author Christian Gruen
 */
public final class UtilLastFrom extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    // fast route if the size is known
    final Iter iter = qc.iter(exprs[0]);
    final long sz = iter.size();
    if(sz >= 0) return sz == 0 ? null : iter.get(sz - 1);

    // loop through all items
    Item litem = null;
    for(Item it; (it = iter.next()) != null;) {
      qc.checkStop();
      litem = it;
    }
    return litem;
  }

  @Override
  protected Expr opt(final CompileContext cc) throws QueryException {
    final Expr ex = exprs[0];
    final SeqType st = ex.seqType();
    if(st.zeroOrOne()) return ex;
    exprType.assign(st.type, st.oneOrMore() ? Occ.ONE : Occ.ZERO_ONE);

    // check for large values and fn:reverse function
    return ex instanceof Value ? ((Value) ex).itemAt(ex.size() - 1) :
           ex instanceof FnReverse ? cc.function(Function.HEAD, info, ((Arr) ex).exprs) :
           this;
  }
}
