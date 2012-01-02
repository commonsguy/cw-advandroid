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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Feed implements Comparable<Feed> {
  static private HashMap<String, Feed> FEEDS=new HashMap<String, Feed>();

  static {
    addFeed("Slashdot", "http://rss.slashdot.org/Slashdot/slashdot");
    addFeed("Wikipedia New Pages",
            "http://en.wikipedia.org/w/index.php?title=Special:NewPages&feed=rss");
    addFeed("EFF", "http://www.eff.org/rss/updates.xml");
    addFeed("Google News", "http://news.google.com/news?pz=1&cf=all&ned=us&hl=en&output=rss");
  }

  private String name;
  private String url;
  
  public static Feed addFeed(String name, String url) {
    Feed result=new Feed(name, url);
    
    addFeed(result);
    
    return(result);
  }
  
  private static void addFeed(Feed feed) {
    FEEDS.put(feed.getKey(), feed);
  }
  
  public static ArrayList<Feed> getFeeds() {
    ArrayList<Feed> result=new ArrayList<Feed>(FEEDS.values());
    
    Collections.sort(result);
    
    return(result);
  }
  
  public static Feed getFeed(String key) {
    return(FEEDS.get(key));
  }
  
  private Feed(String name, String url) {
    this.name=name;
    this.url=url;
  }
  
  public String getKey() {
    return(toString());
  }
  
  public String toString() {
    return(name);
  }
  
  public String getUrl() {
    return(url);
  }
  
  public int compareTo(Feed another) {
    return(toString().compareTo(another.toString()));
  }
}