package emailparser;

import java.util.Enumeration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetHeaders;

/**
 *
 * @author goonmaster
 * This application takes file as a parameter and passes the message body to the standard output.
 */
public class FileBodyParser {
    String filename;

    public FileBodyParser(String args){
        filename = args;
    }

    public void scan() throws MessagingException {
        File file = new File(filename);
        StringBuffer contents = new StringBuffer();
        BufferedReader reader = null;
        char c1 = 0xA;
        String headers = "";

        //convert contents of text file to a string object
        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;

            // repeat until all lines is read
            while ((text = reader.readLine()) != null) {
                contents.append(text).append(c1);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //convert String Buffer to a regular string
        //newlines still identical to origional message
        String converted = contents.toString();

        try {
            //create internet headers object
            InternetHeaders emailVar = new InternetHeaders();

            //convert string to input stream
            InputStream streamVar = new ByteArrayInputStream(converted.getBytes("US-ASCII"));

            //load input stream into internet headers object
            emailVar.load(streamVar);

            //print all lines
            Enumeration num = emailVar.getAllHeaderLines();

            //this bit turns our headers into a long string
            while(num.hasMoreElements()){
                //The rfc822 email header parser library
                //adds the hex character "0D" to headers that contain the character "0A"
                //The character "0D" must be removed
                headers += num.nextElement().toString().replaceAll("\r", "");
                headers += c1 ;
            }

            //getting expected results
            //System.out.print(headers);

            int l1 = headers.length();
            int l2 = converted.length();
            char arr[] = converted.toCharArray();

            /*
            Parameters:
            value - Array that is the source of characters
            offset - The initial offset
            count - The length
             */
            String subset = new String(arr, l1, l2-l1);

            System.out.print(subset);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }//end main method
}
