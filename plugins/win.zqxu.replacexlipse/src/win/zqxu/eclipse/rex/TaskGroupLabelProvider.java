package win.zqxu.eclipse.rex;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.TextStyle;

public class TaskGroupLabelProvider extends StyledCellLabelProvider {
  private TextStyle nameStyle = new TextStyle();;

  public TaskGroupLabelProvider(TableViewer owner) {
    Font font = owner.getTable().getFont();
    String fontName = font.getFontData()[0].getName();
    int fontHeight = font.getFontData()[0].getHeight();
    nameStyle.font = new Font(font.getDevice(), fontName, fontHeight, SWT.BOLD);
  }

  @Override
  public void update(ViewerCell cell) {
    TaskGroup taskGroup = (TaskGroup) cell.getElement();
    String groupName = taskGroup.getName();
    StyleRange nameRange = new StyleRange(nameStyle);
    nameRange.length = groupName.length();
    cell.setStyleRanges(new StyleRange[]{nameRange});
    cell.setText(groupName + "\n" + taskGroup.getDesc());
  }
}