/***
  Copyright (c) 2008-2012 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
  
  From _The Busy Coder's Guide to Advanced Android Development_
    http://commonsware.com/AdvAndroid
*/

package com.commonsware.android.feedfrags;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.support.v4.app.Fragment;

public class ContentFragment extends Fragment {
  private String urlToLoad=null;
  
  public ContentFragment() {
    this(null);
  }
  
  public ContentFragment(String url) {
    super();
    
    urlToLoad=url;
    setRetainInstance(true);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
    return(inflater.inflate(R.layout.content_fragment, container, false));
  }
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    
    WebView browser=getBrowser();
    
    browser.setWebViewClient(new RedirectFixer());
    browser.getSettings().setJavaScriptEnabled(true);
    
    if (savedInstanceState!=null) {
      browser.restoreState(savedInstanceState);
    }
    else if (urlToLoad!=null) {
      loadUrl(urlToLoad);
    }
  }
  
  @Override
  public void onSaveInstanceState(Bundle state) {
    super.onSaveInstanceState(state);
    
    WebView browser=getBrowser();
    
    if (browser!=null) {
      browser.saveState(state);
    }
  }
  
  public void loadUrl(String url) {
    getBrowser().loadUrl(url);
  }
  
  private WebView getBrowser() {
    return((WebView)(getView().findViewById(R.id.browser)));
  }

  private class RedirectFixer extends WebViewClient {
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      view.loadUrl(url);
      
      return(true);
    }
  }
}