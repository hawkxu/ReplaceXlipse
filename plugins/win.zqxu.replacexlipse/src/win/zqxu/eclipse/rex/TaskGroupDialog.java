package win.zqxu.eclipse.rex;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;

public class TaskGroupDialog extends Dialog implements Constants {
  private Text txtName;
  private Text txtDesc;
  private TaskGroup taskGroup;

  /**
   * Create the dialog.
   * 
   * @param parentShell
   */
  public TaskGroupDialog(Shell parentShell) {
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

    Label lblNewLabel = new Label(container, SWT.NONE);
    lblNewLabel.setText(Messages.LABEL_GROUP_NAME);

    txtName = new Text(container, SWT.BORDER);
    GridData gd_txtName = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
    gd_txtName.widthHint = 300;
    txtName.setLayoutData(gd_txtName);

    Label lblNewLabel_1 = new Label(container, SWT.NONE);
    lblNewLabel_1.setText(Messages.LABEL_GROUP_DESC);

    txtDesc = new Text(container, SWT.BORDER);
    txtDesc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

    return container;
  }

  /**
   * Create contents of the button bar.
   * 
   * @param parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    updateTaskGroup();
    Button btnOK = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    btnOK.setImage(ResourceManager.getPluginImage(symbolicName, "icons/accept.png")); //$NON-NLS-1$
    Button btnCancel = createButton(parent, IDialogConstants.CANCEL_ID,
        IDialogConstants.CANCEL_LABEL, false);
    btnCancel.setImage(ResourceManager.getPluginImage(symbolicName, "icons/cross.png")); //$NON-NLS-1$
  }

  private void updateTaskGroup() {
    if (taskGroup == null) {
      getShell().setText(Messages.CREATE_TASK_GROUP);
      getShell().setImage(ResourceManager.getPluginImage(symbolicName, "icons/book_add.png")); //$NON-NLS-1$
    } else {
      txtName.setText(taskGroup.getName());
      txtDesc.setText(taskGroup.getDesc());
      getShell().setText(Messages.MODIFY_TASK_GROUP);
      getShell().setImage(ResourceManager.getPluginImage(symbolicName, "icons/book_edit.png")); //$NON-NLS-1$
    }
  }

  @Override
  protected void okPressed() {
    if (checkInput()) {
      buildTaskGroup();
      super.okPressed();
    }
  }

  private boolean checkInput() {
    if (txtName.getText().trim().isEmpty())
      ReplaceUtils.showError(getShell(), Messages.MSG_INPUT_GROUP_NAME);
    return !txtName.getText().trim().isEmpty();
  }

  private void buildTaskGroup() {
    taskGroup = new TaskGroup();
    taskGroup.setName(txtName.getText().trim());
    taskGroup.setDesc(txtDesc.getText().trim());
  }

  public TaskGroup getTaskGroup() {
    return taskGroup;
  }

  public void setTaskGroup(TaskGroup taskGroup) {
    this.taskGroup = taskGroup;
  }
}
