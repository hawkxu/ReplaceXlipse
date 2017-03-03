package win.zqxu.eclipse.rex;

import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;

public class TaskItemDialog extends Dialog implements Constants {
  private Text txtSearch;
  private Text txtReplace;
  private Button ckbRegex;
  private Button ckbUpper;
  private Button ckbLower;
  private Text txtDesc;
  private TaskItem taskItem;
  private Button ckbFirst;
  private Button ckbMatchWord;
  private Button ckbMatchCase;

  /**
   * Create the dialog.
   * 
   * @param parentShell
   */
  public TaskItemDialog(Shell parentShell) {
    super(parentShell);
  }

  /**
   * Create contents of the dialog.
   * 
   * @param parent
   */
  @Override
  protected Control createDialogArea(Composite parent) {
    Composite container = (Composite) super.createDialogArea(parent);
    GridLayout gridLayout = (GridLayout) container.getLayout();
    gridLayout.numColumns = 2;

    Label lblSearch = new Label(container, SWT.NONE);
    lblSearch.setText(Messages.LABEL_SEARCH);

    txtSearch = new Text(container, SWT.BORDER);
    txtSearch.addModifyListener(new ModifyListener() {
      public void modifyText(ModifyEvent e) {
        handleSearchModified();
      }
    });
    GridData gd_txtSearch = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
    gd_txtSearch.widthHint = 300;
    txtSearch.setLayoutData(gd_txtSearch);
    new Label(container, SWT.NONE);

    Composite composite = new Composite(container, SWT.NONE);
    composite.setLayout(new GridLayout(2, false));

    ckbMatchCase = new Button(composite, SWT.CHECK);
    ckbMatchCase.setText(Messages.LABEL_MATCH_CASE);

    ckbMatchWord = new Button(composite, SWT.CHECK);
    ckbMatchWord.setText(Messages.LABEL_MATCH_WORD);

    ckbRegex = new Button(composite, SWT.CHECK);
    ckbRegex.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        handleRegexSelected();
      }
    });
    ckbRegex.setBounds(0, 0, 98, 17);
    ckbRegex.setText(Messages.LABEL_REGEX);

    ckbFirst = new Button(composite, SWT.CHECK);
    ckbFirst.setText(Messages.LABEL_FIRST_ONLY);

    ckbUpper = new Button(composite, SWT.CHECK);
    ckbUpper.setToolTipText(Messages.TO_UPPER_TIP);
    ckbUpper.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        handleCharCaseSelected((Button) e.getSource());
      }
    });
    ckbUpper.setText(Messages.TO_UPPER_TEXT);

    ckbLower = new Button(composite, SWT.CHECK);
    ckbLower.setToolTipText(Messages.TO_LOWER_TIP);
    ckbLower.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        handleCharCaseSelected((Button) e.getSource());
      }
    });
    ckbLower.setText(Messages.TO_LOWER_TEXT);

    Label lblReplace = new Label(container, SWT.NONE);
    lblReplace.setText(Messages.LABEL_REPLACE);

    txtReplace = new Text(container, SWT.BORDER);
    txtReplace.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

    Label lblDescription = new Label(container, SWT.NONE);
    lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblDescription.setText(Messages.LABEL_DESC);

    txtDesc = new Text(container, SWT.BORDER);
    txtDesc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

    return container;
  }

  private void handleSearchModified() {
    ckbMatchWord.setEnabled(txtSearch.getText().matches("\\w+")); //$NON-NLS-1$
    if (!ckbMatchWord.isEnabled()) ckbMatchWord.setSelection(false);
    handleRegexSelected();
  }

  private void handleRegexSelected() {
    boolean casable = ckbRegex.getSelection();
    if (casable) {
      String search = txtSearch.getText();
      casable = search.matches("\\(.*|.*[^\\\\]\\(.*"); //$NON-NLS-1$
    }
    if (!casable && (ckbUpper.getSelection() || ckbLower.getSelection())) {
      ckbUpper.setSelection(false);
      ckbLower.setSelection(false);
      handleCharCaseSelected(null);
    }
    ckbUpper.setEnabled(casable);
    ckbLower.setEnabled(casable);
  }

  protected void handleCharCaseSelected(Button source) {
    if (source != null && source.getSelection()) {
      if (source == ckbLower)
        ckbUpper.setSelection(false);
      else
        ckbLower.setSelection(false);
    }
    txtReplace.setEnabled(!ckbUpper.getSelection() && !ckbLower.getSelection());
  }

  /**
   * Create contents of the button bar.
   * 
   * @param parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    updateTaskItem();
    Button btnOK = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    btnOK.setImage(ResourceManager.getPluginImage(symbolicName, "icons/accept.png")); //$NON-NLS-1$
    Button btnCancel = createButton(parent, IDialogConstants.CANCEL_ID,
        IDialogConstants.CANCEL_LABEL, false);
    btnCancel.setImage(ResourceManager.getPluginImage(symbolicName, "icons/cross.png")); //$NON-NLS-1$
  }

  private void updateTaskItem() {
    if (taskItem == null) {
      getShell().setText(Messages.CREATE_TASK_ITEM);
      getShell().setImage(ResourceManager.getPluginImage(symbolicName, "icons/style_add.png")); //$NON-NLS-1$
    } else {
      txtSearch.setText(taskItem.getSearch());
      ckbMatchCase.setSelection(taskItem.isMatchCase());
      ckbMatchWord.setSelection(taskItem.isMatchWord());
      ckbRegex.setSelection(taskItem.isRegex());
      ckbFirst.setSelection(taskItem.isFirst());
      ckbUpper.setSelection(taskItem.getToCase() == CharCase.UPPER);
      ckbLower.setSelection(taskItem.getToCase() == CharCase.LOWER);
      txtReplace.setText(taskItem.getReplace());
      txtDesc.setText(taskItem.getDesc());
      getShell().setText(Messages.MODIFY_TASK_ITEM);
      getShell().setImage(ResourceManager.getPluginImage(symbolicName, "icons/style_edit.png")); //$NON-NLS-1$
    }
    handleSearchModified();
    handleCharCaseSelected(null);
  }

  @Override
  protected void okPressed() {
    if (checkInput()) {
      buildTaskItem();
      super.okPressed();
    }
  }

  private boolean checkInput() {
    String message = null;
    if (txtSearch.getText().isEmpty())
      message = Messages.MSG_INPUT_SEARCH;
    else if (!isValidRegex())
      message = Messages.MSG_INVALID_REGEX;
    if (message != null)
      ReplaceUtils.showError(getShell(), message);
    return message == null;
  }

  private boolean isValidRegex() {
    Exception exception = null;
    try {
      if (ckbRegex.getSelection())
        Pattern.compile(txtSearch.getText());
    } catch (Exception ex) {
      exception = ex;
    }
    return exception == null;
  }

  private void buildTaskItem() {
    taskItem = new TaskItem();
    taskItem.setSearch(txtSearch.getText());
    taskItem.setMatchCase(ckbMatchCase.getSelection());
    taskItem.setMatchWord(ckbMatchWord.getSelection());
    taskItem.setRegex(ckbRegex.getSelection());
    taskItem.setFirst(ckbFirst.getSelection());
    if (ckbUpper.getSelection())
      taskItem.setToCase(CharCase.UPPER);
    else if (ckbLower.getSelection())
      taskItem.setToCase(CharCase.LOWER);
    if (txtReplace.isEnabled())
      taskItem.setReplace(txtReplace.getText());
    taskItem.setDesc(txtDesc.getText().trim());
  }

  public TaskItem getTaskItem() {
    return taskItem;
  }

  public void setTaskItem(TaskItem taskItem) {
    this.taskItem = taskItem;
  }
}
