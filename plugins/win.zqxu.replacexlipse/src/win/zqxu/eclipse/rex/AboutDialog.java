package win.zqxu.eclipse.rex;

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.ResourceManager;
import org.osgi.framework.Version;

public class AboutDialog extends Dialog implements Constants {
  private static final String LINK_PATTERN = "%s<a href=\"%s\">%s</a>"; //$NON-NLS-1$

  /**
   * Create the dialog.
   * 
   * @param parentShell
   */
  public AboutDialog(Shell parentShell) {
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

    Label labTitle = new Label(container, SWT.NONE);
    labTitle.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
    labTitle.setText(Messages.ABOUT_DESC);

    Label label2 = new Label(container, SWT.NONE);
    label2.setText(Messages.ABOUT_LABEL_VERSION);

    Label labVersion = new Label(container, SWT.NONE);
    Version v = Platform.getBundle(symbolicName).getVersion();
    labVersion.setText(
        String.format("%d.%d.%d %s", v.getMajor(), v.getMinor(), v.getMicro(), v.getQualifier())); //$NON-NLS-1$

    Label label3 = new Label(container, SWT.NONE);
    label3.setText(Messages.ABOUT_LABEL_AUTHOR);

    Label labAuthor = new Label(container, SWT.NONE);
    labAuthor.setText(Messages.ABOUT_AUTHOR);

    Label lblLicense = new Label(container, SWT.NONE);
    lblLicense.setText(Messages.ABOUT_LABEL_LICENSE);

    Link link_1 = new Link(container, SWT.NONE);
    link_1.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent event) {
        openLinkWebSite(event);
      }
    });
    link_1.setText(String.format(LINK_PATTERN, "", //$NON-NLS-1$
        "http://www.eclipse.org/org/documents/epl-v10.php", Messages.ABOUT_ECLIPSE_LICENSE)); //$NON-NLS-1$
    new Label(container, SWT.NONE);

    Link link = new Link(container, SWT.NONE);
    link.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent event) {
        openLinkWebSite(event);
      }
    });
    link.setText(String.format(LINK_PATTERN, Messages.ABOUT_ICONS_FROM,
        "http://www.famfamfam.com/lab/icons/silk/", Messages.ABOUT_SILK_ICONS)); //$NON-NLS-1$
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);

    Label labLove = new Label(container, SWT.NONE);
    labLove.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
    labLove.setText(Messages.ABOUT_LOVE_BABY);

    getShell().setText(Messages.ABOUT_DIALOG_TITLE);
    getShell().setImage(ResourceManager.getPluginImage(symbolicName, "icons/help.png")); //$NON-NLS-1$
    return container;
  }

  private void openLinkWebSite(SelectionEvent event) {
    try {
      String text = ((Link) event.widget).getText();
      PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser()
          .openURL(new URL(text.replaceAll("^.*?\"|\".*", ""))); //$NON-NLS-1$ //$NON-NLS-2$
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Create contents of the button bar.
   * 
   * @param parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    Button btnOK = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    btnOK.setImage(ResourceManager.getPluginImage(symbolicName, "icons/accept.png")); //$NON-NLS-1$
  }

  /**
   * Return the initial size of the dialog.
   */
  @Override
  protected Point getInitialSize() {
    return new Point(350, 290);
  }
}
