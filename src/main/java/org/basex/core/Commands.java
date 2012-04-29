package org.basex.core;

import static org.basex.core.Text.*;

/**
 * This class defines the available command-line commands.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Christian Gruen
 */
@SuppressWarnings("all")
public interface Commands {
  /** Create commands. */
  enum CmdCreate { DATABASE, DB, INDEX, USER, BACKUP, EVENT }
  /** Info commands. */
  enum CmdInfo { NULL, DATABASE, DB, INDEX, STORAGE }
  /** Drop commands. */
  enum CmdDrop { DATABASE, DB, INDEX, USER, BACKUP, EVENT }
  /** Optimize commands. */
  enum CmdOptimize { NULL, ALL }
  /** Show commands. */
  enum CmdShow { DATABASES, SESSIONS, USERS, BACKUPS, EVENTS }
  /** Permission commands. */
  enum CmdPerm { NONE, READ, WRITE, CREATE, ADMIN }
  /** Index types. */
  enum CmdIndex { TEXT, ATTRIBUTE, FULLTEXT }
  /** Index types. */
  enum CmdIndexInfo { NULL, TEXT, ATTRIBUTE, FULLTEXT, PATH, TAG, ATTNAME }
  /** Alter types. */
  enum CmdAlter { DATABASE, DB, USER }
  /** Repo types. */
  enum CmdRepo { INSTALL, DELETE, LIST }

  /** Command definitions. */
  enum Cmd {
    ADD(HELPADD), ALTER(HELPALTER), CHECK(HELPCHECK), CLOSE(HELPCLOSE),
    COPY(HELPCOPY), CREATE(HELPCREATE), CS(HELPCS), DELETE(HELPDELETE),
    DROP(HELPDROP), EXIT(HELPEXIT), EXPORT(HELPEXPORT), FIND(HELPFIND),
    FLUSH(HELPFLUSH), GET(HELPGET), GRANT(HELPGRANT), HELP(HELPHELP),
    INFO(HELPINFO), KILL(HELPKILL), LIST(HELPLIST), OPEN(HELPOPEN),
    OPTIMIZE(HELPOPTIMIZE), PASSWORD(HELPPASSWORD), RENAME(HELPRENAME),
    REPLACE(HELPREPLACE), REPO(HELPREPO), RESTORE(HELPRESTORE),
    RETRIEVE(HELPRETRIEVE), RUN(HELPRUN), SET(HELPSET),
    SHOW(HELPSHOW), STORE(HELPSTORE), XQUERY(HELPXQUERY);

    /** Help texts. */
    private final String[] help;

    /**
     * Default constructor.
     * @param h help texts, or {@code null} if command is hidden.
     */
    Cmd(final String... h) {
      help = h;
    }

    /**
     * Returns a help string.
     * @param detail show details
     * @return string
     */
    public final String help(final boolean detail) {
      final StringBuilder sb = new StringBuilder();
      if(help == null) {
        if(detail) sb.append(NOHELP).append(NL);
      } else {
        sb.append(this + " " + help[0] + NL + "  " + help[1] + NL);
        if(detail) sb.append(NL + help[2] + NL);
      }
      return sb.toString();
    }
  }
}
