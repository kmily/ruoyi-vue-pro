package cn.iocoder.yudao.framework.web.core.util;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.springframework.util.StringUtils;

@Slf4j
public class XssUtils extends Safelist {

    /**
     * XSS过滤
     */
    public static String filter(String html){
        return Jsoup.clean(html, xssWhitelist());
    }

    /**
     * 允许base64的图片通过等
     */
    @Override
    protected boolean isSafeAttribute(String tagName, Element el, Attribute attr) {
        //不允许 javascript 开头的 src 和 href
        if ("src".equalsIgnoreCase(attr.getKey()) || "href".equalsIgnoreCase(attr.getKey())) {
            String value = attr.getValue();
            if (StringUtils.hasText(value) && value.toLowerCase().startsWith("javascript")) {
                return false;
            }
        }
        //允许 base64 的图片内容
        if ("img".equals(tagName) && "src".equals(attr.getKey()) && attr.getValue().startsWith("data:;base64")) {
            return true;
        }
        return super.isSafeAttribute(tagName, el, attr);
    }

    /**
     * XSS过滤白名单
     */
    public static Safelist xssWhitelist(){
        return new Safelist()
                .addTags("a", "b", "blockquote", "br", "caption", "cite", "code", "col", "colgroup", "dd", "div", "span", "embed", "object", "dl", "dt",
                        "em", "h1", "h2", "h3", "h4", "h5", "h6", "i", "img", "li", "ol", "p", "pre", "q", "small",
                        "strike", "strong", "sub", "sup", "table", "tbody", "td", "tfoot", "th", "thead", "tr", "u", "ul")
                .addAttributes("a", "href", "class", "style", "target", "rel", "nofollow")
                .addAttributes("blockquote", "cite")
                .addAttributes("code", "class", "style")
                .addAttributes("col", "span", "width")
                .addAttributes("colgroup", "span", "width")
                .addAttributes("img", "align", "alt", "height", "src", "title", "width", "class", "style")
                .addAttributes("ol", "start", "type")
                .addAttributes("q", "cite")
                .addAttributes("table", "summary", "width", "class", "style")
                .addAttributes("tr", "abbr", "axis", "colspan", "rowspan", "width", "style")
                .addAttributes("td", "abbr", "axis", "colspan", "rowspan", "width")
                .addAttributes("th", "abbr", "axis", "colspan", "rowspan", "scope", "width")
                .addAttributes("ul", "type", "style")
                .addAttributes("pre", "class", "style")
                .addAttributes("div", "class", "id", "style")
                .addAttributes("video", "src", "autoplay", "controls", "loop", "muted", "poster", "preload")
                .addAttributes("object", "type", "id", "name", "data", "width", "height", "style", "classid", "codebase")
                .addAttributes("param", "name", "value")
                .addAttributes("embed", "src", "wmode", "flashvars", "pluginspage", "allowFullScreen", "allowfullscreen",
                        "quality", "width", "height", "align", "allowScriptAccess", "allowscriptaccess", "allownetworking", "type")
                .addAttributes("span", "class", "style")

                .addProtocols("blockquote", "cite", "http", "https")
                .addProtocols("cite", "cite", "http", "https")
                .addProtocols("q", "cite", "http", "https")
                .addProtocols("embed", "src", "http", "https");

    }

    public static void main(String[] args) {
        StringBuilder html = new StringBuilder();
        html.append("123");
        html.append("<a href=\" http://www.iocoder.cn\" onclick=\"alert(1)\" target=\"_blank\">芋道源码</a>");
        html.append("<img src=\"https://static.iocoder.cn/images/ruoyi-vue-pro/%E7%99%BB%E5%BD%95.jpg\" />");
        html.append("321<img src=1 οnerrοr=\"alert(1)\">");
        html.append("<script>alert('111')</script>");
        System.out.println(filter(html.toString()));
    }

}
