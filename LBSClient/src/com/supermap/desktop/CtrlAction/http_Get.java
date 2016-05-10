package com.supermap.desktop.CtrlAction;

import java.io.FileOutputStream; 
import java.io.IOException; 
import java.io.InputStream; 
import java.net.HttpURLConnection; 
import java.net.MalformedURLException; 
import java.net.URL; 
  
  
public class http_Get { 
  
  public http_Get() { 
    // TODO Auto-generated constructor stub 
  } 
  
  public static void saveImageToDisk() { 
	String URL_PATH = "http://192.168.1.125:8080/myhttp/pro1.png";
    InputStream inputStream = getInputStream(URL_PATH); 
    byte[] data = new byte[1024]; 
    int len = 0; 
    FileOutputStream fileOutputStream = null; 
    try { 
      fileOutputStream = new FileOutputStream("C:\\test.png"); 
      while ((len = inputStream.read(data)) != -1) { 
        fileOutputStream.write(data, 0, len); 
      } 
    } catch (IOException e) { 
      // TODO Auto-generated catch block 
      e.printStackTrace(); 
    } finally { 
      if (inputStream != null) { 
        try { 
          inputStream.close(); 
        } catch (IOException e) { 
          // TODO Auto-generated catch block 
          e.printStackTrace(); 
        } 
      } 
      if (fileOutputStream != null) { 
        try { 
          fileOutputStream.close(); 
        } catch (IOException e) { 
          // TODO Auto-generated catch block 
          e.printStackTrace(); 
        } 
      } 
    } 
  } 
  
  /** 
   * 获得服务器端的数据,以InputStream形式返回 
   * @return 
   */
  public static InputStream getInputStream(String urlPath) { 
    InputStream inputStream = null; 
    HttpURLConnection httpURLConnection = null; 
    try { 
      URL url = new URL(urlPath); 
      if (url != null) { 
        httpURLConnection = (HttpURLConnection) url.openConnection(); 
        // 设置连接网络的超时时间 
        httpURLConnection.setConnectTimeout(3000); 
        httpURLConnection.setDoInput(true); 
        // 表示设置本次http请求使用GET方式请求 
        httpURLConnection.setRequestMethod("GET"); 
        int responseCode = httpURLConnection.getResponseCode(); 
        if (responseCode == 200) { 
          // 从服务器获得一个输入流 
          inputStream = httpURLConnection.getInputStream();
        } 
      } 
    } catch (MalformedURLException e) { 
      // TODO Auto-generated catch block 
      e.printStackTrace(); 
    } catch (IOException e) { 
      // TODO Auto-generated catch block 
      e.printStackTrace(); 
    } 
    return inputStream; 
  } 
} 