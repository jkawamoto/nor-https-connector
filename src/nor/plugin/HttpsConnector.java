/*
 *  Copyright (C) 2010 Junpei Kawamoto
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

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
				"twitter.com",
				"www.evernote.com",
				"www.dropbox.com",
				"www.rememberthemilk.com",
				"www.paypal*",

		};

		final StringBuilder regex = new StringBuilder();
		regex.append("http://(");

		int i = 0;
		for(; i != hosts.length-1; ++i){

			regex.append(hosts[i].replace(".", "\\.").replace("*", "[^/]*").replace("(", "(?:"));
			regex.append("|");

		}
		regex.append(hosts[i].replace(".", "\\.").replace("*", "[^/]*").replace("(", "(?:"));
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
