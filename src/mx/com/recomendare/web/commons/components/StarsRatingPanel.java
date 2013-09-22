/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import org.apache.wicket.Resource;
import org.apache.wicket.markup.html.PackageResource;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author jerry
 *
 */
public final class StarsRatingPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	//the stars to show...
	private int numberOfStars;
	//the style of them...
	private int style;
	
	//the stars...
	private static final Resource STAR_ON_NORMAL_IMAGE = PackageResource.get(StarsRatingPanel.class, "star1.gif").setCacheable(true);
	private static final Resource STAR_OFF_NORMAL_IMAGE = PackageResource.get(StarsRatingPanel.class, "star0.gif").setCacheable(true);
	private static final Resource STAR_ON_ICON_IMAGE = PackageResource.get(StarsRatingPanel.class, "star1.png").setCacheable(true);
	private static final Resource STAR_OFF_ICON_IMAGE = PackageResource.get(StarsRatingPanel.class, "star0.png").setCacheable(true);
	
	//the minimal size... 
	public static final int ICON_STYLE = 0;
	//the same as the rating panel...
	public static final int NORMAL_STYLE = 1;

	/**
	 * @param id
	 */
	public StarsRatingPanel(String id, int numberOfStars, int style) {
		super(id);
		this.numberOfStars = numberOfStars;
		this.style = style;
		init();
	}
	
	private void init()  {
		//draw the corresponding stars...
		switch (numberOfStars) {
			case 0:
						if(style == ICON_STYLE)  {
							add(new Image("firstStar", STAR_OFF_ICON_IMAGE));
							add(new Image("secondStar", STAR_OFF_ICON_IMAGE));
							add(new Image("thirdStar", STAR_OFF_ICON_IMAGE));
							add(new Image("fourthStar", STAR_OFF_ICON_IMAGE));
							add(new Image("fiveStar", STAR_OFF_ICON_IMAGE));
						}
						else if (style == NORMAL_STYLE)  {
							add(new Image("firstStar", STAR_OFF_NORMAL_IMAGE));
							add(new Image("secondStar", STAR_OFF_NORMAL_IMAGE));
							add(new Image("thirdStar", STAR_OFF_NORMAL_IMAGE));
							add(new Image("fourthStar", STAR_OFF_NORMAL_IMAGE));
							add(new Image("fiveStar", STAR_OFF_NORMAL_IMAGE));
						}
						break;
			case 1:
						if(style == ICON_STYLE)  {
							add(new Image("firstStar", STAR_ON_ICON_IMAGE));
							add(new Image("secondStar", STAR_OFF_ICON_IMAGE));
							add(new Image("thirdStar", STAR_OFF_ICON_IMAGE));
							add(new Image("fourthStar", STAR_OFF_ICON_IMAGE));
							add(new Image("fiveStar", STAR_OFF_ICON_IMAGE));
						}
						else if (style == NORMAL_STYLE)  {
							add(new Image("firstStar", STAR_ON_NORMAL_IMAGE));
							add(new Image("secondStar", STAR_OFF_NORMAL_IMAGE));
							add(new Image("thirdStar", STAR_OFF_NORMAL_IMAGE));
							add(new Image("fourthStar", STAR_OFF_NORMAL_IMAGE));
							add(new Image("fiveStar", STAR_OFF_NORMAL_IMAGE));	
						}
						break;
			case 2:
						if(style == ICON_STYLE)  {
							add(new Image("firstStar", STAR_ON_ICON_IMAGE));
							add(new Image("secondStar", STAR_ON_ICON_IMAGE));
							add(new Image("thirdStar", STAR_OFF_ICON_IMAGE));
							add(new Image("fourthStar", STAR_OFF_ICON_IMAGE));
							add(new Image("fiveStar", STAR_OFF_ICON_IMAGE));							
						}
						else if (style == NORMAL_STYLE)  {
							add(new Image("firstStar", STAR_ON_NORMAL_IMAGE));
							add(new Image("secondStar", STAR_ON_NORMAL_IMAGE));
							add(new Image("thirdStar", STAR_OFF_NORMAL_IMAGE));
							add(new Image("fourthStar", STAR_OFF_NORMAL_IMAGE));
							add(new Image("fiveStar", STAR_OFF_NORMAL_IMAGE));	
						}
						break;
			case 3:
						if(style == ICON_STYLE)  {
							add(new Image("firstStar", STAR_ON_ICON_IMAGE));
							add(new Image("secondStar", STAR_ON_ICON_IMAGE));
							add(new Image("thirdStar", STAR_ON_ICON_IMAGE));
							add(new Image("fourthStar", STAR_OFF_ICON_IMAGE));
							add(new Image("fiveStar", STAR_OFF_ICON_IMAGE));							
						}
						else if (style == NORMAL_STYLE)  {
							add(new Image("firstStar", STAR_ON_NORMAL_IMAGE));
							add(new Image("secondStar", STAR_ON_NORMAL_IMAGE));
							add(new Image("thirdStar", STAR_ON_NORMAL_IMAGE));
							add(new Image("fourthStar", STAR_OFF_NORMAL_IMAGE));
							add(new Image("fiveStar", STAR_OFF_NORMAL_IMAGE));
						}
						break;
			case 4:
						if(style == ICON_STYLE)  {
							add(new Image("firstStar", STAR_ON_ICON_IMAGE));
							add(new Image("secondStar", STAR_ON_ICON_IMAGE));
							add(new Image("thirdStar", STAR_ON_ICON_IMAGE));
							add(new Image("fourthStar", STAR_ON_ICON_IMAGE));
							add(new Image("fiveStar", STAR_OFF_ICON_IMAGE));
						}
						else if (style == NORMAL_STYLE)  {
							add(new Image("firstStar", STAR_ON_NORMAL_IMAGE));
							add(new Image("secondStar", STAR_ON_NORMAL_IMAGE));
							add(new Image("thirdStar", STAR_ON_NORMAL_IMAGE));
							add(new Image("fourthStar", STAR_ON_NORMAL_IMAGE));
							add(new Image("fiveStar", STAR_OFF_NORMAL_IMAGE));							
						}
						break;
			case 5:
						if(style == ICON_STYLE)  {
							add(new Image("firstStar", STAR_ON_ICON_IMAGE));
							add(new Image("secondStar", STAR_ON_ICON_IMAGE));
							add(new Image("thirdStar", STAR_ON_ICON_IMAGE));
							add(new Image("fourthStar", STAR_ON_ICON_IMAGE));
							add(new Image("fiveStar", STAR_ON_ICON_IMAGE));
						}
						else if (style == NORMAL_STYLE)  {
							add(new Image("firstStar", STAR_ON_NORMAL_IMAGE));
							add(new Image("secondStar", STAR_ON_NORMAL_IMAGE));
							add(new Image("thirdStar", STAR_ON_NORMAL_IMAGE));
							add(new Image("fourthStar", STAR_ON_NORMAL_IMAGE));
							add(new Image("fiveStar", STAR_ON_NORMAL_IMAGE));							
						}
						break;
			default:
				break;
		}
	}

}//END OF FILE