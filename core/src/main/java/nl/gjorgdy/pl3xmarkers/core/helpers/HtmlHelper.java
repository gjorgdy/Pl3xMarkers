package nl.gjorgdy.pl3xmarkers.core.helpers;

import org.intellij.lang.annotations.Language;

public class HtmlHelper {

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

}
