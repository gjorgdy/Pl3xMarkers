package nl.gjorgdy.pl3xmarkers.core.helpers;

import org.intellij.lang.annotations.Language;
import org.owasp.html.PolicyFactory;

public class HtmlHelper {

	private static final PolicyFactory POLICY = SanitizerHelper.createColorHeadersLinksPolicy();

    private HtmlHelper() {}

    @Language("HTML")
    public static String TravelPopUp(String title, String destinationKey, int relativeX, int relativeZ, String buttonText) {
        @Language("HTML") String html = """
            <div style='display: flex; flex-direction: column; gap: 0.5rem'>
                <b>%s</b>
                <button onclick="(function() {
                  const baseUrl = window.location.href.split('?')[0] || window.location.href;
                  const params = new URLSearchParams(window.location.search);
                  params.set('world', '%s');
                  params.set('x', '%d');
                  params.set('z', '%d');
                  window.location.href = baseUrl + '?' + params.toString();
                })()">
                    %s
                </button>
            </div>
        """;

        return String.format(html, title, destinationKey, relativeX, relativeZ, buttonText);
    }

	@Language("HTML")
	public static String scrollablePopUp(@Language("HTML") String title, @Language("HTML") String subTitle, @Language("HTML") String content) {
		return "<b>" + HtmlHelper.sanitize(title) + "</b><i style='color: gray; margin-left: 0.5rem'>" + HtmlHelper.sanitize(subTitle) + "</i><br>"
					   + "<div style='max-height: 16rem; max-width: 32rem; overflow-y: auto; border: #f0f0f0 solid 2px; padding: 5px; border-radius: 1rem;'>"
					   + content
					   + "</div>";
	}

	@Language("HTML")
	public static String tooltip(@Language("HTML") String title, @Language("HTML") String subTitle, @Language("HTML") String label) {
		return "<b>" + HtmlHelper.sanitize(title) + "</b>"
					   + "<i style='color: gray; margin-left: 0.5rem'>" + HtmlHelper.sanitize(subTitle) + "</i>"
					   + "<br><i>" + HtmlHelper.sanitize(label) + "</i>";

	}

	@Language("HTML")
	public static String sanitize(@Language("HTML") String html) {
		return POLICY.sanitize(html);
	}

}
