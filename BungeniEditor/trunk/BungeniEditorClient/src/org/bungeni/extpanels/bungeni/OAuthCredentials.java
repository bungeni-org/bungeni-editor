/*
 * Copyright (C) 2013 Africa i-Parliaments
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.bungeni.extpanels.bungeni;

import org.apache.commons.lang.RandomStringUtils;

/**
 *
 * @author Ashok Hariharan
 */
public class OAuthCredentials {
    public final String oauthAppId ; 
    public final String oauthAppSecret ; 
    public final String oauthAuthUri;
    public final String oauthFormUri;
    public final String oauthTokenUri;
    public String refreshState;
    public String refreshCode;
    
    public OAuthCredentials(
            String appId, 
            String appSecret, 
            String authUri, 
            String authFormUri, 
            String authTokenUri, 
            String refreshCode, 
            String refreshState
            ){
        this.oauthAppId = appId;
        this.oauthAppSecret = appSecret;
        this.oauthAuthUri = authUri;
        this.oauthFormUri = authFormUri;
        this.oauthTokenUri = authTokenUri;
        this.refreshCode = refreshCode;
        this.refreshState = refreshState;
    }
    
    public void setRefreshCode(String refreshCode) {
        this.refreshCode = refreshCode;
    }
    
    public void setRefreshState(String refreshState){
        this.refreshState = refreshState;
    }
    
    public String authUri(){
       
        StringBuilder suri = new StringBuilder(this.oauthAuthUri);
        suri.append("?client_id=").append(oauthAppId).
                append("&client_secret=").append(oauthAppSecret).
                append("&response_type=code").
                append("&state=").append(RandomStringUtils.randomAlphabetic(8));
        return suri.toString();
        
    }
    
    public String accessTokenUri(){
       
        StringBuilder suri = new StringBuilder(this.oauthTokenUri);
        suri.append("?client_id=").append(oauthAppId).
                append("&grant_type=authorization_code").
                append("&code=").append(this.refreshCode);
        return suri.toString();
        
    }
    
    public String authFormUri() {
        return this.oauthFormUri;
    }
    
    public String authTokenUri(){
        return this.oauthTokenUri;
    }
    
    public String refreshCode(){
        return this.refreshCode;
    }
    
    public String refreshState(){
        return this.refreshState;
    }
}
