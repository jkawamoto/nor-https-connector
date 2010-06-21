package nor.plugin;

import java.util.regex.MatchResult;

import nor.core.plugin.Plugin;
import nor.core.proxy.filter.MessageHandler;
import nor.core.proxy.filter.MessageHandlerAdapter;
import nor.http.HeaderName;
import nor.http.HttpRequest;
import nor.http.HttpResponse;
import nor.http.Status;

public class HttpsConnector extends Plugin{

	/* 1. www.aaa.com
	 * 2. example.bbb.net
	 * => http://(www.aaa.com|example.bbb.net)(.*)
	 */

	private String urlRegex;

	public void init(){

		final String[] hosts = {

				"www.facebook.com",
				"*google.com",
				"twitter.com"

		};

		final StringBuilder regex = new StringBuilder();
		regex.append("http://(");

		int i = 0;
		for(; i != hosts.length-1; ++i){

			regex.append(hosts[i].replace(".", "\\.").replace("*", ".*").replace("(", "(?:"));
			regex.append("|");

		}
		regex.append(hosts[i].replace(".", "\\.").replace("*", ".*").replace("(", "(?:"));
		regex.append(")(.*)");

		this.urlRegex = regex.toString();

	}

	@Override
	public MessageHandler[] messageHandlers() {

		return new MessageHandler[]{

				new MessageHandlerAdapter(HttpsConnector.this.urlRegex){

					@Override
					public HttpResponse doRequest(final HttpRequest request, final MatchResult url) {

						final String host = url.group(1);
						final String path = url.group(2) != null ? url.group(2) : "";

						final HttpResponse res = request.createResponse(Status.Found);
						res.getHeader().set(HeaderName.Location, String.format("https://%s%s", host, path));

						return res;

					}

				}

		};

	}

}
