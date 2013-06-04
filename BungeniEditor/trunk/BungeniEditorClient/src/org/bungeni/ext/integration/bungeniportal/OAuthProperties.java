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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;
import org.bungeni.editor.config.BaseConfigReader;

/**
 *
 * @author Ashok Hariharan
 */
public class OAuthProperties {
    
    private static org.apache.log4j.Logger log =
       Logger.getLogger(OAuthProperties.class.getName());


    
    private Properties oauthProperties = null;
    private static OAuthProperties instance = null;
    public static final String FILE_NAME = "oauth.properties";
    public static final String REFRESH_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss" ; 
    
        /**
    e.g content of oauth.properties
    authorization_code=55e9be20f3e047216c4bf6746d850fa9dad4438d
    authorization_time=2013-05-30 11\:58\:45
    expires_in=3600
    refresh_token=b7e35f2a10319b739a0dd48d06c48746901b5429
    token_type=bearer
    authorization_state=dQLWUzRM
    access_token=44e5f024c9ed612015e6763e6d0cc190d4cffeb2

     **/

    public static final String[] VALID_OAUTH_PROPERTIES = {
      "authorization_code",
      "authorization_time",
      "expires_in",
      "refresh_token",
      "token_type",
      "access_token"
    };
    
    private OAuthProperties(){
        oauthProperties = new Properties();
    }
    
    public static OAuthProperties getInstance(){
        if (instance == null) {
            instance = new OAuthProperties();
        }
        return instance;
    }
    
    public boolean fileExists(){
        return this.getFile().isFile();
    }
    
    
    private File getFile(){
        File fOauthProps = new File(
                BaseConfigReader.getSettingsFolder() + File.separator + FILE_NAME
                );
        return fOauthProps;
    }

    
    public void  loadOauthProperties() throws IOException{
        File f = getFile();
        Properties props = new Properties();
        if (f.isFile()) {
            props.load(new FileInputStream(f));
        }
        this.oauthProperties = props;
     }
    
    public void setProperties(Properties props){
        this.oauthProperties = props;
    }
    
    public Properties getProperties(){
        return this.oauthProperties;
    }
    
    public void saveToFile() throws IOException{
        File fOauthProps = getFile();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.oauthProperties.store(
                new FileOutputStream(fOauthProps), 
                "stored on " + df.format(new Date())
                );
    }

    
    public enum OAuthState {
        INVALID,
        EXPIRED,
        VALID
    }
    
   public OAuthState validate(){
        Properties props = this.getProperties();
        for (String validProp : VALID_OAUTH_PROPERTIES) {
            if (!props.containsKey(validProp)){
                return OAuthState.INVALID;
            }
        }
        try {
        if (isAccessTokenExpired()) {
            return OAuthState.EXPIRED;
        } else {
            return OAuthState.VALID;
        }
        } catch(ParseException ex) {
            log.error("Error while parsing refresh date", ex);
        }
        return OAuthState.EXPIRED;
    }

    public boolean isAccessTokenExpired() throws ParseException {
        long expiryTime = 3600 ; 
        // parse the refresh date
        DateFormat df = getOauthRefreshDateFormat();
        Date dtOriginal = df.parse(this.getProperties().get("authorization_time").toString());
        Calendar c = Calendar.getInstance();
        Date dtCurrent = c.getTime();
        long timeDiff = Math.abs( 
                (dtCurrent.getTime() - dtOriginal.getTime()) / 1000 
                ) ;
        if (timeDiff >= expiryTime ) {
            return true;
        }
        return false;
    }

       
    private DateFormat getOauthRefreshDateFormat(){
      return new SimpleDateFormat(REFRESH_DATE_FORMAT);
    }
 
   
}

