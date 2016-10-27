package win.zqxu.eclipse.rex;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
  private static final String BUNDLE_NAME = "win.zqxu.eclipse.rex.messages"; //$NON-NLS-1$
  static {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages() {
  }

  public static String ABOUT_AUTHOR;
  public static String ABOUT_DESC;
  public static String ABOUT_DIALOG_TITLE;
  public static String ABOUT_ECLIPSE_LICENSE;
  public static String ABOUT_ICONS_FROM;
  public static String ABOUT_LABEL_AUTHOR;
  public static String ABOUT_LABEL_LICENSE;
  public static String ABOUT_LABEL_VERSION;
  public static String ABOUT_LOVE_BABY;
  public static String ABOUT_SILK_ICONS;
  public static String CREATE_TASK_GROUP;
  public static String CREATE_TASK_ITEM;
  public static String LABEL_DESC;
  public static String LABEL_FIRST_ONLY;
  public static String LABEL_GROUP_DESC;
  public static String LABEL_GROUP_NAME;
  public static String LABEL_MATCH_CASE;
  public static String LABEL_MATCH_WORD;
  public static String LABEL_RANGE;
  public static String LABEL_REGEX;
  public static String LABEL_REPLACE;
  public static String LABEL_SEARCH;
  public static String MODIFY_TASK_GROUP;
  public static String MODIFY_TASK_ITEM;
  public static String MSG_CONFIRM_REMOVE_GROUP;
  public static String MSG_INPUT_GROUP_NAME;
  public static String MSG_INPUT_SEARCH;
  public static String MSG_INVALID_REGEX;
  public static String MSG_NO_REGEX;
  public static String MSG_TITLE_CONFIRM;
  public static String MSG_TITLE_ERROR;
  public static String REMOVE_TASK_GROUP;
  public static String REMOVE_TASK_ITEM;
  public static String RENDER_FIRST;
  public static String RENDER_REMOVE;
  public static String RENDER_REPLACE;
  public static String RENDER_TO_LOWER;
  public static String RENDER_TO_UPPER;
  public static String RENDER_WITH;
  public static String REPLACE_DIALOG_TITLE;
  public static String REPLACE_IN_SELECTION;
  public static String RUN_TASK_GROUP;
  public static String RUN_TASK_ITEM;
  public static String TASK_GROUP;
  public static String TASK_ITEM;
  public static String TO_LOWER_TEXT;
  public static String TO_LOWER_TIP;
  public static String TO_UPPER_TEXT;
  public static String TO_UPPER_TIP;
}
