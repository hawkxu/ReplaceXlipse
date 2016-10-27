package win.zqxu.eclipse.rex;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

public class TaskItemTransfer extends ByteArrayTransfer {
  private static final String ITEM_TYPE_NAME = "TaskItem";
  private static final int ITEM_TYPE_ID = registerType(ITEM_TYPE_NAME);
  private static final TaskItemTransfer _instance = new TaskItemTransfer();

  public static TaskItemTransfer getInstance() {
    return _instance;
  }

  private TaskItemTransfer() {
  }

  @Override
  protected int[] getTypeIds() {
    return new int[]{ITEM_TYPE_ID};
  }

  @Override
  protected String[] getTypeNames() {
    return new String[]{ITEM_TYPE_NAME};
  }

  @Override
  protected void javaToNative(Object object, TransferData transferData) {
    super.javaToNative(String.valueOf(object).getBytes(), transferData);
  }

  @Override
  protected Object nativeToJava(TransferData transferData) {
    return Integer.valueOf(new String((byte[]) super.nativeToJava(transferData)));
  }
}
