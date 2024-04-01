package com.nezip.bizcard1;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;

public class changeString {
	String result;

	// htlm의 문자 사용불가로 변경해주기
	String strTohtml(String html) {
		StringBuffer replacedBuf = new StringBuffer(html.length());
		for (char c : html.toCharArray()) {
			switch (c) {
			case '#':
				replacedBuf.append("%23");
				break;
			case '%':
				replacedBuf.append("%25");
				break;
			case '\'':
				replacedBuf.append("%27");
				break;
			case '?':
				replacedBuf.append("%3f");
				break;
			case '☆':
				replacedBuf.append("%27");
				break;
			default:
				replacedBuf.append(c);
			}

		}
		result = replacedBuf.toString();
		return result;
	}

	String dbTostr(String str) {

		if (str != null && !"".equals(str.trim())) {
			StringBuffer replacedBuf = new StringBuffer(str.length());
			for (char c : str.toCharArray()) {
				switch (c) {
				case '☆':
					replacedBuf.append("\'");
					break;
				default:
					replacedBuf.append(c);
				}
			}
			result = replacedBuf.toString();
			if ("1900-01-01".equals(result)) {
				result = "";
			}
			return result;
		} else {
			return "";
		}
	}

	String strTodb(String str) {
		StringBuffer replacedBuf = new StringBuffer(str.length());
		for (char c : str.toCharArray()) {
			switch (c) {
			case '\'':
				replacedBuf.append("☆");
				break;

			default:
				replacedBuf.append(c);
			}
		}
		result = replacedBuf.toString();
		return result;
	}

	// 기사 세부내용
	String contentsTohtml(String html, String image1, String image2,
			String image3, String image4, String image5, String image6,
			String image7, String image8, String image9, String image10) {
		String change1 = html.replaceAll("[%%IMAGE1%%]", "<center><img src='"
				+ image1 + "' width='95%' ></center><br>");
		String change2 = change1.replaceAll("[%%IMAGE2%%]",
				"<center><img src='" + image2 + "' width='95%' ></center><br>");
		String change3 = change2.replaceAll("[%%IMAGE3%%]",
				"<center><img src='" + image3 + "' width='95%' ></center><br>");
		String change4 = change3.replaceAll("[%%IMAGE4%%]",
				"<center><img src='" + image4 + "' width='95%' ></center><br>");
		String change5 = change4.replaceAll("[%%IMAGE5%%]",
				"<center><img src='" + image5 + "' width='95%' ></center><br>");
		String change6 = change5.replaceAll("[%%IMAGE6%%]",
				"<center><img src='" + image6 + "' width='95%' ></center><br>");
		String change7 = change6.replaceAll("[%%IMAGE7%%]",
				"<center><img src='" + image7 + "' width='95%' ></center><br>");
		String change8 = change7.replaceAll("[%%IMAGE8%%]",
				"<center><img src='" + image8 + "' width='95%' ></center><br>");
		String change9 = change8.replaceAll("[%%IMAGE9%%]",
				"<center><img src='" + image9 + "' width='95%' ></center><br>");
		String change10 = change9
				.replaceAll("[%%IMAGE10%%]", "<center><img src='" + image10
						+ "' width='95%' ></center><br>");
		String change11 = change10.replaceAll("<P align=justify />", "");
		StringBuffer replacedBuf = new StringBuffer(change11.length());
		for (char c : change11.toCharArray()) {
			switch (c) {
			case '#':
				replacedBuf.append("%23");
				break;
			case '%':
				replacedBuf.append("%25");
				break;
			case '\'':
				replacedBuf.append("%27");
				break;
			case '?':
				replacedBuf.append("%3f");
				break;
			case '☆':
				replacedBuf.append("%27");
				break;
			default:
				replacedBuf.append(c);
			}

		}
		result = replacedBuf.toString();
		return result;
	}

	// 기사 이멜 보내기
	String contentsToemail(String html) {
		String change1 = html.replaceAll("[%%IMAGE1%%]", "");
		String change2 = change1.replaceAll("[%%IMAGE2%%]", "");
		String change3 = change2.replaceAll("[%%IMAGE3%%]", "");
		String change4 = change3.replaceAll("[%%IMAGE4%%]", "");
		String change5 = change4.replaceAll("[%%IMAGE5%%]", "");
		String change6 = change5.replaceAll("[%%IMAGE6%%]", "");
		String change7 = change6.replaceAll("[%%IMAGE7%%]", "");
		String change8 = change7.replaceAll("[%%IMAGE8%%]", "");
		String change9 = change8.replaceAll("[%%IMAGE9%%]", "");
		String change10 = change9.replaceAll("[%%IMAGE10%%]", "");
		String change11 = change10.replaceAll("<P align=justify />", "");
		result = change11;

		return result;
	}

	static String htmlToStr(String html) {
		try {
			String url = new String();
			url = html;
			url = URLDecoder.decode(url, "UTF-8");
			url = new String(url.getBytes("UTF-8"), "8859_1");

			return url;
		} catch (Exception E) {
			return E.toString();
		}

	}

	static String changeIt(String val) {
		String newStr = "";
		/*
		 * String frag = ""; String encval = "";
		 * 
		 * val = val.replaceAll("%3C", "<"); val = val.replaceAll("%3E", ">");
		 * val = val.replaceAll("%22", "\""); val = val.replaceAll("%20", " ");
		 * val = val.replaceAll("%58", ":"); val = val.replaceAll("%47", "/");
		 * 
		 * int len = val.length(); int backlen = len; int i = 0;
		 * 
		 * 
		 * while (backlen > 0) { int lastpercent = val.lastIndexOf("%"); if
		 * (lastpercent != -1) { frag = val.substring(lastpercent+1,
		 * val.length()); val = val.substring(0, lastpercent); if (frag.length()
		 * >= 2) { encval = frag.substring(0,2); newStr =
		 * frag.substring(2,frag.length()) + newStr; if
		 * ("01234567890abcdefABCDEF".indexOf(encval.substring(0,1)) != -1 &&
		 * "01234567890abcdefABCDEF".indexOf(encval.substring(1,2)) != -1) {
		 * encval = Character.toString((char)Integer.parseInt(encval, 16)); //
		 * hex to base 10 newStr = encval + newStr; // prepend the char in }
		 * else { newStr = "&#037;"+encval + newStr; } } backlen = lastpercent;
		 * } else { newStr = val + newStr; backlen = 0; } }
		 */

		newStr = val;
		return newStr;
	}

}
