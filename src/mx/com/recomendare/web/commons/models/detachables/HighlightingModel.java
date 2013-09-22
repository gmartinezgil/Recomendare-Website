/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.util.convert.IConverter;

/**
 * @author jerry
 *
 */
public final class HighlightingModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;

	private transient IModel text;
	private transient IModel searchWord;
	private transient Pattern lastp;
	private transient String lastSearch;

	public HighlightingModel(IModel searchResultText, IModel searchKeyWordText) {
		this.text = searchResultText;
		this.searchWord = searchKeyWordText;
	}

	protected String load() {
		String srch = (String)searchWord.getObject();
		Object object = text.getObject();
		String txt = "";
		if (object != null) {
			IConverter converter =
				Application.get().getConverterLocator().getConverter(object.getClass());
			txt = converter.convertToString(object,
					Session.get().getLocale());
		}
		if (srch != null) {
			Pattern p = null;
			if (lastp != null && srch.equals(lastSearch) )
				p = lastp;
			else {
				try {
					p = Pattern.compile(srch, Pattern.CASE_INSENSITIVE);
				} catch (PatternSyntaxException e) {
					if (e.getIndex()>-1) {
						srch = srch.substring(0, e.getIndex());
						try {
							p = Pattern.compile(srch,
									Pattern.CASE_INSENSITIVE);
						} catch (Exception e2) {
							srch = null;
						}
					} else
						srch = null;
				}
			}
			if (p != null) {
				Matcher m = p.matcher(txt);
				return m.replaceFirst("<span style=\"font-weight: bold; font-style: italic; color: #FFB700\">$0</span>");
			}
		}
		return txt;
	}

	/*
	protected String load() {
        return ((String)text.getObject()).replaceAll((String)searchWord.getObject(),"<span style=\"font-weight: bold\">" + searchWord.getObject() + "</span>");
    } 
    */

	protected void onDetach() {
		super.onDetach();
		text = null;
		searchWord = null;
	}
	
}//END OF FILE