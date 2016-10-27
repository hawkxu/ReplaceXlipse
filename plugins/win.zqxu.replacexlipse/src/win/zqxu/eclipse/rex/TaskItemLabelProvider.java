package win.zqxu.eclipse.rex;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;

public class TaskItemLabelProvider extends StyledCellLabelProvider {
  private TextStyle headerStyle = new TextStyle();
  private TextStyle replaceStyle = new TextStyle();

  public TaskItemLabelProvider(TableViewer owner) {
    Font font = owner.getTable().getFont();
    String fontName = font.getFontData()[0].getName();
    int fontHeight = font.getFontData()[0].getHeight();
    Font headerFont = new Font(font.getDevice(), fontName, fontHeight, SWT.BOLD);
    headerStyle.font = headerFont;
    replaceStyle.font = headerFont;
    replaceStyle.background = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
  }

  @Override
  public void update(ViewerCell cell) {
    TaskItem taskItem = (TaskItem) cell.getElement();
    List<StyleRange> ranges = new ArrayList<StyleRange>();
    StringBuilder cellText = new StringBuilder();
    String search = taskItem.getSearch();
    boolean first = taskItem.isFirst();
    CharCase toCase = taskItem.getToCase();
    String replace = taskItem.getReplace();

    String text;
    if (replace.isEmpty() && toCase == null)
      text = Messages.RENDER_REMOVE;
    else
      text = Messages.RENDER_REPLACE;
    if (first) text += Messages.RENDER_FIRST;
    addCellText(cellText, text, ranges, headerStyle);
    addCellText(cellText, search, ranges, replaceStyle);
    if (toCase != null) {
      if (toCase == CharCase.UPPER)
        text = Messages.RENDER_TO_UPPER;
      else
        text = Messages.RENDER_TO_LOWER;
      addCellText(cellText, text, ranges, headerStyle);
    } else if (!replace.isEmpty()) {
      addCellText(cellText, Messages.RENDER_WITH, ranges, headerStyle);
      addCellText(cellText, replace, ranges, replaceStyle);
    }
    cellText.append("\n").append(taskItem.getDesc()); //$NON-NLS-1$
    cell.setText(cellText.toString());
    cell.setStyleRanges(ranges.toArray(new StyleRange[0]));
  }

  private void addCellText(StringBuilder cellText, String text,
      List<StyleRange> ranges, TextStyle textStyle) {
    int start = cellText.length();
    cellText.append(text);
    ranges.add(new StyleRange(textStyle));
    int index = ranges.size() - 1;
    ranges.get(index).start = start;
    ranges.get(index).length = text.length();
  }
}
