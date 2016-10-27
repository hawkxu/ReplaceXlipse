package win.zqxu.eclipse.rex;

import java.beans.Introspector;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TaskStore implements Constants {
  private static File getStoreFile() {
    Bundle bundle = Platform.getBundle(symbolicName);
    return new File(Platform.getStateLocation(bundle).toFile(), "tasks.xml");
  }

  private static Document loadStore() throws Exception {
    File store = getStoreFile();
    if (!store.exists()) return null;
    return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(store);
  }

  public static List<TaskGroup> readGroupList() throws Exception {
    List<TaskGroup> groupList = new ArrayList<TaskGroup>();
    Document document = loadStore();
    if (document != null) {
      Unmarshaller unmarshaller = JAXBContext.newInstance(TaskGroup.class).createUnmarshaller();
      NodeList nodeList = getGroupNodeList(document);
      for (int i = 0; i < nodeList.getLength(); i++)
        groupList.add((TaskGroup) unmarshaller.unmarshal(nodeList.item(i)));
    }
    return groupList;
  }

  public static List<TaskItem> readItemList(int groupIndex) throws Exception {
    Node groupNode = getGroupNodeList(loadStore()).item(groupIndex);
    NodeList nodeList = getItemNodeList((Element) groupNode);
    List<TaskItem> itemList = new ArrayList<TaskItem>();
    Unmarshaller unmarshaller = JAXBContext.newInstance(TaskItem.class).createUnmarshaller();
    for (int i = 0; i < nodeList.getLength(); i++) {
      itemList.add((TaskItem) unmarshaller.unmarshal(nodeList.item(i)));
    }
    return itemList;
  }

  private static Document ensureStore() throws Exception {
    Document document = loadStore();
    if (document == null) {
      document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      document.appendChild(document.createElement("root"));
    }
    return document;
  }

  public static void saveTaskGroup(int groupIndex, TaskGroup taskGroup) throws Exception {
    Document document = ensureStore();
    Element rootNode = document.getDocumentElement();
    if (groupIndex != -1)
      updateChildNode(rootNode, groupIndex, taskGroup);
    else
      rootNode.appendChild(createObjectNode(document, taskGroup));
    saveStore(document);
  }

  public static void removeTaskGroup(int groupIndex) throws Exception {
    Document document = ensureStore();
    removeChildNode(getGroupNodeList(document), groupIndex);
    saveStore(document);
  }

  public static void moveTaskGroup(int fromIndex, int toIndex) throws Exception {
    Document document = ensureStore();
    moveChild(getGroupNodeList(document), fromIndex, toIndex);
    saveStore(document);
  }

  public static void saveTaskItem(int groupIndex, int itemIndex,
      TaskItem taskItem) throws Exception {
    Document document = ensureStore();
    Node groupNode = getGroupNodeList(document).item(groupIndex);
    if (itemIndex != -1)
      updateChildNode((Element) groupNode, itemIndex, taskItem);
    else
      groupNode.appendChild(createObjectNode(document, taskItem));
    saveStore(document);
  }

  public static void removeTaskItem(int groupIndex, int itemIndex) throws Exception {
    Document document = ensureStore();
    Node groupNode = getGroupNodeList(document).item(groupIndex);
    removeChildNode(getItemNodeList((Element) groupNode), itemIndex);
    saveStore(document);
  }

  public static void moveTaskItem(int groupIndex, int fromIndex, int toIndex) throws Exception {
    Document document = ensureStore();
    Node groupNode = getGroupNodeList(document).item(groupIndex);
    moveChild(getItemNodeList((Element) groupNode), fromIndex, toIndex);
    saveStore(document);
  }

  private static NodeList getGroupNodeList(Document document) {
    String simpleClassName = TaskGroup.class.getSimpleName();
    return document.getElementsByTagName(Introspector.decapitalize(simpleClassName));
  }

  private static NodeList getItemNodeList(Element groupNode) {
    String simpleClassName = TaskItem.class.getSimpleName();
    return groupNode.getElementsByTagName(Introspector.decapitalize(simpleClassName));
  }

  private static Node createObjectNode(Document document, Object object) throws Exception {
    Element temp = document.createElement("temp");
    Marshaller marshaller = JAXBContext.newInstance(object.getClass()).createMarshaller();
    marshaller.marshal(object, temp);
    return temp.getFirstChild();
  }

  private static void updateChildNode(Element parentNode, int childIndex, Object object)
      throws Exception {
    String tagName = Introspector.decapitalize(object.getClass().getSimpleName());
    Node childNode = parentNode.getElementsByTagName(tagName).item(childIndex);
    parentNode.replaceChild(createObjectNode(parentNode.getOwnerDocument(), object), childNode);
  }

  private static void removeChildNode(NodeList children, int childIndex) {
    Node childNode = children.item(childIndex);
    childNode.getParentNode().removeChild(childNode);
  }

  private static void moveChild(NodeList nodeList, int fromIndex, int toIndex) {
    int smallIndex = Math.min(fromIndex, toIndex);
    int largeIndex = Math.max(fromIndex, toIndex);
    Node smallNode = nodeList.item(smallIndex);
    Node largeNode = nodeList.item(largeIndex);
    Node referNode = largeNode.getNextSibling();
    Element parentNode = (Element) largeNode.getParentNode();
    parentNode.removeChild(largeNode);
    parentNode.insertBefore(largeNode, smallNode);
    parentNode.insertBefore(smallNode, referNode);
  }

  private static void saveStore(Document document) throws Exception {
    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.transform(new DOMSource(document), new StreamResult(getStoreFile()));
  }
}
