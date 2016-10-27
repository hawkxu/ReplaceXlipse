package win.zqxu.eclipse.rex;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TaskGroup {
  private String name;
  private String desc;

  public String getName() {
    return name == null ? "" : name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDesc() {
    return desc == null ? "" : desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public boolean fullEqulas(TaskGroup another) {
    return another != null && getName().equals(another.getName())
        && getDesc().equals(another.getDesc());
  }
}
