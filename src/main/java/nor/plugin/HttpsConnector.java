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

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.MatchResult;

import nor.core.plugin.PluginAdapter;
import nor.core.proxy.filter.MessageHandler;
import nor.core.proxy.filter.MessageHandlerAdapter;
import nor.http.HeaderName;
import nor.http.HttpRequest;
import nor.http.HttpResponse;
import nor.http.Status;
import nor.util.io.Stream;
import nor.util.log.Logger;

public class HttpsConnector extends PluginAdapter{

	private String urlRegex = null;
	private Map<String, String> redirects = null;

	private static final Logger LOGGER = Logger.getLogger(HttpsConnector.class);

	@Override
	public void init(final File common, final File local) throws IOException{
		LOGGER.entering("init", common, local);

		if(!common.exists()){

			final InputStream in = this.getClass().getResourceAsStream("hosts.default");
			final OutputStream out = new FileOutputStream(common);

			Stream.copy(in, out);

			out.close();
			in.close();

		}
		final Properties prop = new Properties();
		final Reader r = new FileReader(common);
		prop.load(r);
		r.close();

		if(local.exists()){

			final Properties localProp = new Properties();
			final Reader localIn = new FileReader(local);
			localProp.load(localIn);
			localIn.close();

			prop.putAll(localProp);

		}

		if(prop.size() != 0){

			this.redirects = new HashMap<String, String>();

			/* 1. www.aaa.com
			 * 2. example.bbb.net
			 * => http://(www.aaa.com|example.bbb.net)(.*)
			 */
			final StringBuilder regex = new StringBuilder();
			regex.append("http://(");
			for(final Object key : prop.keySet()){

				final String host = key.toString();

				regex.append(this.toRegex(host));
				regex.append("|");

				this.redirects.put(host, prop.getProperty(host));

			}
			regex.setLength(regex.length() - 1);
			regex.append(")(.*)");

			this.urlRegex = regex.toString();

		}

		LOGGER.exiting("init");
	}

	@Override
	public MessageHandler[] messageHandlers() {

		if(this.urlRegex == null){

			return null;

		}

		return new MessageHandler[]{

				new MessageHandlerAdapter(HttpsConnector.this.urlRegex){

					@Override
					public HttpResponse doRequest(final HttpRequest request, final MatchResult url) {
						LOGGER.entering(this.getClass(), "doRequest", request, url);

						final String host = url.group(1);
						final String path = url.group(2);

						final HttpResponse res = request.createResponse(Status.Found);
						if(path != null){

							final String new_host = HttpsConnector.this.redirects.get(host);
							res.getHeader().set(HeaderName.Location, String.format("https://%s%s", new_host, path));

						}else{

							res.getHeader().set(HeaderName.Location, String.format("https://%s", host));
						}

						LOGGER.exiting(this.getClass(), "doRequest", res);
						return res;
					}

				}

		};

	}

	private String toRegex(final String str){

		return str.toString().replace(".", "\\.").replace("*", "[^/]*").replace("(", "(?:");

	}

}
