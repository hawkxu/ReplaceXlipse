package win.zqxu.eclipse.rex;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.jface.text.IFindReplaceTargetExtension;
import org.eclipse.jface.text.IFindReplaceTargetExtension3;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

public class ReplaceSupport implements ShellListener, IPartListener {
  private ReplaceDialog fDialog;
  private IWorkbenchWindow fWindow;
  private IFindReplaceTarget fTarget;
  private IFindReplaceTargetExtension fTargetExtension;

  public ReplaceSupport(ReplaceDialog dialog) {
    fDialog = dialog;
    fDialog.getShell().addShellListener(this);
    fWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    fWindow.getPartService().addPartListener(this);
    updateTarget(fWindow.getActivePage().getActivePart());
  }

  @Override
  public void shellActivated(ShellEvent e) {
    updateReplaceInSelection(true);
  }

  @Override
  public void shellClosed(ShellEvent e) {
    setReplaceInSelection(false);
    updateTarget(null);
    fWindow.getPartService().removePartListener(this);
  }

  @Override
  public void shellDeactivated(ShellEvent e) {
    updateReplaceInSelection(false);
  }

  @Override
  public void shellDeiconified(ShellEvent e) {
  }

  @Override
  public void shellIconified(ShellEvent e) {
  }

  @Override
  public void partActivated(IWorkbenchPart part) {
    updateTarget(part);
    fDialog.updateRunButtons();
  }

  @Override
  public void partBroughtToTop(IWorkbenchPart part) {
  }

  @Override
  public void partClosed(IWorkbenchPart part) {
  }

  @Override
  public void partDeactivated(IWorkbenchPart part) {
  }

  @Override
  public void partOpened(IWorkbenchPart part) {
  }

  private void updateTarget(IWorkbenchPart part) {
    if (fTargetExtension != null) fTargetExtension.endSession();
    fTarget = null;
    if (part instanceof ITextEditor) {
      fTarget = (IFindReplaceTarget) part.getAdapter(IFindReplaceTarget.class);
      if (fTarget != null && !fTarget.isEditable()) fTarget = null;
    }
    fTargetExtension = null;
    if (fTarget instanceof IFindReplaceTargetExtension) {
      fTargetExtension = (IFindReplaceTargetExtension) fTarget;
      fTargetExtension.beginSession();
    }
  }

  public boolean isReplaceSupported() {
    return fTarget != null;
  }

  public boolean isRegexSupported() {
    return fTarget instanceof IFindReplaceTargetExtension3;
  }

  private void updateReplaceInSelection(boolean activated) {
    boolean supported = fTargetExtension != null;
    if (supported) {
      activated &= fTarget.getSelectionText().contains("\n");
      setReplaceInSelection(activated);
    }
    fDialog.updateReplaceInSelectionButton(supported, activated);
  }

  public void setReplaceInSelection(boolean replaceInSelection) {
    if (fTargetExtension == null) return;
    if (replaceInSelection) {
      Point lineSelection = fTargetExtension.getLineSelection();
      IRegion scope = new Region(lineSelection.x, lineSelection.y);
      fTargetExtension.setSelection(scope.getOffset(), 0);
      fTargetExtension.setScope(scope);
    } else if (fTargetExtension.getScope() != null) {
      IRegion scope = fTargetExtension.getScope();
      fTargetExtension.setScope(null);
      fTargetExtension.setSelection(scope.getOffset(), scope.getLength());
    }
  }

  public void performReplace(List<TaskItem> itemList) {
    ReplaceRunnable runnable = new ReplaceRunnable(fTarget, itemList);
    BusyIndicator.showWhile(fDialog.getShell().getDisplay(), runnable);
  }

  private static class ReplaceRunnable implements Runnable {
    private IFindReplaceTarget fTarget;
    private IFindReplaceTargetExtension3 fTarget3;
    private List<TaskItem> itemList;

    public ReplaceRunnable(IFindReplaceTarget fTarget, List<TaskItem> itemList) {
      this.fTarget = fTarget;
      if (fTarget instanceof IFindReplaceTargetExtension3)
        fTarget3 = (IFindReplaceTargetExtension3) fTarget;
      this.itemList = itemList;
    }

    @Override
    public void run() {
      for (TaskItem taskItem : itemList)
        performReplace(taskItem);
    }

    private void performReplace(TaskItem taskItem) {
      String search = taskItem.getSearch();
      boolean matchCase = taskItem.isMatchCase();
      boolean matchWord = taskItem.isMatchWord();
      boolean regex = taskItem.isRegex();
      CharCase toCase = taskItem.getToCase();
      String replace = taskItem.getReplace();
      int offset = 0;
      while (true) {
        if (fTarget3 == null)
          offset = fTarget.findAndSelect(offset, search, true, matchCase, matchWord);
        else
          offset = fTarget3.findAndSelect(offset, search, true, matchCase, matchWord, regex);
        if (offset == -1) return;
        if (toCase != null) {
          String found = fTarget.getSelectionText();
          replace = convertCase(found, search, matchCase, toCase);
        }
        if (fTarget3 == null)
          fTarget.replaceSelection(replace);
        else
          fTarget3.replaceSelection(replace, regex);
        if (taskItem.isFirst()) return;
        Point selection = fTarget.getSelection();
        offset = selection.x + selection.y;
      }
    }

    private String convertCase(String found, String search, boolean matchCase, CharCase toCase) {
      int flags = matchCase ? 0 : Pattern.CASE_INSENSITIVE;
      Pattern p = Pattern.compile(search, flags);
      Matcher m = p.matcher(found);
      if (!m.find() || m.groupCount() < 1) return found; // this is impossible
      StringBuilder group = new StringBuilder();
      int start = m.start(0);
      for (int i = 1; i <= m.groupCount(); i++) {
        if (m.start(i) < start) continue;
        group.append(found.substring(start, m.start(i)));
        start = m.end(i);
        if (toCase == CharCase.UPPER)
          group.append(m.group(i).toUpperCase());
        else
          group.append(m.group(i).toLowerCase());
      }
      group.append(found.substring(start, m.end(0)));
      StringBuffer replacement = new StringBuffer();
      m.appendReplacement(replacement, group.toString());
      return m.appendTail(replacement).toString();
    }
  }
}
