package org.basex.query.func.hof;

import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.func.*;
import org.basex.query.iter.*;
import org.basex.query.value.*;
import org.basex.query.value.item.*;
import org.basex.query.value.seq.*;
import org.basex.query.value.type.*;

/**
 * Implements the {@code hof:scan-left($seq, $start, $f)} function.
 *
 * @author BaseX Team 2005-17, BSD License
 * @author Leo Woerteler
 */
public final class HofScanLeft extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    final Iter outer = qc.iter(exprs[0]);
    final FItem f = checkArity(exprs[2], 2, qc);
    return new Iter() {
      private Value acc = qc.value(exprs[1]);
      private Iter inner = acc.iter();
      @Override
      public Item next() throws QueryException {
        while(true) {
          final Item i = inner.next();
          if(i != null) return i;
          final Item o = outer.next();
          if(o == null) return null;
          acc = f.invokeValue(qc, info, acc, o);
          inner = acc.iter();
          qc.checkStop();
        }
      }
    };
  }

  @Override
  protected Expr opt(final CompileContext cc) {
    final Expr ex1 = exprs[0], ex2 = exprs[1];
    if(ex1 == Empty.SEQ) return ex2;

    final SeqType st = ex1.seqType();
    exprType.assign(st.type, st.occ.union(Occ.ZERO));
    return this;
  }
}
