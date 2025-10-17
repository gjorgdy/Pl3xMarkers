package nl.gjorgdy.pl3xmarkers.core.helpers;

import org.owasp.html.CssSchema;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

import java.util.Set;

public class SanitizerHelper {

	public static PolicyFactory createColorHeadersLinksPolicy() {
		CssSchema colorSchema = CssSchema.withProperties(Set.of("color"));

		return new HtmlPolicyBuilder()
		   .allowElements("h1", "h2", "h3", "h4", "h5", "h6")
		   .allowElements("a", "b", "i", "strong", "u")
		   .allowAttributes("href").onElements("a")
		   .allowUrlProtocols("https")
		   .allowStyling(colorSchema)
		   .toFactory();
	}

}