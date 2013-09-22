package mx.com.recomendare.web.commons.components;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;

/**
 * 
 * @author jerry
 *
 */
public class TagCloud extends Panel {

    private static final long serialVersionUID = 1L;
    private static final ResourceReference TAGCLOUD_CSS = new ResourceReference(TagCloud.class, "TagCloud.css");

    public TagCloud(String id, final IModel model) {
        super(id, model);
        init();
    }

    @SuppressWarnings("unchecked")
	private void init() {
        add(HeaderContributor.forCss(TAGCLOUD_CSS));
        RepeatingView rp = new RepeatingView("tags") {
            private static final long serialVersionUID = 1L;
            public boolean isVisible() {
                return size() > 0;
            }
        };
       
        Iterator<Tag> iterator = ((List<Tag>)getModelObject()).iterator();
        while (iterator.hasNext()) {
            Tag tag = iterator.next();
            rp.add(new TagPanel(rp.newChildId(), tag));
        }
        add(rp);
    }

    /**
     * Tag
     */
    public static abstract class Tag implements Serializable {
		private static final long serialVersionUID = 1L;
		private String tag;
        private int weight;

        public Tag(final String tag, final int weight) {
            this.tag = tag;
            this.weight = weight;
        }

        public int getWeight() {
            return weight;
        }

        public String getTag() {
            return tag;
        }

        public abstract Link getLink(String id);
    }

    /**
     * TagPanel
     */
    public final class TagPanel extends Panel {
        private static final long serialVersionUID = 1L;

        public TagPanel(final String id, final Tag tag) {
            super(id);
            Link link = tag.getLink("link");
            Label labelTag = new Label("tag", tag.getTag());
            link.add(labelTag);
            int sanitizedWeight = tag.getWeight();
            if (sanitizedWeight > 5) {
                sanitizedWeight = 5;
            } else if (sanitizedWeight < 1) {
                sanitizedWeight = 1;
            }
            labelTag.add(new SimpleAttributeModifier("class", "tag-weight-" + sanitizedWeight));
            add(link);
        }
    }
    
}//END OF FILE