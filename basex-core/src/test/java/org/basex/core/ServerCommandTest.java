package org.basex.core;

import static org.junit.Assert.*;

import java.io.*;

import org.basex.*;
import org.basex.api.client.*;
import org.basex.core.cmd.*;
import org.basex.core.users.*;
import org.basex.util.*;
import org.junit.*;
import org.junit.Test;

/**
 * This class tests the database commands with the client/server architecture.
 *
 * @author BaseX Team 2005-17, BSD License
 * @author Christian Gruen
 */
public final class ServerCommandTest extends CommandTest {
  /** Server instance. */
  private static BaseXServer server;

  /**
   * Starts the server.
   * @throws IOException I/O exception
   */
  @BeforeClass
  public static void start() throws IOException {
    server = createServer();
    session = createClient();
    cleanUp();
  }

  /**
   * Stops the server.
   */
  @AfterClass
  public static void stop() {
    try {
      if(session != null) session.close();
    } catch(final Exception ex) {
      fail(Util.message(ex));
    }
    stopServer(server);
  }

  /**
   * Kill test.
   * @throws IOException on server error
   */
  @Test
  public void kill() throws IOException {
    ok(new Kill(UserText.ADMIN));
    ok(new Kill(UserText.ADMIN + '2'));
    ok(new Kill(Prop.NAME + '*'));
    ok(new CreateUser(NAME2, NAME2));
    try(ClientSession cs = createClient(NAME2, NAME2)) {
      ok(new Kill(NAME2));
      ok(new Kill(NAME2 + '?'));
    }
  }
}
