package com.tonini.diego.musicat.entity;

import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by Diego on 24/08/2015.
 */
public class MyNanoHTTPD extends NanoHTTPD {

    public static final String TEXT_404 = "404 File Not Found";
    public List<Track> mTracks;

    public MyNanoHTTPD(int port,List<Track> tracks){
        this(port);
        mTracks = tracks;
    }
    public MyNanoHTTPD(String hostname, int port) {
        super(hostname, port);
    }

    public MyNanoHTTPD(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession arg0) {

        Log.w("Httpd","request uri: "+arg0.getUri());
        for(String key : arg0.getParms().keySet()){
            Log.w("Httpd","param: "+key+", value: "+arg0.getParms().get(key));
        }

        if(arg0.getUri().equals("/")) {
            return new Response(Response.Status.OK, "text/html", responceHome());
        }
        else if(arg0.getUri().contains("play")){

            int index = Integer.parseInt(arg0.getParms().get("pos"));
            Log.w("Httpd", "want to play index: "+index);
            File videoFile = new File(mTracks.get(index).getTrackUri().toString());
            try {
                FileInputStream fis = new FileInputStream(videoFile);
                return new Response(Response.Status.OK,MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3"),fis);
                //return new Response(Response.Status.OK, MimeTypeMap.getSingleton().getMimeTypeFromExtension(Files.getFileExtension(videoFile.getPath())),fis);
            } catch (FileNotFoundException e) {
                Log.e("Httpd","file not ofund "+e.toString());
            }
        }

        // not found
        return new Response(Response.Status.NOT_FOUND, "text/html", "error");
    }

    public String responceHome(){
        String s =
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                        "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                        "<head>\n" +
                        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                        "\n" +
                        "<title>Simple Little Table in HTML/CSS3</title>\n" +
                        "\n" +
                        "<style type=\"text/css\">\n" +
                        "\n" +
                        "/*\n" +
                        "This was made by Joao Sardinha\n" +
                        "Visit me at http://johnsardine.com/\n" +
                        "\n" +
                        "The table style doesnt contain images, it contains gradients for browsers who support them as well as round corners and drop shadows.\n" +
                        "\n" +
                        "It has been tested in all major browsers.\n" +
                        "\n" +
                        "This table style is based on Simple Little Table By Orman Clark,\n" +
                        "you can download a PSD version of this at his website, PremiumPixels.\n" +
                        "http://www.premiumpixels.com/simple-little-table-psd/\n" +
                        "\n" +
                        "PLEASE CONTINUE READING AS THIS CONTAINS IMPORTANT NOTES\n" +
                        "\n" +
                        "*/\n" +
                        "\n" +
                        "/*\n" +
                        "Im reseting this style for optimal view using Eric Meyer's CSS Reset - http://meyerweb.com/eric/tools/css/reset/\n" +
                        "------------------------------------------------------------------ */\n" +
                        "body, html  { height: 100%; }\n" +
                        "html, body, div, span, applet, object, iframe,\n" +
                        "/*h1, h2, h3, h4, h5, h6,*/ p, blockquote, pre,\n" +
                        "a, abbr, acronym, address, big, cite, code,\n" +
                        "del, dfn, em, font, img, ins, kbd, q, s, samp,\n" +
                        "small, strike, strong, sub, sup, tt, var,\n" +
                        "b, u, i, center,\n" +
                        "dl, dt, dd, ol, ul, li,\n" +
                        "fieldset, form, label, legend,\n" +
                        "table, caption, tbody, tfoot, thead, tr, th, td {\n" +
                        "\tmargin: 0;\n" +
                        "\tpadding: 0;\n" +
                        "\tborder: 0;\n" +
                        "\toutline: 0;\n" +
                        "\tfont-size: 100%;\n" +
                        "\tvertical-align: baseline;\n" +
                        "\tbackground: transparent;\n" +
                        "}\n" +
                        "body { line-height: 1; }\n" +
                        "ol, ul { list-style: none; }\n" +
                        "blockquote, q { quotes: none; }\n" +
                        "blockquote:before, blockquote:after, q:before, q:after { content: ''; content: none; }\n" +
                        ":focus { outline: 0; }\n" +
                        "del { text-decoration: line-through; }\n" +
                        "table {border-spacing: 0; } /* IMPORTANT, I REMOVED border-collapse: collapse; FROM THIS LINE IN ORDER TO MAKE THE OUTER BORDER RADIUS WORK */\n" +
                        "\n" +
                        "/*------------------------------------------------------------------ */\n" +
                        "\n" +
                        "/*This is not important*/\n" +
                        "body{\n" +
                        "\tfont-family:Arial, Helvetica, sans-serif;\n" +
                        "\tbackground: url(background.jpg);\n" +
                        "\tmargin:0 auto;\n" +
                        "\twidth:520px;\n" +
                        "}\n" +
                        "a:link {\n" +
                        "\tcolor: #666;\n" +
                        "\tfont-weight: bold;\n" +
                        "\ttext-decoration:none;\n" +
                        "}\n" +
                        "a:visited {\n" +
                        "\tcolor: #666;\n" +
                        "\tfont-weight:bold;\n" +
                        "\ttext-decoration:none;\n" +
                        "}\n" +
                        "a:active,\n" +
                        "a:hover {\n" +
                        "\tcolor: #bd5a35;\n" +
                        "\ttext-decoration:underline;\n" +
                        "}\n" +
                        "\n" +
                        "\n" +
                        "/*\n" +
                        "Table Style - This is what you want\n" +
                        "------------------------------------------------------------------ */\n" +
                        "table a:link {\n" +
                        "\tcolor: #666;\n" +
                        "\tfont-weight: bold;\n" +
                        "\ttext-decoration:none;\n" +
                        "}\n" +
                        "table a:visited {\n" +
                        "\tcolor: #999999;\n" +
                        "\tfont-weight:bold;\n" +
                        "\ttext-decoration:none;\n" +
                        "}\n" +
                        "table a:active,\n" +
                        "table a:hover {\n" +
                        "\tcolor: #bd5a35;\n" +
                        "\ttext-decoration:underline;\n" +
                        "}\n" +
                        "table {\n" +
                        "\tfont-family:Arial, Helvetica, sans-serif;\n" +
                        "\tcolor:#666;\n" +
                        "\tfont-size:12px;\n" +
                        "\ttext-shadow: 1px 1px 0px #fff;\n" +
                        "\tbackground:#eaebec;\n" +
                        "\tmargin:20px;\n" +
                        "\tborder:#ccc 1px solid;\n" +
                        "\n" +
                        "\t-moz-border-radius:3px;\n" +
                        "\t-webkit-border-radius:3px;\n" +
                        "\tborder-radius:3px;\n" +
                        "\n" +
                        "\t-moz-box-shadow: 0 1px 2px #d1d1d1;\n" +
                        "\t-webkit-box-shadow: 0 1px 2px #d1d1d1;\n" +
                        "\tbox-shadow: 0 1px 2px #d1d1d1;\n" +
                        "}\n" +
                        "table th {\n" +
                        "\tpadding:21px 25px 22px 25px;\n" +
                        "\tborder-top:1px solid #fafafa;\n" +
                        "\tborder-bottom:1px solid #e0e0e0;\n" +
                        "\n" +
                        "\tbackground: #ededed;\n" +
                        "\tbackground: -webkit-gradient(linear, left top, left bottom, from(#ededed), to(#ebebeb));\n" +
                        "\tbackground: -moz-linear-gradient(top,  #ededed,  #ebebeb);\n" +
                        "}\n" +
                        "table th:first-child{\n" +
                        "\ttext-align: left;\n" +
                        "\tpadding-left:20px;\n" +
                        "}\n" +
                        "table tr:first-child th:first-child{\n" +
                        "\t-moz-border-radius-topleft:3px;\n" +
                        "\t-webkit-border-top-left-radius:3px;\n" +
                        "\tborder-top-left-radius:3px;\n" +
                        "}\n" +
                        "table tr:first-child th:last-child{\n" +
                        "\t-moz-border-radius-topright:3px;\n" +
                        "\t-webkit-border-top-right-radius:3px;\n" +
                        "\tborder-top-right-radius:3px;\n" +
                        "}\n" +
                        "table tr{\n" +
                        "\ttext-align: center;\n" +
                        "\tpadding-left:20px;\n" +
                        "}\n" +
                        "table tr td:first-child{\n" +
                        "\ttext-align: left;\n" +
                        "\tpadding-left:20px;\n" +
                        "\tborder-left: 0;\n" +
                        "}\n" +
                        "table tr td {\n" +
                        "\tpadding:18px;\n" +
                        "\tborder-top: 1px solid #ffffff;\n" +
                        "\tborder-bottom:1px solid #e0e0e0;\n" +
                        "\tborder-left: 1px solid #e0e0e0;\n" +
                        "\t\n" +
                        "\tbackground: #fafafa;\n" +
                        "\tbackground: -webkit-gradient(linear, left top, left bottom, from(#fbfbfb), to(#fafafa));\n" +
                        "\tbackground: -moz-linear-gradient(top,  #fbfbfb,  #fafafa);\n" +
                        "}\n" +
                        "table tr.even td{\n" +
                        "\tbackground: #f6f6f6;\n" +
                        "\tbackground: -webkit-gradient(linear, left top, left bottom, from(#f8f8f8), to(#f6f6f6));\n" +
                        "\tbackground: -moz-linear-gradient(top,  #f8f8f8,  #f6f6f6);\n" +
                        "}\n" +
                        "table tr:last-child td{\n" +
                        "\tborder-bottom:0;\n" +
                        "}\n" +
                        "table tr:last-child td:first-child{\n" +
                        "\t-moz-border-radius-bottomleft:3px;\n" +
                        "\t-webkit-border-bottom-left-radius:3px;\n" +
                        "\tborder-bottom-left-radius:3px;\n" +
                        "}\n" +
                        "table tr:last-child td:last-child{\n" +
                        "\t-moz-border-radius-bottomright:3px;\n" +
                        "\t-webkit-border-bottom-right-radius:3px;\n" +
                        "\tborder-bottom-right-radius:3px;\n" +
                        "}\n" +
                        "table tr:hover td{\n" +
                        "\tbackground: #f2f2f2;\n" +
                        "\tbackground: -webkit-gradient(linear, left top, left bottom, from(#f2f2f2), to(#f0f0f0));\n" +
                        "\tbackground: -moz-linear-gradient(top,  #f2f2f2,  #f0f0f0);\t\n" +
                        "}\n" +
                        "\n" +
                        "</style>\n" +
                        "\n" +
                        "</head>\n" +
                        "\n" +
                        "<body>\n" +
                        "<table cellspacing='0'> <!-- cellspacing='0' is important, must stay -->\n" +
                        "\t<tr><th>Tracks</th><th>Artist</th><th>Album</th></tr><!-- Table Header -->\n" +
                        "    \n" +
                        "<!-- Table Row -->\n" +
                        "\t" +
                        getAllTr() +
                        "<!-- Darker Table Row -->\n" +
                        "</table>"+
                        "</body>\n" +
                        "</html>";

        return s;
    }

    private String getTrWithTrack(Track track){
        return "<tr><td><a href=\"play.html?mode=play&pos="+String.valueOf(mTracks.indexOf(track))+"\">"+track.getTitle()+"</a></td><td>"+track.getArtist()+"</td><td>"+track.getAlbum()+"</td></tr>";
    }
    private String getAllTr(){
        String s = "";
        for(Track t : mTracks){
            //Log.i("httpd","concat: "+t.getTitle());
            s+= getTrWithTrack(t);
        }
        return s;
    }

    public interface OnTrackSelectedWeb{
        void onTrackSelectedWeb(int pos);
    }

    public void setOnTrackSelectedWebListener(OnTrackSelectedWeb onTrackSelectedWebListener){
        this.calback = onTrackSelectedWebListener;
    }
    private OnTrackSelectedWeb calback;


}


