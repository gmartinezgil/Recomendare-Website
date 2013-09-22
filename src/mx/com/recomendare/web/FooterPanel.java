/**
 * 
 */
package mx.com.recomendare.web;

import mx.com.recomendare.util.Constants;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author jerry
 *
 */
public final class FooterPanel extends Panel {
    private static final long serialVersionUID = 1L;

    /**
     * @param id
     */
    public FooterPanel(String id) {
        super(id);
        init();
    }

    private void init() {
        //COPYRIGHT
        add(new Label("copyright", Constants.COPYRIGHT));
    }
    
}//END OF FILE