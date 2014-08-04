package org.basex.data;

import org.basex.*;
import org.basex.core.*;
import org.basex.core.cmd.*;
import org.junit.*;
import org.junit.Test;

/**
 * This class tests the {@code UPDINDEX} option.
 *
 * @author BaseX Team 2005-14, BSD License
 * @author Christian Gruen
 */
public final class UpdIndexTest extends SandboxTest {
  /**
   * Initializes the tests.
   * @throws Exception exception
   */
  @Before
  public void init() throws Exception {
    run(new Set(MainOptions.UPDINDEX, true));
    run(new CreateDB(NAME));
  }

  /**
   * Test.
   * @throws BaseXException database exception
   */
  @Test
  public void test() throws BaseXException {
    for(int i = 0; i < 25; i++) {
      run(new Replace("x.xml", "<x><a>A</a><a>B</a></x>"));
    }
  }

  /**
   * Test.
   * @throws BaseXException database exception
   */
  @Test
  public void test2() throws BaseXException {
    for(int i = 0; i < 25; i++) {
      run(new Replace("x.xml", "<x><a>A</a><a>B</a></x>"));
      run(new Replace("x.xml", "<x><a>A</a><a>C</a></x>"));
    }
  }

  /**
   * Test.
   * @throws BaseXException database exception
   */
  @Test
  public void test3() throws BaseXException {
    for(int i = 0; i < 25; i++) {
      run(new Replace("x.xml", "<x><a>A</a><a>BC</a><a>DEF</a></x>"));
    }
  }

  /**
   * Test.
   * @throws BaseXException database exception
   */
  @Test
  public void test4() throws BaseXException {
    for(int i = 0; i < 25; i++) {
      run(new Open(NAME));
      run(new Replace("x.xml", "<x><a>A</a><a>BC</a></x>"));
      run(new Close());
    }
  }

  /**
   * Test.
   * @throws BaseXException database exception
   */
  @Test
  public void test5() throws BaseXException {
    for(int i = 0; i < 25; i++) {
      run(new Add("a", "<x c='c'/>"));
      run(new Add("a", "<x a='a' b='b'/>"));
      run(new Replace("a", "<x/>"));
    }
  }

  /**
   * Runs the specified command.
   * @param cmd command to be run
   * @return string result
   * @throws BaseXException database exception
   */
  private static String run(final Command cmd) throws BaseXException {
    return cmd.execute(context);
  }
}
