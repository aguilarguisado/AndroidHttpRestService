Android Http Service
======
**Android Http Service** is probably the easiest way of using RestServices with Android.

With this component you only have to define whatever you want before and after the request, and declare an intent in your manifest. It will handle network problems, verbs, headers etc. only giving them to it. Developers only have to tell the component what they want to send.

For this first version you can:

* Set headers.
* Set the type of the verb for the request
* Set default query parameters that used to be in your URL (even with POST, like http://www.example.com?api=json)
* Define your logout method and isLogged method for the cases when user must be authentified before making the request.
* Set parameters for the query (via JSON or query parameters attached to the URL).


##Screenshots
![Screenshot Android](https://github.com/juaagugui/AndroidHttpRestService/blob/master/art/screenshot1.png = 300x512 "screenshot1")

![Screenshot Android](https://github.com/juaagugui/AndroidHttpRestService/blob/master/art/screenshot2.png = 300x512 "screenshot1")

![Screenshot Android](https://github.com/juaagugui/AndroidHttpRestService/blob/master/art/screenshot3.png = 300x512 "screenshot1")

## Version 
* Version 1.0

## How-to use this code

###Implementation
 * Declare your **IHttpManagerService** in your fragment or your activity and define the two required method (**isLogged** and **logout**) in case you need them. You can also let them empty. For example:

			httpManagerService = new HttpManagerService(this.getActivity(),this) {
				@Override
				public boolean logout() {
					// Not necessary
					return false;
				}
				@Override
				public boolean isLogged() {
					// Not necessary
					return false;
				}
			};

* The **IHttpManagerService** receives a listener **OnHttpEventListener**. In our sample, the fragment implements this interface. The code implemented in its two methods will be executed before and after the request. For example, we can show/hide loading before and after the request.

			@Override
			public void onRequestInit() {
				loading.setVisibility(ProgressBar.VISIBLE);
			}
		
			@Override
			public void onRequestFinish() {				
				loading.setVisibility(ProgressBar.GONE);
			}

	
* **IMPORTANT!** Don't forget to declare RestIntent in your AndroidManifest!! It should be placed inside your application tag.

			<service android:name="mss.httpService.services.RESTIntentService" >
			</service>	 

###Use
* To make a HttpRequest you have to define a HttpConnection object. We can set the object from its contructor or by its getters and setters. A simple HttpConnection:

			IHttpConnection connection = new HttpConnection("http://www.example.com?p=q", //URL
														RESTIntentService.GET, //Verb=GET
														null, //No JSON
														this); //OnRESTResultCallback
* OnRESTResultCallback implements the method which will be called when the server side makes a response. We have to process the response with appropriates codes, Strings, JSONs or whatever we expect or can get.

			@Override
			public void onRESTResult(int code, String result) {
				//Custom behavior
			}
* At last, make the request (it could raise NoInternetConnectionException). The method declared above will handle the response:

			try {
				httpManagerService.sendRequest(connection);
			} catch (NoInternetConnectionException expected) {
				setCustomCSSHtmlToWebView(R.string.noInternet, "no_internet.png");
			}

## License 
* see [LICENSE](https://github.com/juaagugui/AndroidHttpRestService/blob/master/LICENSE) file

## Contact
#### Developer/Company
* e-mail: juan.aguilar.guisado@gmail.com
* Twitter: [@juaagugui](https://twitter.com/juaagugui)
* Linkedin: [Juan Aguilar Guisado](http://es.linkedin.com/in/juanaguilarguisado)