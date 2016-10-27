package win.zqxu.eclipse.rex.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import win.zqxu.eclipse.rex.ReplaceDialog;

/**
 * Replace command handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class ReplaceHandler extends AbstractHandler {
  private ReplaceDialog dialog;

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    if (dialog == null) {
      dialog = new ReplaceDialog();
    }
    dialog.open();
    return null;
  }
}
