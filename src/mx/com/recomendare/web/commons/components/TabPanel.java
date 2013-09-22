/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import java.util.List;

import org.apache.wicket.Resource;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author jerry
 *
 */
public final class TabPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	//the display modes...
	public static int FULL_DISPLAY_MODE = 0;
	public static int MINI_DISPLAY_MODE = 1;

	//the panel properly...
	private TabbedPanel tabbedPanel;
	
	//the tabs...
	private List<ITab> tabs;

	public TabPanel(final String id, final List<ITab> tabs, final int mode) {
		super(id);
		this.tabs = tabs;
		if(MINI_DISPLAY_MODE == mode) 	add(HeaderContributor.forCss(TabPanel.class, "TabPanelMini.css"));
		else add(HeaderContributor.forCss(TabPanel.class, "TabPanel.css"));
		init();
	}
	
	private void init()  {
		tabbedPanel = new AjaxTabbedPanel("tabpanel", tabs) {
			private static final long serialVersionUID = 1L;
			protected WebMarkupContainer newLink(final String linkId, final int index) {
				ITab currentTab = tabs.get(index);
				if (currentTab instanceof ImageTab) {
					ImageTab imageTab = (ImageTab)currentTab;
					return new ImageTabLink(linkId, imageTab.getImage()) {
						private static final long serialVersionUID = 1L;
						public void onClick(AjaxRequestTarget target) {
							setSelectedTab(index);
							if (target != null) {
								target.addComponent(TabPanel.this);
							}
							onAjaxUpdate(target);
						}
					};
				}
				else {
					final WebMarkupContainer link = new AjaxFallbackLink(linkId) {
						private static final long serialVersionUID = 1L;
						public void onClick(AjaxRequestTarget target) {
							setSelectedTab(index);
							if (target != null) {
								target.addComponent(TabPanel.this);
							}
							onAjaxUpdate(target);
						}
					};
					link.add(new WebMarkupContainer("image").setVisible(false));
					return link;
				}
			}
		};
		tabbedPanel.setOutputMarkupId(true);
		TabPanel.this.setOutputMarkupId(true);
		add(tabbedPanel);
	}

	/**
	 * 
	 *Tab
	 */
	public static class Tab extends AbstractTab {
		private static final long serialVersionUID = 1L;

		private Panel p;

		public Tab(final String label) {
			super(new Model(label));
		}
		
		public Tab(final IModel modelLabel) {
			super(modelLabel);
		}

		public Panel getPanel(String panelId) {
			if (p == null) {
				// Lazily create the panel
				p = createPanel();
				if (!TabbedPanel.TAB_PANEL_ID.equals(p.getId())) {
					throw new IllegalArgumentException(	"Panel id must be TabbedPanel.TAB_PANEL_ID");
				}
				p.setOutputMarkupId(true);
			}
			return p;
		}
		
		protected Panel createPanel() {
			throw new IllegalArgumentException("Must provide a panel");
		}
	}
	
	//now with images...
	/**
	 * AbstractImageTab
	 */
	public static abstract class ImageTab extends Tab {
		private static final long serialVersionUID = 1L;
		private Resource image;

		public ImageTab(String title, Resource image) {
			super(title);
			this.image = image;
		}
		
		public ImageTab(IModel titleModel, Resource image) {
			super(titleModel);
			this.image = image;
		}

		public Resource getImage() 	{
			return image;
		}
	}
	
	/**
	 * ImageTabLink
	 */
	abstract class ImageTabLink extends AjaxFallbackLink {
		private static final long serialVersionUID = 1L;

		public ImageTabLink(String id, Resource image) {
			super(id);
			add(new Image("image", image));
		}
	}

}//END OF FILE