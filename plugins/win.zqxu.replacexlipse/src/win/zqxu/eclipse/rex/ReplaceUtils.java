package win.zqxu.eclipse.rex;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class ReplaceUtils {
  public static void showError(Shell shell, String message) {
    MessageDialog.openError(shell, Messages.MSG_TITLE_ERROR, message);
  }

  public static void showException(Shell shell, Exception ex) {
    ex.printStackTrace();
    showError(shell, ex.getMessage());
  }

  public static boolean showConfirm(Shell shell, String message) {
    return MessageDialog.openConfirm(shell, Messages.MSG_TITLE_CONFIRM, message);
  }
}
