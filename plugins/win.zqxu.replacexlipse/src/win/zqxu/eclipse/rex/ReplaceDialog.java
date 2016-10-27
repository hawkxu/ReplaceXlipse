package win.zqxu.eclipse.rex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.ResourceManager;

public class ReplaceDialog extends Dialog implements Constants {
  private ToolItem tlmRunGroup;
  private ToolItem tlmRunItem;
  private ToolItem tlmCreateGroup;
  private ToolItem tlmModifyGroup;
  private ToolItem tlmRemoveGroup;
  private ToolItem tlmCreateItem;
  private ToolItem tlmModifyItem;
  private ToolItem tlmRemoveItem;
  private TableViewer tbvGroup;
  private Table tblItem;
  private TableViewer tbvItem;
  private Table tblGroup;
  private int defaultRowHeight;
  private Composite composite;
  private Label lblNewLabel;
  private Button ckbInSelection;
  private ReplaceSupport fSupport;

  /**
   * Create the dialog.
   * 
   * @param parentShell
   */
  public ReplaceDialog() {
    super(Display.getCurrent().getActiveShell());
    setBlockOnOpen(false);
    setShellStyle(getShellStyle() ^ SWT.APPLICATION_MODAL | SWT.MODELESS | SWT.SHELL_TRIM);
  }

  /**
   * Create contents of the dialog.
   * 
   * @param parent
   */
  @Override
  protected Control createDialogArea(Composite parent) {
    Composite container = (Composite) super.createDialogArea(parent);
    container.setLayout(new GridLayout(2, false));

    ToolBar toolBar = new ToolBar(container, SWT.FLAT | SWT.RIGHT);
    toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

    tlmRunGroup = new ToolItem(toolBar, SWT.NONE);
    tlmRunGroup.setToolTipText(Messages.RUN_TASK_GROUP);
    tlmRunGroup.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        handleRunGroup();
      }
    });
    tlmRunGroup.setImage(ResourceManager.getPluginImage(symbolicName, "icons/book_go.png")); //$NON-NLS-1$

    tlmRunItem = new ToolItem(toolBar, SWT.NONE);
    tlmRunItem.setToolTipText(Messages.RUN_TASK_ITEM);
    tlmRunItem.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        handleRunItem();
      }
    });
    tlmRunItem.setImage(ResourceManager.getPluginImage(symbolicName, "icons/style_go.png")); //$NON-NLS-1$

    new ToolItem(toolBar, SWT.SEPARATOR);

    tlmCreateGroup = new ToolItem(toolBar, SWT.NONE);
    tlmCreateGroup.setToolTipText(Messages.CREATE_TASK_GROUP);
    tlmCreateGroup.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        handleCreateGroup();
      }
    });
    tlmCreateGroup.setImage(ResourceManager.getPluginImage(symbolicName, "icons/book_add.png")); //$NON-NLS-1$

    tlmModifyGroup = new ToolItem(toolBar, SWT.NONE);
    tlmModifyGroup.setToolTipText(Messages.MODIFY_TASK_GROUP);
    tlmModifyGroup.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        handleModifyGroup();
      }
    });
    tlmModifyGroup.setImage(ResourceManager.getPluginImage(symbolicName, "icons/book_edit.png")); //$NON-NLS-1$

    tlmRemoveGroup = new ToolItem(toolBar, SWT.NONE);
    tlmRemoveGroup.setToolTipText(Messages.REMOVE_TASK_GROUP);
    tlmRemoveGroup.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        handleRemoveGroup();
      }
    });
    tlmRemoveGroup
        .setImage(ResourceManager.getPluginImage(symbolicName, "icons/book_delete.png")); //$NON-NLS-1$

    new ToolItem(toolBar, SWT.SEPARATOR);

    tlmCreateItem = new ToolItem(toolBar, SWT.NONE);
    tlmCreateItem.setToolTipText(Messages.CREATE_TASK_ITEM);
    tlmCreateItem.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        handleCreateItem();
      }
    });
    tlmCreateItem.setImage(ResourceManager.getPluginImage(symbolicName, "icons/style_add.png")); //$NON-NLS-1$

    tlmModifyItem = new ToolItem(toolBar, SWT.NONE);
    tlmModifyItem.setToolTipText(Messages.MODIFY_TASK_ITEM);
    tlmModifyItem.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        handleModifyItem();
      }
    });
    tlmModifyItem.setImage(ResourceManager.getPluginImage(symbolicName, "icons/style_edit.png")); //$NON-NLS-1$

    tlmRemoveItem = new ToolItem(toolBar, SWT.NONE);
    tlmRemoveItem.setToolTipText(Messages.REMOVE_TASK_ITEM);
    tlmRemoveItem.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        handleRemoveItem();
      }
    });
    tlmRemoveItem
        .setImage(ResourceManager.getPluginImage(symbolicName, "icons/style_delete.png")); //$NON-NLS-1$

    composite = new Composite(container, SWT.NONE);
    composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    GridLayout gl_composite = new GridLayout(3, false);
    gl_composite.verticalSpacing = 0;
    gl_composite.marginLeft = 18;
    gl_composite.horizontalSpacing = 10;
    composite.setLayout(gl_composite);

    lblNewLabel = new Label(composite, SWT.NONE);
    lblNewLabel.setText(Messages.LABEL_RANGE);

    ckbInSelection = new Button(composite, SWT.CHECK);
    ckbInSelection.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        fSupport.setReplaceInSelection(ckbInSelection.getSelection());
      }
    });
    ckbInSelection.setText(Messages.REPLACE_IN_SELECTION);

    ToolBar toolBar_1 = new ToolBar(composite, SWT.FLAT | SWT.RIGHT);
    toolBar_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, true, 1, 1));

    ToolItem tlmAbout = new ToolItem(toolBar_1, SWT.NONE);
    tlmAbout.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        new AboutDialog(getShell()).open();
      }
    });
    tlmAbout.setImage(ResourceManager.getPluginImage(symbolicName, "icons/help.png")); //$NON-NLS-1$

    SashForm sashForm = new SashForm(container, SWT.NONE);
    sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

    Group grpTaskGroup = new Group(sashForm, SWT.NONE);
    grpTaskGroup.setText(Messages.TASK_GROUP);
    grpTaskGroup.setLayout(new GridLayout(1, false));

    tbvGroup = new TableViewer(grpTaskGroup, SWT.BORDER | SWT.FULL_SELECTION);
    tblGroup = tbvGroup.getTable();
    tblGroup.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseDoubleClick(MouseEvent event) {
        TableItem row = tblGroup.getItem(new Point(event.x, event.y));
        if (row == null) return;
        modifyTaskGroup(tblGroup.indexOf(row), (TaskGroup) row.getData());
      }
    });
    tblGroup.addControlListener(new ControlAdapter() {
      @Override
      public void controlResized(ControlEvent event) {
        resizeColumnToFit(event);
      }
    });
    tblGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    tblGroup.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        int old = (Integer) e.widget.getData("selection"); //$NON-NLS-1$
        if (old == tblGroup.getSelectionIndex()) return;
        e.widget.setData("selection", tblGroup.getSelectionIndex()); //$NON-NLS-1$
        updateItemTable();
        updateButtonsEnabled();
      }
    });

    TableColumn tclGroup = new TableColumn(tblGroup, SWT.NONE);
    tclGroup.setWidth(100);

    Group grpReplacement = new Group(sashForm, SWT.NONE);
    grpReplacement.setText(Messages.TASK_ITEM);
    grpReplacement.setLayout(new GridLayout(1, false));

    tbvItem = new TableViewer(grpReplacement, SWT.BORDER | SWT.FULL_SELECTION);
    tblItem = tbvItem.getTable();
    tblItem.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseDoubleClick(MouseEvent event) {
        TableItem row = tblItem.getItem(new Point(event.x, event.y));
        if (row == null) return;
        modifyTaskItem(tblItem.indexOf(row), (TaskItem) row.getData());
      }
    });
    tblItem.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        updateButtonsEnabled();
      }
    });
    tblItem.addControlListener(new ControlAdapter() {
      @Override
      public void controlResized(ControlEvent event) {
        resizeColumnToFit(event);
      }
    });
    tblItem.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

    TableColumn tclItem = new TableColumn(tblItem, SWT.NONE);
    tclItem.setWidth(100);
    sashForm.setWeights(new int[]{1, 2});

    getShell().setImage(ResourceManager.getPluginImage(symbolicName, "icons/text_replace.png")); //$NON-NLS-1$
    getShell().setText(Messages.REPLACE_DIALOG_TITLE);
    return container;
  }

  private void resizeColumnToFit(ControlEvent event) {
    Table table = (Table) event.widget;
    table.getColumn(0).setWidth(table.getClientArea().width);
    if (table.getHorizontalBar() != null)
      table.getHorizontalBar().setVisible(false);
  }

  @Override
  protected Control createButtonBar(Composite parent) {
    initialize();
    return null;
  }

  /**
   * Return the initial size of the dialog.
   */
  @Override
  protected Point getInitialSize() {
    return new Point(555, 416);
  }

  private void initialize() {
    fSupport = new ReplaceSupport(this);
    installLabelProvider();
    loadTaskGroups();
    updateButtonsEnabled();
    initDragSupport(tblGroup, TaskGroupTransfer.getInstance());
    initDragSupport(tblItem, TaskItemTransfer.getInstance());
  }

  public void updateRunButtons() {
    boolean supported = fSupport.isReplaceSupported();
    tlmRunGroup.setEnabled(supported && tblGroup.getSelectionCount() == 1);
    tlmRunItem.setEnabled(supported && tblItem.getSelectionCount() == 1);
  }

  public void updateReplaceInSelectionButton(boolean supported, boolean checked) {
    ckbInSelection.setEnabled(supported);
    ckbInSelection.setSelection(supported && checked);
  }

  private void installLabelProvider() {
    tblGroup.addListener(SWT.MeasureItem, new Listener() {
      @Override
      public void handleEvent(Event event) {
        event.height = getDefaultRowHeight(event);
      }
    });
    tblItem.addListener(SWT.MeasureItem, new Listener() {
      @Override
      public void handleEvent(Event event) {
        event.height = getDefaultRowHeight(event);
      }
    });
    tbvGroup.setLabelProvider(new TaskGroupLabelProvider(tbvGroup));
    tbvItem.setLabelProvider(new TaskItemLabelProvider(tbvItem));
  }

  private int getDefaultRowHeight(Event event) {
    if (defaultRowHeight == 0) {
      defaultRowHeight = event.gc.textExtent("H\nd").y + 6; //$NON-NLS-1$
    }
    return defaultRowHeight;
  }

  private void loadTaskGroups() {
    try {
      tblGroup.setData("selection", -1); //$NON-NLS-1$
      for (TaskGroup taskGroup : TaskStore.readGroupList())
        tbvGroup.add(taskGroup);
      performTableViewerSelect(tbvGroup, 0);
    } catch (Exception ex) {
      ReplaceUtils.showException(getShell(), ex);
    }
  }

  private void updateItemTable() {
    try {
      int groupIndex = tblGroup.getSelectionIndex();
      tbvItem.setItemCount(0);
      if (groupIndex == -1) return;
      for (TaskItem taskItem : TaskStore.readItemList(groupIndex))
        tbvItem.add(taskItem);
      performTableViewerSelect(tbvItem, 0);
    } catch (Exception ex) {
      ReplaceUtils.showException(getShell(), ex);
    }
  }

  private void updateButtonsEnabled() {
    updateRunButtons();
    tlmModifyGroup.setEnabled(tblGroup.getSelectionCount() == 1);
    tlmRemoveGroup.setEnabled(tblGroup.getSelectionCount() == 1);
    tlmCreateItem.setEnabled(tblGroup.getSelectionCount() == 1);
    tlmModifyItem.setEnabled(tblItem.getSelectionCount() == 1);
    tlmRemoveItem.setEnabled(tblItem.getSelectionCount() == 1);
  }

  private void handleRunGroup() {
    List<TaskItem> itemList = new ArrayList<TaskItem>();
    for (int i = 0; i < tblItem.getItemCount(); i++)
      itemList.add((TaskItem) tbvItem.getElementAt(i));
    performRunTask(itemList);
  }

  private void handleRunItem() {
    int index = tblItem.getSelectionIndex();
    TaskItem taskItem = (TaskItem) tbvItem.getElementAt(index);
    performRunTask(Arrays.asList(taskItem));
  }

  private void performRunTask(List<TaskItem> itemList) {
    for (TaskItem taskItem : itemList) {
      if (taskItem.isRegex()) {
        if (!fSupport.isRegexSupported()) {
          ReplaceUtils.showError(getShell(),
              Messages.MSG_NO_REGEX);
          return;
        }
        break;
      }
    }
    fSupport.performReplace(itemList);
  }

  private void handleCreateGroup() {
    modifyTaskGroup(-1, null);
  }

  private void handleModifyGroup() {
    int groupIndex = tblGroup.getSelectionIndex();
    TaskGroup taskGroup = (TaskGroup) tbvGroup.getElementAt(groupIndex);
    modifyTaskGroup(groupIndex, taskGroup);
  }

  private void modifyTaskGroup(int groupIndex, TaskGroup taskGroup) {
    TaskGroupDialog dlgGroup = new TaskGroupDialog(getShell());
    dlgGroup.setTaskGroup(taskGroup);
    try {
      if (dlgGroup.open() == Dialog.OK) {
        TaskGroup changed = dlgGroup.getTaskGroup();
        if (changed.fullEqulas(taskGroup)) return;
        TaskStore.saveTaskGroup(groupIndex, changed);
        if (groupIndex != -1)
          tbvGroup.replace(changed, groupIndex);
        else
          performTableViewerAdd(tbvGroup, changed);
      }
    } catch (Exception ex) {
      ReplaceUtils.showException(getShell(), ex);
    }
  }

  private void handleRemoveGroup() {
    int groupIndex = tblGroup.getSelectionIndex();
    TaskGroup taskGroup = (TaskGroup) tblGroup.getItem(groupIndex).getData();
    if (!ReplaceUtils.showConfirm(getShell(),
        String.format(Messages.MSG_CONFIRM_REMOVE_GROUP, taskGroup.getName())))
      return;
    try {
      TaskStore.removeTaskGroup(groupIndex);
      performTableViewerRemove(tbvGroup, groupIndex);
      updateButtonsEnabled();
    } catch (Exception ex) {
      ReplaceUtils.showException(getShell(), ex);
    }
  }

  private void handleCreateItem() {
    modifyTaskItem(-1, null);
  }

  private void handleModifyItem() {
    int itemIndex = tblItem.getSelectionIndex();
    TaskItem taskItem = (TaskItem) tblItem.getItem(itemIndex).getData();
    modifyTaskItem(itemIndex, taskItem);
  }

  private void modifyTaskItem(int itemIndex, TaskItem taskItem) {
    TaskItemDialog dlgItem = new TaskItemDialog(getShell());
    dlgItem.setTaskItem(taskItem);
    try {
      int groupIndex = tblGroup.getSelectionIndex();
      if (dlgItem.open() == Dialog.OK) {
        TaskItem changed = dlgItem.getTaskItem();
        if (changed.fullEquals(taskItem)) return;
        TaskStore.saveTaskItem(groupIndex, itemIndex, changed);
        if (itemIndex != -1)
          tbvItem.replace(changed, itemIndex);
        else
          performTableViewerAdd(tbvItem, changed);
      }
    } catch (Exception ex) {
      ReplaceUtils.showException(getShell(), ex);
    }
  }

  private void handleRemoveItem() {
    int groupIndex = tblGroup.getSelectionIndex();
    int itemIndex = tblItem.getSelectionIndex();
    try {
      TaskStore.removeTaskItem(groupIndex, itemIndex);
      performTableViewerRemove(tbvItem, itemIndex);
      updateButtonsEnabled();
    } catch (Exception ex) {
      ReplaceUtils.showException(getShell(), ex);
    }
  }

  private void performTableViewerAdd(TableViewer tableViewer, Object element) {
    tableViewer.add(element);
    performTableViewerSelect(tableViewer, tableViewer.getTable().getItemCount() - 1);
  }

  private void performTableViewerRemove(TableViewer tableViewer, int index) {
    tableViewer.remove(tableViewer.getElementAt(index));
    if (index >= tableViewer.getTable().getItemCount())
      index = tableViewer.getTable().getItemCount() - 1;
    performTableViewerSelect(tableViewer, index);
  }

  private void performTableViewerSelect(TableViewer tableViewer, int index) {
    StructuredSelection selection = null;
    Object element = tableViewer.getElementAt(index);
    if (element != null)
      selection = new StructuredSelection(element);
    tableViewer.setSelection(selection, true);
    tableViewer.getTable().notifyListeners(SWT.Selection, new Event());
  }

  private void initDragSupport(Table tblTask, Transfer transfer) {
    DragSource source = new DragSource(tblTask, DND.DROP_MOVE);
    source.setTransfer(new Transfer[]{transfer});
    source.addDragListener(new DragSourceAdapter() {
      public void dragSetData(DragSourceEvent event) {
        DragSource ds = (DragSource) event.widget;
        Table table = (Table) ds.getControl();
        event.data = table.getSelectionIndex();
      }
    });

    DropTarget target = new DropTarget(tblTask, DND.DROP_MOVE);
    target.setTransfer(new Transfer[]{transfer});
    target.addDropListener(new DropTargetAdapter() {
      public void dragOver(DropTargetEvent event) {
        event.feedback = getInsertPosition(event) | DND.FEEDBACK_SCROLL;
      }

      public void drop(DropTargetEvent event) {
        int position = getInsertPosition(event);
        if (position == DND.FEEDBACK_NONE) return;
        DropTarget target = (DropTarget) event.widget;
        Table table = (Table) target.getControl();
        int from = (Integer) event.data;
        int to = table.indexOf((TableItem) event.item);
        if (position == DND.FEEDBACK_INSERT_AFTER)
          to++;
        if (to > from) to--;
        if (from == to) return;
        handleMoveTaskObject(table, from, to);
      }

      private int getInsertPosition(DropTargetEvent event) {
        if (event.item == null) return DND.FEEDBACK_NONE;
        DropTarget target = (DropTarget) event.widget;
        Table table = (Table) target.getControl();
        Rectangle bounds = ((TableItem) event.item).getBounds();
        int half = table.toDisplay(0, bounds.y).y + bounds.height / 2;
        return event.y >= half ? DND.FEEDBACK_INSERT_AFTER : DND.FEEDBACK_INSERT_BEFORE;
      }
    });
  }

  private void handleMoveTaskObject(Table table, int from, int to) {
    TableViewer tbvTask = tbvGroup;
    try {
      if (table == tblGroup) {
        TaskStore.moveTaskGroup(from, to);
      } else {
        tbvTask = tbvItem;
        int groupIndex = tblGroup.getSelectionIndex();
        TaskStore.moveTaskItem(groupIndex, from, to);
      }
      Object element = tbvTask.getElementAt(from);
      tbvTask.remove(element);
      tbvTask.insert(element, to);
      table.setSelection(to);
    } catch (Exception ex) {
      ReplaceUtils.showException(getShell(), ex);
    }
  }
}
