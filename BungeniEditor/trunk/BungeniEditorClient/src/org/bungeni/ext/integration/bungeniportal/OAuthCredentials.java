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
package org.bungeni.ext.integration.bungeniportal;

import java.text.MessageFormat;
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
    public final String oauthAccessTokenUri;
    public String oauthRefreshTokenUri;
    
    public OAuthCredentials(
            String appId, 
            String appSecret, 
            String authUri, 
            String authFormUri, 
            String authAccessTokenUri, 
            String refreshTokenUri
            ){
        this.oauthAppId = appId;
        this.oauthAppSecret = appSecret;
        this.oauthAuthUri = authUri;
        this.oauthFormUri = authFormUri;
        this.oauthAccessTokenUri = authAccessTokenUri;
        this.oauthRefreshTokenUri = refreshTokenUri;
    }

    public String authUri(){
        // "/oauth/authorize?client_id={0}&client_secret={1}&response_type=code&state={2}"
        Object[] values = {
            this.oauthAppId,
            this.oauthAppSecret,
            RandomStringUtils.randomAlphabetic(8)
        };
        String sUri  = MessageFormat.format(this.oauthAuthUri, values);
        return sUri;
    }
    
    public String accessTokenUri(String authorizationCode){
        // /oauth/access-token?client_id={0}&grant_type=authorization_code&code={1}
        Object[] values = {
          this.oauthAppId,  
          authorizationCode
        };
        
        String sUri = MessageFormat.format(this.oauthAccessTokenUri, values);
        return sUri;
    }
    
    public String renewAccessTokenUri(String refreshToken){
        // /oauth/access-token?grant_type=refresh_token&refresh_token={0}
        StringBuilder suri = new StringBuilder(this.oauthRefreshTokenUri);
        Object[] values = {
            refreshToken
        };
        String sUri = MessageFormat.format(this.oauthRefreshTokenUri, values);
        return sUri;
    }
    
    
    public String authFormUri() {
        return this.oauthFormUri;
    }
}
