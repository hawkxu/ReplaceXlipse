package win.zqxu.eclipse.rex;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TaskItem {
  private String search;
  private boolean matchCase;
  private boolean matchWord;
  private boolean regex;
  private boolean first;
  private CharCase toCase;
  private String replace;
  private String desc;

  public String getSearch() {
    return search == null ? "" : search;
  }

  public void setSearch(String search) {
    this.search = search;
  }

  public boolean isMatchCase() {
    return matchCase;
  }

  public void setMatchCase(boolean matchCase) {
    this.matchCase = matchCase;
  }

  public boolean isMatchWord() {
    return matchWord;
  }

  public void setMatchWord(boolean matchWord) {
    this.matchWord = matchWord;
  }

  public boolean isRegex() {
    return regex;
  }

  public void setRegex(boolean regex) {
    this.regex = regex;
  }

  public boolean isFirst() {
    return first;
  }

  public void setFirst(boolean first) {
    this.first = first;
  }

  public CharCase getToCase() {
    return toCase;
  }

  public void setToCase(CharCase toCase) {
    this.toCase = toCase;
  }

  public String getReplace() {
    return replace == null ? "" : replace;
  }

  public void setReplace(String replace) {
    this.replace = replace;
  }

  public String getDesc() {
    return desc == null ? "" : desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public boolean fullEquals(TaskItem another) {
    return another != null && getSearch().equals(another.getSearch())
        && isMatchCase() == another.isMatchCase() && isMatchWord() == another.isMatchWord()
        && isRegex() == another.isRegex() && isFirst() == another.isFirst()
        && getToCase() == another.getToCase() && getReplace().equals(another.getReplace())
        && getDesc().equals(another.getDesc());
  }
}
