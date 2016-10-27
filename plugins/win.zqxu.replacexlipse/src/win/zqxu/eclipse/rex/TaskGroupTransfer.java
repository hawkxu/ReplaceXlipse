package win.zqxu.eclipse.rex;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

public class TaskGroupTransfer extends ByteArrayTransfer {
  private static final String GROUP_TYPE_NAME = "TaskGroup";
  private static final int GROUP_TYPE_ID = registerType(GROUP_TYPE_NAME);
  private static final TaskGroupTransfer _instance = new TaskGroupTransfer();

  public static TaskGroupTransfer getInstance() {
    return _instance;
  }

  private TaskGroupTransfer() {
  }

  @Override
  protected int[] getTypeIds() {
    return new int[]{GROUP_TYPE_ID};
  }

  @Override
  protected String[] getTypeNames() {
    return new String[]{GROUP_TYPE_NAME};
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
