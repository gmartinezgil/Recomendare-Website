/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import java.io.File;
import java.io.IOException;

import mx.com.recomendare.util.Util;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.markup.html.image.resource.ThumbnailImageResource;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author jerry
 *
 */
public final class UploadedThumbnailPicturePanel extends Panel {
    private static final long serialVersionUID = 1L;
    
    //the uploaded image to show...
    private NonCachingImage uploadedImage;
    
    //the log...
    private static final Log log = LogFactory.getLog(UploadedThumbnailPicturePanel.class);

    /**
     * @param id
     */
    public UploadedThumbnailPicturePanel(String id) {
        super(id);
        init();
    }

    private void init() {
        //IMAGE
        uploadedImage = new NonCachingImage("pictureImage", WebUtil.QUESTIONMARK_IMAGE);
        add(uploadedImage);
    }

    public void updatePicture(String pathToImageFile) {
        final File f = new File(pathToImageFile);
        if (f.exists() && f.canRead()) {
            log.info("File founded in - " + f.getAbsolutePath());
            final DynamicImageResource imageResource = new DynamicImageResource() {
                private static final long serialVersionUID = 1L;
                protected byte[] getImageData() {
                    byte[] imageBytes = null;
                    try {
                        log.info("Reading file...");
                        imageBytes = Util.getContentAsBytesFromFile(f);
                        log.info("done..." + imageBytes);
                    } catch (IOException e) {
                        log.error("Can't read image file, reason - " + e.getMessage(), e);
                    }
                    return imageBytes;
                }
            };
            final ThumbnailImageResource thumbnailImageResource = new ThumbnailImageResource(imageResource, 100);
            uploadedImage.setImageResource(thumbnailImageResource);
        }
    }

}//END OF FILE
