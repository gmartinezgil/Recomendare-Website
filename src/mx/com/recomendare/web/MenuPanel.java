/**
 * 
 */
package mx.com.recomendare.web;

import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.items.AddNewItemWithWizardPage;
import mx.com.recomendare.web.items.ViewItemsAroundMePage;
import mx.com.recomendare.web.session.SignInPage;
import mx.com.recomendare.web.users.AddNewUserWithWizardPage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author jerry
 *
 */
public final class MenuPanel extends Panel {
    private static final long serialVersionUID = 1L;

    /**
     * @param id
     */
    public MenuPanel(String id) {
        super(id);
        init();
    }

    private void init() {
        //add(new Label("version", Constants.VERSION));
        //HOME
    	/*
    	final Link homeLink = new Link("homeLink") {
            private static final long serialVersionUID = 1L;
            public void onClick() {
                setResponsePage(Index.class);
            }
        };
        */
    	final BookmarkablePageLink homeLink = new BookmarkablePageLink("homeLink", Index.class);
        homeLink.add(new Label("homeLinkLabel", new LabelStringDetachableModel("HOME")));
        add(homeLink);
        /*
        //SEARCH
        add(new Link("searchLink") {
        private static final long serialVersionUID = 1L;
        public void onClick() {
        setResponsePage(SearchPage.class);
        }
        });
         */
        //PLACE
        final Link addLink = new Link("addNewPlaceWizardLink") {
            private static final long serialVersionUID = 1L;
            public void onClick() {
                setResponsePage(AddNewItemWithWizardPage.class);
            }
        }; 
        addLink.add(new Label("addNewPlaceWizardLinkLabel", new LabelStringDetachableModel("RECOMMEND")));
        add(addLink);
        //AROUND ME
        final Link aroundMeLink = new Link("aroundMeLink") {
            private static final long serialVersionUID = 1L;
            public void onClick() {
                setResponsePage(ViewItemsAroundMePage.class);
            }
        }; 
        aroundMeLink.add(new Label("aroundMeLinkLabel", new LabelStringDetachableModel("WHAT_IS_AROUND_ME")));
        add(aroundMeLink);
        //LOGIN
        final Link loginLink = new Link("loginLink") {
            private static final long serialVersionUID = 1L;
            public void onClick() {
                setResponsePage(SignInPage.class);
            }
        }; 
        loginLink.add(new Label("loginLinkLabel", new LabelStringDetachableModel("LOGIN")));
        add(loginLink);
        /*
        //ITEM
        add(new Link("addNewItemWizardLink"){
        private static final long serialVersionUID = 1L;
        public void onClick() {
        setResponsePage(AddNewItemPage.class);
        }
        });
        //TEST...
        add(new Link("monitorLink"){
        private static final long serialVersionUID = 1L;
        public void onClick() {
        setResponsePage(MemoryStatusChartPage.class);
        }
        });
         */
        //REGISTER
        final Link registerLink = new Link("registerLink"){
			private static final long serialVersionUID = 1L;
			public void onClick() {
				setResponsePage(AddNewUserWithWizardPage.class);
			}
        };
        registerLink.add(new Label("registerLinkLabel", new LabelStringDetachableModel("ADD_USER")));
        add(registerLink);
    }
    
}//END OF FILE